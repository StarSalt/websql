package org.singledog.websql.web.param;

import org.hibernate.validator.constraints.NotEmpty;

/**
 * Created by Adam on 2017/9/14.
 */
public class SendVerifyCodeParam {

    @NotEmpty
    private String emailOrUname;

    public String getEmailOrUname() {
        return emailOrUname;
    }

    public void setEmailOrUname(String emailOrUname) {
        this.emailOrUname = emailOrUname;
    }
}
