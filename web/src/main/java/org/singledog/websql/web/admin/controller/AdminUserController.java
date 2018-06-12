package org.singledog.websql.web.admin.controller;

import org.apache.ibatis.features.jpa.domain.Page;
import org.apache.ibatis.utils.CollectionUtils;
import org.singledog.websql.user.entity.Role;
import org.singledog.websql.user.entity.SoftDelete;
import org.singledog.websql.user.entity.UserEntity;
import org.singledog.websql.user.param.UserParam;
import org.singledog.websql.user.param.UserSearchParam;
import org.singledog.websql.user.service.RoleService;
import org.singledog.websql.user.service.UserRoleService;
import org.singledog.websql.user.service.UserService;
import org.singledog.websql.user.util.StringUtil;
import org.singledog.websql.web.param.GrantRole4UserParam;
import org.singledog.websql.web.result.CommonResult;
import org.singledog.websql.web.result.ListResult;
import org.singledog.websql.web.result.ResultCode;
import org.singledog.websql.web.vo.UserVo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by Adam on 2017/9/12.
 */
@RestController
@RequestMapping("/admin/user")
public class AdminUserController {

    private static final Logger logger = LoggerFactory.getLogger(AdminUserController.class);

    @Autowired
    private UserService userService;
    @Autowired
    private UserRoleService userRoleService;
    @Autowired
    private RoleService roleService;

    @RequestMapping("/list")
    public ListResult<UserVo> list(UserSearchParam searchParam) {
        if (searchParam.getPage() == null) {
            searchParam.setPage(0);
        } else {
            searchParam.setPage(searchParam.getPage() - 1);
        }

        if (searchParam.getRows() == null)
            searchParam.setRows(20);

        searchParam.setUserEmail(StringUtil.emptyToNull(searchParam.getUserEmail()));
        if (!StringUtils.isEmpty(searchParam.getUserEmail())) {
            searchParam.setUserEmail(StringUtil.wrapLike(searchParam.getUserEmail()));
        }

        searchParam.setUserName(StringUtil.emptyToNull(searchParam.getUserName()));
        if (!StringUtils.isEmpty(searchParam.getUserName())) {
            searchParam.setUserName(StringUtil.wrapLike(searchParam.getUserName()));
        }

        if (searchParam.getDelete() == null)
            searchParam.setDelete(SoftDelete.normal);

        List<UserVo> userVos;
        Page<UserEntity> userEntities = userService.listUsers(searchParam);
        if (CollectionUtils.isEmpty(userEntities.getContent())) {
            userVos = Collections.emptyList();
        } else {
            userVos = userEntities.getContent().stream().map(UserVo::new).collect(Collectors.toList());
            userService.loadUserName(userVos);
        }

        ListResult<UserVo> listResult = new ListResult<>(userEntities);
        listResult.setRows(userVos);
        return listResult;
    }

    @RequestMapping(value = "/add", method = RequestMethod.POST)
    public CommonResult<String> addUser(@RequestBody @Valid UserParam userParam) {
        try {
            UserEntity userEntity = userService.addUser(userParam);
            return CommonResult.newInstance(ResultCode.SUCCESS, String.class);
        } catch (Exception e) {
            return CommonResult.newInstance(ResultCode.SERVER_ERROR, String.class).setMessage(e.getMessage());
        }
    }

    @RequestMapping(value = "/update", method = RequestMethod.POST)
    public CommonResult<String> updateUser(@RequestBody UserParam userParam) {
        if (userParam.getId() == null)
            return CommonResult.newInstance(ResultCode.PARAM_ERROR, String.class);

        try {
            userService.updateUser(userParam);
            return CommonResult.newInstance(ResultCode.SUCCESS, String.class);
        } catch (Exception e) {
            return CommonResult.newInstance(ResultCode.SERVER_ERROR, String.class).setMessage(e.getMessage());
        }
    }

    @RequestMapping(value = "/block/{id}/{state}", method = RequestMethod.POST)
    public CommonResult<String> blockUser(@PathVariable("id") Long userId, @PathVariable("state") Byte status) {
        int i = userService.lockUser(userId, status);
        return CommonResult.newInstance(ResultCode.SUCCESS, String.class).setMessage("锁定/解锁成功! " + i);
    }

    @RequestMapping(value = "/grant/role", method = RequestMethod.POST)
    public CommonResult<String> grantRoles(@RequestBody GrantRole4UserParam userParam) {
        UserEntity entity = userService.getByUserId(userParam.getUserId());
        if (entity == null) {
            return CommonResult.newInstance(ResultCode.PARAM_ERROR, String.class).setMessage("用户不存在!");
        }
        List<Role> roles;
        if (CollectionUtils.isEmpty(userParam.getRoleIds())) {
            roles = Collections.emptyList();
        } else {
            roles = roleService.findByIds(userParam.getRoleIds());
        }

        try {
            userRoleService.grantRoles(entity, roles.toArray(new Role[roles.size()]));
            return CommonResult.newInstance(ResultCode.SUCCESS, String.class);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return CommonResult.newInstance(ResultCode.SERVER_ERROR, String.class).setMessage(e.getMessage());
        }

    }

}
