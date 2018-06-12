package org.singledog.websql.user.auth;

import org.singledog.websql.user.entity.DbResource;
import org.singledog.websql.user.entity.Role;
import org.singledog.websql.user.entity.UserEntity;
import org.singledog.websql.user.event.EventType;
import org.singledog.websql.user.event.LoggerEventBuilder;
import org.singledog.websql.user.exceptions.LoginException;
import org.singledog.websql.user.param.HttpInfoHolder;
import org.singledog.websql.user.service.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

/**
 * Created by Adam on 2017/9/12.
 */
@Profile({"default","local","test"})
@Service
public class SystemUserAuthorityServiceImpl implements UserAuthorityService {
    private static final Logger logger = LoggerFactory.getLogger(SystemUserAuthorityServiceImpl.class);

    @Autowired
    private UserService userService;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private SaltGenerator saltGenerator;
    @Autowired
    private UserRoleService userRoleService;
    @Autowired
    private RoleService roleService;
    @Autowired
    private ApplicationContext applicationContext;

    @Override
    public UserEntity login(String userNameOrEmail, String password) {
        logger.info("user trying login , username: {}", userNameOrEmail);
        PasswordValidator.validate(password);
        UserEntity userEntity = userService.getUserByEmail(userNameOrEmail);
        if (userEntity == null) {
            logger.info("get user with email failed ! try username !");
            userEntity = userService.getUserByUserName(userNameOrEmail);
        }

        userService.checkStatus(userEntity);

        if (userEntity == null) {
            logger.error("login failed ! username not exists ! {}", userNameOrEmail);
            throw new LoginException("用户名或密码错误!");
        }

        if (!passwordEncoder.matches(saltGenerator.wrapSalt(password, userEntity.getSalt()), userEntity.getPassword())) {
            logger.error("login failed ! wrong password !");
            throw new LoginException("用户名或密码错误!");
        }

        applicationContext.publishEvent(LoggerEventBuilder
                .getInstance(SystemUserAuthorityServiceImpl.class)
                .eventType(EventType.LOGIN)
                .userEntity(userEntity)
                .params("email:".concat(userNameOrEmail))
                .remark("user id: ".concat(String.valueOf(userEntity.getId())))
                .build());
        return userEntity;
    }

    @Override
    public List<DbResource> getAuthenticatedResources(UserEntity userEntity) {
        if (userEntity == null)
            return Collections.emptyList();

        List<Role> roles = userRoleService.findRoles(userEntity.getId());
        return roleService.listDbResource(roles);
    }
}
