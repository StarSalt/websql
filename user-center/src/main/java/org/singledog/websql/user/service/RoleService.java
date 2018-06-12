package org.singledog.websql.user.service;

import com.alibaba.fastjson.JSON;
import org.apache.ibatis.features.jpa.domain.Page;
import org.apache.ibatis.features.jpa.domain.PageRequest;
import org.apache.ibatis.features.jpa.domain.Sort;
import org.singledog.websql.user.entity.*;
import org.singledog.websql.user.event.EventType;
import org.singledog.websql.user.event.LoggerEventBuilder;
import org.singledog.websql.user.mapper.DbResourceMapper;
import org.singledog.websql.user.mapper.RoleMapper;
import org.singledog.websql.user.mapper.RoleResourceMapper;
import org.singledog.websql.user.param.HttpInfoHolder;
import org.singledog.websql.user.param.RoleSearchParam;
import org.singledog.websql.user.util.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by Adam on 2017/9/8.
 */
@Service
public class RoleService {

    private static final Logger logger = LoggerFactory.getLogger(RoleService.class);

    @Autowired
    private RoleMapper roleMapper;
    @Autowired
    private DbResourceMapper resourceMapper;
    @Autowired
    private RoleResourceMapper roleResourceMapper;
    @Autowired
    private ApplicationContext applicationContext;

    /**
     * admin create a role
     * @param departId
     * @param roleName
     * @return
     */
    @Transactional
    public Role addRole(Long departId, String roleName) {
        logger.info("create role , depart : {}, name : {}",departId, roleName);
        HttpInfoHolder.getAdmin();
        Role role = new Role();
        role.defaultProperty();
        role.setRoleName(roleName);
        role.setDepartId(departId);
        role.setRoleType(Role.ROLE_TYPE_NORMAL);
        this.roleMapper.saveAutoIncrementKey(role);

        applicationContext.publishEvent(LoggerEventBuilder
                .getInstance(DatabaseService.class)
                .eventType(EventType.CREATE_ROLE)
                .userEntity(HttpInfoHolder.getUserEntity())
                .params(JSON.toJSONString("departId:".concat(String.valueOf(departId).concat(",roleName:").concat(roleName))))
                .remark("role id: ".concat(String.valueOf(role.getId())))
                .build());

        return role;
    }

    /**
     * system create anonymous role
     * @param departId
     * @return
     */
    @Transactional
    public Role addSystemRole(Long departId) {
        logger.info("create system role for depart : {}", departId);
        Role role = new Role();
        role.defaultProperty();
        role.setRoleType(Role.ROLE_TYPE_ANONYMOUS);
        role.setDepartId(departId);
        role.setRoleName("ANONYMOUS.".concat(String.valueOf(System.currentTimeMillis())));
        this.roleMapper.saveAutoIncrementKey(role);
        applicationContext.publishEvent(LoggerEventBuilder
                .getInstance(DatabaseService.class)
                .eventType(EventType.CREATE_ANONYMOUS_ROLE)
                .userEntity(HttpInfoHolder.getUserEntity())
                .params(JSON.toJSONString("departId:".concat(String.valueOf(departId).concat(",roleName:").concat(role.getRoleName()))))
                .remark("role id: ".concat(String.valueOf(role.getId())))
                .build());
        return role;
    }

    /**
     * search role by role name and type
     * @param roleSearchParam
     * @return
     */
    public Page<Role> searchRole(RoleSearchParam roleSearchParam) {
        if (roleSearchParam.getDelete() == null)
            roleSearchParam.setDelete(SoftDelete.normal);
        if (roleSearchParam.getRoleType() == null)
            roleSearchParam.setRoleType(Role.ROLE_TYPE_NORMAL);
        roleSearchParam.setRoleName(StringUtil.emptyToNull(roleSearchParam.getRoleName()));
        if (!StringUtils.isEmpty(roleSearchParam.getRoleName()))
            roleSearchParam.setRoleName(StringUtil.wrapLike(roleSearchParam.getRoleName()));

        return roleMapper.searchRoles(roleSearchParam, PageRequest.of(roleSearchParam.getPageNum() - 1,
                roleSearchParam.getPageSize(), new Sort(Sort.Direction.DESC, "create_time")));
    }

    @Transactional
    public int deleteRole(Long roleId) {
        int i = roleMapper.softDelete(roleId);
        applicationContext.publishEvent(LoggerEventBuilder
                .getInstance(DatabaseService.class)
                .eventType(EventType.DELETE_ROLE)
                .userEntity(HttpInfoHolder.getUserEntity())
                .params(JSON.toJSONString("roleName:".concat(String.valueOf(roleId))))
                .remark("role id: ".concat(String.valueOf(roleId)))
                .build());

        return i;
    }

