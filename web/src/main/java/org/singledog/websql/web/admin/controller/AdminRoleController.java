package org.singledog.websql.web.admin.controller;

import org.apache.ibatis.features.jpa.domain.Page;
import org.singledog.websql.user.entity.DbResource;
import org.singledog.websql.user.entity.Role;
import org.singledog.websql.user.entity.SoftDelete;
import org.singledog.websql.user.entity.UserRole;
import org.singledog.websql.user.mapper.DbResourceMapper;
import org.singledog.websql.user.mapper.RoleMapper;
import org.singledog.websql.user.mapper.UserRoleMapper;
import org.singledog.websql.user.param.RoleSearchParam;
import org.singledog.websql.user.param.UserSearchParam;
import org.singledog.websql.user.service.DbResourceService;
import org.singledog.websql.user.service.RoleService;
import org.singledog.websql.user.service.UserRoleService;
import org.singledog.websql.user.service.UserService;
import org.singledog.websql.web.param.GrantRoleParam;
import org.singledog.websql.web.result.CommonResult;
import org.singledog.websql.web.result.ListResult;
import org.singledog.websql.web.result.ResultCode;
import org.singledog.websql.web.vo.RoleResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Created by adam on 9/17/17.
 */
@RestController
@RequestMapping("/admin/role")
public class AdminRoleController {

    private static final Logger logger = LoggerFactory.getLogger(AdminRoleController.class);

    @Autowired
    private RoleService roleService;
    @Autowired
    private UserService userService;
    @Autowired
    private RoleMapper roleMapper;
    @Autowired
    private DbResourceService dbResourceService;
    @Autowired
    private DbResourceMapper dbResourceMapper;
    @Autowired
    private UserRoleMapper userRoleMapper;

    @RequestMapping(value = "/add", method = RequestMethod.POST)
    public CommonResult<String> addRole(String roleName) {
        Role role = roleService.addRole(0L, roleName);
        return CommonResult.newInstance(ResultCode.SUCCESS, String.class);
    }

    @RequestMapping("/list")
    public ListResult<RoleResult> list(Integer roleType, String roleName, Long userId, Integer page, Integer rows) {
        RoleSearchParam param = new RoleSearchParam();
        param.setRoleName(roleName);
        param.setRoleType(roleType);
        if (page == null)
            page = 1;
        if (rows == null)
            rows = 20;

        param.setPageNum(page);
        param.setPageSize(rows);
        Page<Role> rolePage = roleService.searchRole(param);
        ListResult<RoleResult> result = new ListResult<>(rolePage);

        Set<Long> roleIds = new HashSet<>();
        if (userId != null) {
            List<UserRole> userRoles = userRoleMapper.findByUserId(userId);
            if (!CollectionUtils.isEmpty(userRoles)) {
                roleIds.addAll(userRoles.stream().map(UserRole::getRoleId).collect(Collectors.toSet()));
            }
        }

        List<RoleResult> resultList = rolePage.getContent().stream().map(role -> {
            RoleResult r = new RoleResult(role);
            if (roleIds.contains(role.getId()))
                r.setChecked(true);
            return r;
        }).collect(Collectors.toList());
        userService.loadUserName(resultList);

        result.setRows(resultList);
        return result;
    }

    @RequestMapping(value = "/del/{id}",method = RequestMethod.POST)
    public CommonResult<String> deleteRole(@PathVariable("id") Long id) {
        roleService.deleteRole(id);
        return CommonResult.newInstance(ResultCode.SUCCESS, String.class);
    }

    @RequestMapping(value = "/grant")
    public CommonResult<String> grantResource(@RequestBody GrantRoleParam grantRoleParam) {
        Role role = roleMapper.findById(grantRoleParam.getRoleId());
        if (role == null)
            return CommonResult.newInstance(ResultCode.PARAM_ERROR, String.class).setMessage("角色不存在!");

        List<DbResource> dbResources = null;
        if (!CollectionUtils.isEmpty(grantRoleParam.getResourceIds())) {
            dbResources = dbResourceMapper.findByIdInAndDelete(grantRoleParam.getResourceIds(), SoftDelete.normal);
        } else {
            dbResources = Collections.emptyList();
        }

        if (grantRoleParam.isAppend() && CollectionUtils.isEmpty(dbResources)) {
            return CommonResult.newInstance(ResultCode.PARAM_ERROR, String.class).setMessage("资源不存在!");
        }

        if (grantRoleParam.isAppend()) {
            roleService.mergeResource(role, dbResources);
        } else {
            roleService.grantResource(role, dbResources);
        }

        return CommonResult.newInstance(ResultCode.SUCCESS, String.class);
    }

}
