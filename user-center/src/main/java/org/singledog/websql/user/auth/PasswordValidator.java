package org.singledog.websql.user.auth;

import org.apache.ibatis.utils.Assert;
import org.singledog.websql.user.exceptions.ServiceException;

/**
 * Created by Adam on 2017/12/20.
 */
public class PasswordValidator {

    public static void validate(String password) {
        Assert.notNull(password, "密码不能为空!");
        if (!password.matches("(?=.*[0-9].*)(?=.*[A-Z].*)(?=.*[a-z].*).{6,20}$")) {
            throw new ServiceException("密码强度太弱! 密码需符合： 6-20位、同时包含大写、小写、数字");
        }
    }

}
