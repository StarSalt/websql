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
@Table(name = "db_resource")
public class DbResource implements DefaultPropertySupport, SoftDelete{

    public static final int IS_VIEW_Y = 1;
    public static final int IS_VIEW_N = 0;

    @Id
    private Long id;

    @Column(name = "database_id")
    private Long databaseId;

    @Column(name = "table_name")
    private String tableName;

    @Column(name = "is_view")
    private Integer isView;

    @Column(name = "create_time")
    private Date createTime;

    @Column(name = "update_time")
    private Date updateTime;

    @Column(name = "del")
    private Integer delete;

    @Override
    public void defaultProperty() {
        this.createTime = new Date();
        this.isView = IS_VIEW_N;
        this.updateTime = this.createTime;
        this.delete = SoftDelete.normal;
    }

    @Override
    public Integer getDelete() {
        return delete;
    }

    @Override
    public void setDelete(Integer delete) {
        this.delete = delete;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getDatabaseId() {
        return databaseId;
    }

    public void setDatabaseId(Long databaseId) {
        this.databaseId = databaseId;
    }

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
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

    public Integer getIsView() {
        return isView;
    }

    public void setIsView(Integer isView) {
        this.isView = isView;
    }
}
