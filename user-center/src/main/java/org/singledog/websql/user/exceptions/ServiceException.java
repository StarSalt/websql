package org.singledog.websql.user.exceptions;

/**
 * Created by Adam on 2017/9/8.
 */
public class ServiceException extends RuntimeException {

    public ServiceException() {
        super();
    }

    public ServiceException(String message) {
        super(message);
    }

    public ServiceException(Throwable cause) {
        super(cause);
    }

    public ServiceException(String message, Throwable throwable) {
        super(message, throwable);
    }

}
