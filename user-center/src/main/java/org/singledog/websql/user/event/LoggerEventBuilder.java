package org.singledog.websql.user.event;

import org.singledog.websql.user.entity.UserEntity;
import org.slf4j.MDC;

/**
 * Created by Adam on 2017/9/12.
 */
public class LoggerEventBuilder {

    private LoggerEvent loggerEvent;

    private LoggerEventBuilder(Object source) {
        loggerEvent = new LoggerEvent(source);
    }

    public static LoggerEventBuilder getInstance(Object source) {
        return new LoggerEventBuilder(source);
    }

    public LoggerEvent build() {
        loggerEvent.setRequestId(MDC.get("rid"));
        return loggerEvent;
    }

    public LoggerEventBuilder eventType(EventType eventType) {
        loggerEvent.setEventType(eventType);
        return this;
    }

    public LoggerEventBuilder userEntity(UserEntity userEntity) {
        loggerEvent.setUserEntity(userEntity);
        return this;
    }

    public LoggerEventBuilder params(Object params) {
        loggerEvent.setParams(params);
        return this;
    }

    public LoggerEventBuilder remark(String remark) {
        loggerEvent.setRemark(remark);
        return this;
    }

}
