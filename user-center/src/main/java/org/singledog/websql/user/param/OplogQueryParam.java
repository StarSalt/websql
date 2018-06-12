package org.singledog.websql.user.param;

/**
 * Created by Adam on 2018-06-12.
 */
public class OplogQueryParam extends PagingParam {

    private String userName;
    private String userEmail;
    private String eventType;

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public String getEventType() {
        return eventType;
    }

    public void setEventType(String eventType) {
        this.eventType = eventType;
    }
}
