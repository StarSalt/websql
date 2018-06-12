package org.singledog.websql.user.auth;

import org.singledog.websql.user.entity.DbResource;
import org.singledog.websql.user.entity.UserEntity;

import java.util.List;

/**
 * Created by Adam on 2017/9/12.
 */
public interface UserAuthorityService {

    /**
     * try login using username or email and password
     * @param userNameOrEmail
     * @param password
     * @return UserEntity if login success, or throw {@link org.singledog.websql.user.exceptions.LoginException} if login failed
     */
    UserEntity login(String userNameOrEmail, String password);

    /**
     * get user authenticated resources
     * @param userEntity
     * @return authenticated resources, or empty list if none authenticated resource found
     */
    List<DbResource> getAuthenticatedResources(UserEntity userEntity);

}
