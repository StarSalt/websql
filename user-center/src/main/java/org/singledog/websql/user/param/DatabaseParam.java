package org.singledog.websql.user.param;

import org.hibernate.validator.constraints.NotEmpty;

/**
 * Created by Adam on 2017/9/8.
 */
public class DatabaseParam {

    private Long id;
    @NotEmpty
    private String dbName;
    @NotEmpty
    private String url;
    @NotEmpty
    private String userName;
    @NotEmpty
    private String password;
    @NotEmpty
    private String dbType;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDbName() {
        return dbName;
    }

    public void setDbName(String dbName) {
        this.dbName = dbName;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getDbType() {
        return dbType;
    }

    public void setDbType(String dbType) {
        this.dbType = dbType;
    }
}
