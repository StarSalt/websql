package org.singledog.websql.user.exceptions;

/**
 * Created by Adam on 2017/9/12.
 */
public class LoginException extends ServiceException {

    public LoginException() {
    }

    public LoginException(String message) {
        super(message);
    }

    public LoginException(String message, Throwable throwable) {
        super(message, throwable);
    }
}
