package org.singledog.websql.user.exceptions;

/**
 * Created by Adam on 2017/9/8.
 */
public class NoAuthorityException extends ServiceException {

    public NoAuthorityException() {
    }

    public NoAuthorityException(String message) {
        super(message);
    }

    public NoAuthorityException(Throwable cause) {
        super(cause);
    }

    public NoAuthorityException(String message, Throwable throwable) {
        super(message, throwable);
    }
}
