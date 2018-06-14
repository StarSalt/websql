package org.singledog.websql.web.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import org.singledog.websql.user.entity.DataBase;
import org.singledog.websql.user.service.UserNameRequired;
import org.springframework.beans.BeanUtils;

import java.util.Date;

/**
 * Created by Adam on 2017/9/15.
 */
public class DatabaseResult implements UserNameRequired {

    private Long id;

    private String dbName;

    private String url;

    private String userName;

    private String dbType;

    private Long createUserId;

    private String createUserName;

    private String updateUserName;

    private Long updateUserId;

    @JsonFormat(shape= JsonFormat.Shape.STRING, pattern="yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    private Date createTime;
    @JsonFormat(shape= JsonFormat.Shape.STRING, pattern="yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    private Date updateTime;

    public DatabaseResult() {}

    public DatabaseResult(DataBase dataBase) {
        BeanUtils.copyProperties(dataBase, this);
    }

    @Override
    public String[][] idNameFieldMapping() {
        return new String[][]{{"createUserId","createUserName"},{"updateUserId","updateUserName"}};
    }

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

    public String getDbType() {
        return dbType;
    }

    public void setDbType(String dbType) {
        this.dbType = dbType;
    }

    public Long getCreateUserId() {
        return createUserId;
    }

    public void setCreateUserId(Long createUserId) {
        this.createUserId = createUserId;
    }

    public String getCreateUserName() {
        return createUserName;
    }

    public void setCreateUserName(String createUserName) {
        this.createUserName = createUserName;
    }

    public String getUpdateUserName() {
        return updateUserName;
    }

    public void setUpdateUserName(String updateUserName) {
        this.updateUserName = updateUserName;
    }

    public Long getUpdateUserId() {
        return updateUserId;
    }

    public void setUpdateUserId(Long updateUserId) {
        this.updateUserId = updateUserId;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }
}
