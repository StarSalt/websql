package org.singledog.websql.user.param;

import org.singledog.websql.user.entity.UserEntity;
import org.singledog.websql.user.exceptions.NoAuthorityException;
import org.singledog.websql.user.exceptions.NotLoginException;

/**
 * Created by Adam on 2017/9/8.
 */
public class HttpInfoHolder {

    private static final ThreadLocal<UserEntity> userHolder = new ThreadLocal<>();

    public static void setUserEntity(UserEntity userEntity) {
        userHolder.set(userEntity);
    }

    public static UserEntity getUserEntity() {
        return userHolder.get();
    }

    public static UserEntity getUserEntityNotNull() {
        UserEntity userEntity = getUserEntity();
        if (userEntity == null)
            throw new NotLoginException();

        return userEntity;
    }

    public static UserEntity getAdmin() {
        UserEntity entity = getUserEntityNotNull();
        if (entity.getAdmin() != null && entity.getAdmin() == UserEntity.ADMIN_Y)
            return entity;

        throw new NoAuthorityException();
    }

}
