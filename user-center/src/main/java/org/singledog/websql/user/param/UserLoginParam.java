package org.singledog.websql.user.param;

import org.hibernate.validator.constraints.NotEmpty;

/**
 * Created by Adam on 2017/9/12.
 */
public class UserLoginParam {

    @NotEmpty
    private String userNameOrEmail;
    @NotEmpty
    private String password;

    public String getUserNameOrEmail() {
        return userNameOrEmail;
    }

    public void setUserNameOrEmail(String userNameOrEmail) {
        this.userNameOrEmail = userNameOrEmail;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
