package org.singledog.websql.user.entity;

/**
 * Created by Adam on 2017/8/15.
 */
public enum Functions {

    login("登录验证码"),

    find_password("重置密码邮件"),

    register("注册邮箱激活");

    private String subject;

    private Functions(String subject) {
        this.subject = subject;
    }

    public String getSubject() {
        return subject;
    }
}
