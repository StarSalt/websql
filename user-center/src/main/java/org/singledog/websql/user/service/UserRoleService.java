package org.singledog.websql.user.service;

import org.singledog.websql.user.entity.Role;
import org.singledog.websql.user.entity.RoleResource;
import org.singledog.websql.user.entity.UserEntity;
import org.singledog.websql.user.entity.UserRole;
import org.singledog.websql.user.event.EventType;
import org.singledog.websql.user.event.LoggerEventBuilder;
import org.singledog.websql.user.mapper.RoleResourceMapper;
import org.singledog.websql.user.mapper.UserRoleMapper;
import org.singledog.websql.user.param.HttpInfoHolder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by Adam on 2017/9/12.
 */
@Service
public class UserRoleService {

    private static final Logger logger = LoggerFactory.getLogger(UserRoleService.class);

    @Autowired
    private UserRoleMapper userRoleMapper;
    @Autowired
    private RoleService roleService;
    @Autowired
    private ApplicationContext applicationContext;
    @Autowired
    private RoleResourceMapper roleResourceMapper;

    @Transactional
    public void grantRoles(UserEntity userEntity, Role ... roles) {
        if (userEntity == null) {
            return ;
        }

        HttpInfoHolder.getAdmin();
        userRoleMapper.deleteByUserId(userEntity.getId());
        if (roles == null || roles.length == 0) {
            return;
        }

        logger.info("grant roles for user: {}, {}", userEntity.getId(), userEntity.getEmail());
        List<Role> hadRoles = this.findRoles(userEntity.getId());
        Map<Long, Role> roleMap = hadRoles.stream().collect(Collectors.toMap(Role::getId, r -> r));
        for (Role role : roles) {
            if (roleMap.containsKey(role.getId())) {
                logger.info("user already had role : {}, {}", role.getId(), role.getRoleName());
                continue;
            }

            logger.info("granting role : {} , {}", role.getId(), role.getRoleName());
            UserRole userRole = new UserRole(userEntity, role);
            userRole.defaultProperty();
            this.userRoleMapper.saveAutoIncrementKey(userRole);
        }

        applicationContext.publishEvent(LoggerEventBuilder
                .getInstance(DatabaseService.class)
                .eventType(EventType.GRANT_ROLE_TO_USER)
                .userEntity(HttpInfoHolder.getUserEntity())
                .params("")
                .remark("user id: ".concat(String.valueOf(userEntity.getId())))
                .build());

        logger.info("grant complete !");
    }

    @Transactional
    public int revokeRoles(Long userId, Long ... roleIds) {
        logger.info("revoking roles : {}, userId: {}", Arrays.toString(roleIds), userId);
        if (userId == null || roleIds == null || roleIds.length == 0)
            return 0;

        HttpInfoHolder.getAdmin();
        int i = userRoleMapper.deleteByUserIdAndRoleIdIn(userId, Arrays.asList(roleIds));
        applicationContext.publishEvent(LoggerEventBuilder
                .getInstance(DatabaseService.class)
                .eventType(EventType.REVOKE_ROLE_FROM_USER)
                .userEntity(HttpInfoHolder.getUserEntity())
                .params("roleIds: ".concat(Arrays.toString(roleIds)))
                .remark("user id: ".concat(String.valueOf(userId)))
                .build());
        return i;
    }

    public List<Role> findRoles(Long userId) {
        List<UserRole> userRoles = userRoleMapper.findByUserId(userId);
        if (CollectionUtils.isEmpty(userRoles)) {
            return Collections.emptyList();
        }

        List<Long> roleIds = userRoles.stream().map(UserRole::getRoleId).collect(Collectors.toList());
        return roleService.findByIds(roleIds);
    }

    public List<RoleResource> listRoleResources(Long userId, Long databaseId) {
        List<UserRole> userRoles = userRoleMapper.findByUserId(userId);
        if (CollectionUtils.isEmpty(userRoles)) {
            return Collections.emptyList();
        }

        List<Long> roleIds = userRoles.stream().map(UserRole::getRoleId).collect(Collectors.toList());
        return roleResourceMapper.findByRoleIdIn(roleIds, databaseId);
    }

    public List<String> listTableNames(Long userId, Long databaseId) {
        List<UserRole> userRoles = userRoleMapper.findByUserId(userId);
        if (CollectionUtils.isEmpty(userRoles)) {
            return Collections.emptyList();
        }

        List<Long> roleIds = userRoles.stream().map(UserRole::getRoleId).collect(Collectors.toList());
        List<String> tableNames = roleResourceMapper.findByRoleIdIn(roleIds, databaseId)
                .stream()
                .map(r -> r.getDatabaseId() + "." + r.getTableName())
                .collect(Collectors.toList());

        return tableNames;
    }

    public boolean hasAuthority(Collection<String> tableNames, Long databaseId, String tableName) {
        return tableNames.contains(String.valueOf(databaseId).concat(".").concat(tableName));
    }

}
