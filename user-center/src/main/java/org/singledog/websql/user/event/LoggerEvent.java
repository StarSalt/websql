package org.singledog.websql.user.event;

import com.alibaba.fastjson.JSON;
import org.singledog.websql.user.entity.OperateLog;
import org.singledog.websql.user.entity.UserEntity;
import org.springframework.context.ApplicationEvent;

import java.util.Date;

/**
 * Created by Adam on 2017/9/12.
 */
public class LoggerEvent extends ApplicationEvent {

    private EventType eventType;

    private UserEntity userEntity;

    private Object params;

    private String remark;

    private String requestId;

    /**
     * Create a new ApplicationEvent.
     *
     * @param source the object on which the event initially occurred (never {@code null})
     */
    public LoggerEvent(Object source) {
        super(source);
    }

    public EventType getEventType() {
        return eventType;
    }

    public void setEventType(EventType eventType) {
        this.eventType = eventType;
    }

    public UserEntity getUserEntity() {
        return userEntity;
    }

    public void setUserEntity(UserEntity userEntity) {
        this.userEntity = userEntity;
    }

    public Object getParams() {
        return params;
    }

    public void setParams(Object params) {
        this.params = params;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getRequestId() {
        return requestId;
    }

    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }

    @Override
    public String toString() {
        return new StringBuilder()
                .append("[ ").append(userEntity.getId()).append(", ")
                .append(userEntity.getEmail())
                .append(",").append(userEntity.getUserName())
                .append(" ]").append(" ").append(this.eventType.name())
                .append(", params : ").append(params)
                .append(", remark : ").append(remark)
                .toString();
    }

    public OperateLog toOperateLog() {
        OperateLog operateLog = new OperateLog();
        operateLog.defaultProperty();
        operateLog.setRequestId(this.getRequestId());
        operateLog.setUserId(this.getUserEntity().getId());
        operateLog.setUserName(this.getUserEntity().getUserName());
        operateLog.setRemark(this.getRemark());
        operateLog.setCreateTime(new Date());
        operateLog.setEventName(this.getEventType().getDesc());
        operateLog.setEventType(this.getEventType().name());
        operateLog.setOpTime(new Date());
        operateLog.setParams(JSON.toJSONString(this.getParams()));
        operateLog.setUserEmail(this.getUserEntity().getEmail());
        return operateLog;
    }
}
