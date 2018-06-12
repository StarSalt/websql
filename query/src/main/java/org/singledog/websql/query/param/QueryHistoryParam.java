package org.singledog.websql.query.param;

import org.singledog.websql.user.param.PagingParam;

/**
 * Created by Adam on 2018-06-11.
 */
public class QueryHistoryParam extends PagingParam {

    private String tableName;
    private Long dbId;
    private String userName;
    private String userEmail;

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public Long getDbId() {
        return dbId;
    }

    public void setDbId(Long dbId) {
        this.dbId = dbId;
    }

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
}
