package org.singledog.websql.web.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import org.singledog.websql.query.entity.SqlCollector;

import java.util.Date;

/**
 * Created by Adam on 2017/12/8.
 */
public class CollectSqlVo {

    private Long id;
    private String title;
    private String sql;
    private Long useCount;
    @JsonFormat(shape= JsonFormat.Shape.STRING, pattern="yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    private Date createTime;
    @JsonFormat(shape= JsonFormat.Shape.STRING, pattern="yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    private Date updateTime;

    public CollectSqlVo() {}

    public CollectSqlVo(SqlCollector collector) {
        this.id = collector.getId();
        this.title = collector.getTitle();
        this.sql = collector.getSql();
        this.useCount = collector.getUseCount();
        this.createTime = collector.getCreateTime();
        this.updateTime = collector.getUpdateTime();
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

    public Long getUseCount() {
        return useCount;
    }

    public void setUseCount(Long useCount) {
        this.useCount = useCount;
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
