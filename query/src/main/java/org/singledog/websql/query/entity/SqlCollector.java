package org.singledog.websql.query.entity;

import org.apache.ibatis.features.jpa.annotation.Column;
import org.apache.ibatis.features.jpa.annotation.Entity;
import org.apache.ibatis.features.jpa.annotation.Id;
import org.apache.ibatis.features.jpa.annotation.Table;
import org.singledog.websql.user.entity.AbstractEntity;
import org.singledog.websql.user.entity.DefaultPropertySupport;
import org.singledog.websql.user.entity.UserEntity;
import org.singledog.websql.user.param.HttpInfoHolder;

import java.util.Date;

/**
 * Created by Adam on 2017/12/6.
 */

@Entity
@Table(name = "sql_collector")
public class SqlCollector implements DefaultPropertySupport {

    @Id
    private Long id;
    @Column
    private String title;
    @Column(name = "sql_text")
    private String sql;
    @Column(name = "user_id")
    private Long userId;
    @Column(name = "user_name")
    private String userName;
    @Column(name = "use_count")
    private Long useCount;
    @Column(name = "create_time")
    private Date createTime;
    @Column(name = "update_time")
    private Date updateTime;

    @Override
    public void defaultProperty() {
        UserEntity userEntity = HttpInfoHolder.getUserEntity();
        if (userEntity != null) {
            this.setUserId(userEntity.getId());
            this.setUserName(userEntity.getUserName());
        }
        this.setCreateTime(new Date());
        this.setUpdateTime(this.getCreateTime());
        this.setUseCount(0L);
    }

    public Long getUseCount() {
        return useCount;
    }

    public void setUseCount(Long useCount) {
        this.useCount = useCount;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSql() {
        return sql;
    }

    public void setSql(String sql) {
        this.sql = sql;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
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
