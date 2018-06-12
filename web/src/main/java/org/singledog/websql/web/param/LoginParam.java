package org.singledog.websql.web.param;

import org.hibernate.validator.constraints.NotEmpty;

/**
 * Created by Adam on 2017/9/14.
 */
public class LoginParam extends SendVerifyCodeParam {

    @NotEmpty
    private String password;
    @NotEmpty
    private String verifyCode;

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getVerifyCode() {
        return verifyCode;
    }

    public void setVerifyCode(String verifyCode) {
        this.verifyCode = verifyCode;
    }
}
