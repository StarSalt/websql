package org.singledog.websql.web.util;

import org.singledog.websql.user.entity.UserEntity;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by Adam on 2017/8/21.
 */
public class HttpRequestUtil {

    public static final String USER_SESSION_KEY = "_user.session.key_";

    public static void setUser(HttpServletRequest request, UserEntity userEntity) {
        request.getSession().setAttribute(USER_SESSION_KEY, userEntity);
    }

    public static UserEntity getUser(HttpServletRequest request) {
        return (UserEntity) request.getSession().getAttribute(USER_SESSION_KEY);
    }

}
