package org.singledog.websql.web.result;

public enum ResultCode implements ReturnResult {
    SUCCESS(200, "success", 200),

    NO_RESULT(204, "无结果!", 204),
    REDIRECT(302, "请求跳转!", 204),
    BAD_REQUEST(400, "请求无效!", 400),
    UNAUTHORIZED(401, "未授权!", 401),
    FORBIDDEN(403, "拒绝访问!", 403),
    SERVER_ERROR(500, "服务器内部错误!", 500),
    PARAM_ERROR(1001, "参数有误!", 401),
    LOGIN_FAILED(1002, "登录失败!", 401),
    SMS_CODE_ERROR(1003, "短信验证码错误!", 401),
    THIRD_USER_PROFILE_FAILED(1004, "获取第三方用户资料失败!", 401),
    PRODUCT_ERROR(1005, "product 错误!", 401),
    PHONE_ALREADY_EXISTS(1006, "手机号已存在!", 200),
    PHONE_NOT_EXISTS(1007, "手机号不存在!", 200),
    EMAIL_ALREADY_EXISTS(1008, "邮箱已经存在!", 200),
    EMAIL_NOT_EXISTS(1009, "邮箱不存在!", 200),
    USER_NOT_EXISTS(1010, "用户不存在!", 401),
    VERIFY_CODE_ERROR(1011, "验证码错误!", 401),
    SEND_SMS_ERROR(1012, "短信发送失败， 请稍后重试!", 401),
    SEND_EMAIL_ERROR(1013, "发送邮件失败!", 401),
    VERIFY_CODE_EXPIRED(1014, "验证码已过期!", 401),
    NOT_LOGIN(1015, "用户未登录!", 401),
    UNBIND_NOT_SATISFIED(1016, "不满足解绑条件!", 401),
    NEED_VERIFY_CODE(1017, "多次登录失败， 需要输入验证码!", 401),
    TOKEN_EXPIRED(1018, "token已过期!", 401),
    TOKEN_INVALID(1019, "token无效!", 401),
    ACTIVE_LINK_INVALID(1020, "链接已失效!", 401),
    ACTIVE_LINK_ERROR(1037, "链接已损坏!", 401),
    NAME_OR_PWD_ERROR(1021, "用户名或密码错误!", 401),
    PASSWORD_ERROR(1022, "密码错误!", 401),
    ACCOUNT_OR_PASSWORD_ERROR(1038, "用户名或密码错误!", 401),
    ACCOUNT_NOT_ACTIVED(1023, "帐号未激活!", 401),
    ACCOUNT_EXPIRED(1024, "帐号已过期!", 401),
    ACCOUNT_DISABLED(1025, "帐号已禁用!", 401),
    ACCOUNT_LOCKED(1026, "帐号已锁定!", 401),
    TOKEN_NOT_MATCH(1027, "Token不匹配!", 401),
    SEND_SMS_LIMIT(1028, "发送短信已超出限制!", 401),
    SEND_EMAIL_LIMIT(1029, "发送邮件已超出限制!", 401),
    SEND_SMS_FREQ(1030, "发送短信过于频繁，稍后再试!", 401),
    SEND_EMAIL_FREQ(1031, "发送邮件过于频繁，稍后再试!", 401),
    PHONE_BIND_EXISTS(1032, "绑定失败，账号已绑定手机号!", 200),
    ACCOUNT_ALREADY_ACTIVED(1033, "帐号已经激活!", 401),
    CAR_ALREADY_EXISTS(1034, "车已经存在!", 401),
    THIRD_USER_BIND_FAILED(1035, "第三方绑定失败!", 401),
    THIRD_USER_ALREADY_BIND_FAILED(1036, "绑定已经存在!", 401),
    NICKNAME_EXISTS(1037, "昵称已存在!", 200),
    NICKNAME_NOT_EXISTS(1038, "昵称不存在!", 200),

//    	ACCOUNT_INVALID(1027, "帐号已注销!", 403)
    ;

    private int code;
    private int httpCode;
    private String message;

    private ResultCode(int code, String message, int httpCode) {
        this.code = code;
        this.message = message;
        this.httpCode = httpCode;
    }

    @Override
    public int code() {
        return code;
    }

    @Override
    public String message() {
        return message;
    }

    public int httpCode() {
        return httpCode;
    }

    public static ResultCode valueOf(int code) {
        for (ResultCode resultCode : values()) {
            if (resultCode.code() == code) {
                return resultCode;
            }
        }

        throw new IllegalArgumentException("No Constants match ! code : " + code);
    }
}