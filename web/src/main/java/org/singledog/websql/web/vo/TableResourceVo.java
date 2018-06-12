package org.singledog.websql.web.vo;

import org.singledog.websql.user.entity.DbResource;
import org.singledog.websql.user.entity.SoftDelete;
import org.springframework.beans.BeanUtils;

import java.util.Date;

/**
 * Created by adam on 9/17/17.
 */
public class TableResourceVo extends DbResource {

    private Long id;

    private Long databaseId;

    private String tableName;

    private Integer isView;

    private String isViewStr;

    private Date createTime;

    private Date updateTime;

    private Integer delete;

    private String dbType;

    private String delStatus;

    private boolean checked;

    public boolean isChecked() {
        return checked;
    }

    public void setChecked(boolean checked) {
        this.checked = checked;
    }

    public TableResourceVo() {
    }

    public TableResourceVo(DbResource tableResourceVo) {
        BeanUtils.copyProperties(tableResourceVo, this);
        if (delete != null) {
            this.delStatus = delete == SoftDelete.deleted ? "已删除" : "未删除";
        }

        if (this.isView != null) {
            this.isViewStr = isView == IS_VIEW_Y ? "视图" : "表";
        }
    }

    public String getIsViewStr() {
        return isViewStr;
    }

    public void setIsViewStr(String isViewStr) {
        this.isViewStr = isViewStr;
    }

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public void setId(Long id) {
        this.id = id;
    }

    @Override
    public Long getDatabaseId() {
        return databaseId;
    }

    @Override
    public void setDatabaseId(Long databaseId) {
        this.databaseId = databaseId;
    }

    @Override
    public String getTableName() {
        return tableName;
    }

    @Override
    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    @Override
    public Integer getIsView() {
        return isView;
    }

    @Override
    public void setIsView(Integer isView) {
        this.isView = isView;
    }

    @Override
    public Date getCreateTime() {
        return createTime;
    }

    @Override
    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    @Override
    public Date getUpdateTime() {
        return updateTime;
    }

    @Override
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

    public String getDbType() {
        return dbType;
    }

    public void setDbType(String dbType) {
        this.dbType = dbType;
    }

    public String getDelStatus() {
        return delStatus;
    }

    public void setDelStatus(String delStatus) {
        this.delStatus = delStatus;
    }
}