    /**
     * find list by id
     * @param idList
     * @return
     */
    public List<Role> findByIds(List<Long> idList) {
        return roleMapper.findByIdInAndRoleTypeAndDelete(idList, Role.ROLE_TYPE_NORMAL, SoftDelete.normal);
    }

    /**
     * merge role resources
     * @param role
     * @param dbResources
     * @return
     */
    @Transactional
    public List<RoleResource> mergeResource(Role role, List<DbResource> dbResources) {
        HttpInfoHolder.getAdmin();
        List<RoleResource> roleResources = listResources(role);
        if (CollectionUtils.isEmpty(dbResources))
            return roleResources;

        List<RoleResource> result = new LinkedList<>();
        Map<Long, RoleResource> roleResourceMap = roleResources.stream()
                .collect(Collectors.toMap(RoleResource::getResourceId, r -> r));
        List<Long> id = new ArrayList<>(dbResources.size());
        for (DbResource dbResource : dbResources) {
            if (dbResource.getId() == null) {
                logger.warn("DB resource has not been persisted ! Ignoring !", JSON.toJSONString(dbResource));
                continue;
            }
            id.add(dbResource.getId());
            if (roleResourceMap.containsKey(dbResource.getId())) {
                logger.info("db resource has already exists !");
                result.add(roleResourceMap.get(dbResource.getId()));
                continue;
            }

            RoleResource roleResource = new RoleResource();
            roleResource.defaultProperty();
            roleResource.setDatabaseId(dbResource.getDatabaseId());
            roleResource.setResourceId(dbResource.getId());
            roleResource.setRoleId(role.getId());
            roleResource.setTableName(dbResource.getTableName());
            this.roleResourceMapper.saveAutoIncrementKey(roleResource);
            result.add(roleResource);
        }

        applicationContext.publishEvent(LoggerEventBuilder
                .getInstance(DatabaseService.class)
                .eventType(EventType.GRANT_RESOURCE_TO_ROLE)
                .userEntity(HttpInfoHolder.getUserEntity())
                .params("resourceId: " + JSON.toJSONString(id))
                .remark("role id: ".concat(String.valueOf(role.getId())))
                .build());

        return result;
    }

    /**
     * re-grant role resource
     * @param role
     * @param dbResources
     * @return
     */
    @Transactional
    public List<RoleResource> grantResource(Role role, List<DbResource> dbResources) {
        this.roleResourceMapper.deleteByRoleId(role.getId());
        List<RoleResource> roleResources = new ArrayList<>(dbResources.size());

        List<Long> ids = new ArrayList<>(dbResources.size());
        for (DbResource dbResource : dbResources) {
            if (dbResource.getId() == null) {
                logger.warn("DB resource has not been persisted ! Ignoring !", JSON.toJSONString(dbResource));
                continue;
            }

            ids.add(dbResource.getId());
            RoleResource roleResource = new RoleResource();
            roleResource.defaultProperty();
            roleResource.setDatabaseId(dbResource.getDatabaseId());
            roleResource.setResourceId(dbResource.getId());
            roleResource.setRoleId(role.getId());
            roleResource.setTableName(dbResource.getTableName());
            this.roleResourceMapper.saveAutoIncrementKey(roleResource);
            roleResources.add(roleResource);
        }

        applicationContext.publishEvent(LoggerEventBuilder
                .getInstance(DatabaseService.class)
                .eventType(EventType.MERGE_RESOURCE_TO_ROLE)
                .userEntity(HttpInfoHolder.getUserEntity())
                .params("resourceId: " + JSON.toJSONString(ids))
                .remark("role id: ".concat(String.valueOf(role.getId())))
                .build());

        return roleResources;
    }

    public List<RoleResource> listResources(Role ... roles) {
        if (roles == null || roles.length == 0)
            return Collections.emptyList();

        List<Long> roleIds = new ArrayList<>(roles.length);
        for (Role role : roles) {
            roleIds.add(role.getId());
        }

        return roleResourceMapper.findByRoleIdIn(roleIds, null);
    }

    public List<DbResource> listDbResource(Collection<Role> roleList) {
        List<RoleResource> roleResources = listResources(roleList.toArray(new Role[roleList.size()]));
        if (CollectionUtils.isEmpty(roleResources))
            return Collections.emptyList();

        List<Long> resourceIds = roleResources.stream().map(RoleResource::getResourceId).collect(Collectors.toList());
        return resourceMapper.findByIdInAndDelete(resourceIds, SoftDelete.normal);
    }

}
