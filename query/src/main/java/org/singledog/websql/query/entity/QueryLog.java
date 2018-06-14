package org.singledog.websql.query.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import org.apache.ibatis.features.jpa.annotation.Column;
import org.apache.ibatis.features.jpa.annotation.Entity;
import org.apache.ibatis.features.jpa.annotation.Id;
import org.apache.ibatis.features.jpa.annotation.Table;
import org.singledog.websql.user.entity.DefaultPropertySupport;
import sun.util.resources.cldr.ga.LocaleNames_ga;

import java.util.Date;

/**
 * Created by Adam on 2017/9/20.
 */
@Entity
@Table(name = "query_log")
public class QueryLog implements DefaultPropertySupport {

    @Id
    private Long id;
    @Column(name = "user_id")
    private Long userId;
    @Column(name = "user_name")
    private String userName;
    @Column(name = "user_email")
    private String userEmail;
    @Column(name = "db_id")
    private Long dbId;
    @Column(name = "db_name")
    private String dbName;
    @Column(name = "table_name")
    private String tableName;
    @Column(name = "collect_id")
    private Long collectId;
    @Column(name = "sql_text")
    private String sql;
    @Column(name = "cost_time")
    private Long costTime;
    @JsonFormat(shape= JsonFormat.Shape.STRING, pattern="yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    @Column(name = "create_time")
    private Date createTime;

    @Override
    public void defaultProperty() {
        this.createTime = new Date();
    }

    public Long getCostTime() {
        return costTime;
    }

    public void setCostTime(Long costTime) {
        this.costTime = costTime;
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

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getDbId() {
        return dbId;
    }

    public void setDbId(Long dbId) {
        this.dbId = dbId;
    }

    public String getDbName() {
        return dbName;
    }

    public void setDbName(String dbName) {
        this.dbName = dbName;
    }

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public String getSql() {
        return sql;
    }

    public void setSql(String sql) {
        this.sql = sql;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Long getCollectId() {
        return collectId;
    }

    public void setCollectId(Long collectId) {
        this.collectId = collectId;
    }
}
