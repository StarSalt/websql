package org.singledog.websql.user.entity;

import org.apache.ibatis.features.jpa.annotation.Column;
import org.apache.ibatis.features.jpa.annotation.Entity;
import org.apache.ibatis.features.jpa.annotation.Id;
import org.apache.ibatis.features.jpa.annotation.Table;

import java.util.Date;

/**
 * Created by Adam on 2017/9/7.
 */
@Entity
@Table(name = "data_base")
public class DataBase extends AbstractEntity implements SoftDelete {

    @Id
    private Long id;

    @Column(name = "db_name")
    private String dbName;

    @Column(name = "url")
    private String url;

    @Column(name = "user_name")
    private String userName;

    @Column(name = "password")
    private String password;

    @Column(name = "db_type")
    private String dbType;

    @Column(name = "create_user_id")
    private Long createUserId;

    @Column(name = "update_user_id")
    private Long updateUserId;

    @Column(name = "create_time")
    private Date createTime;

    @Column(name = "update_time")
    private Date updateTime;

    @Column(name = "del")
    private Integer delete;

    @Override
    public void defaultProperty() {
        super.defaultProperty();
        this.delete = SoftDelete.normal;
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

    public Long getCreateUserId() {
        return createUserId;
    }

    public void setCreateUserId(Long createUserId) {
        this.createUserId = createUserId;
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

    @Override
    public Integer getDelete() {
        return delete;
    }

    @Override
    public void setDelete(Integer delete) {
        this.delete = delete;
    }
}
