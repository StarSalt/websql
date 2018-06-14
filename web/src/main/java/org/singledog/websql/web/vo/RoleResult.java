package org.singledog.websql.web.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import org.singledog.websql.user.entity.Role;
import org.singledog.websql.user.service.UserNameRequired;
import org.springframework.beans.BeanUtils;

import java.util.Date;

/**
 * Created by adam on 9/17/17.
 */
public class RoleResult implements UserNameRequired {

    private Long id;

    private Long departId;

    private String roleName;

    private Integer roleType;

    @JsonFormat(shape= JsonFormat.Shape.STRING, pattern="yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    private Date createTime;

    @JsonFormat(shape= JsonFormat.Shape.STRING, pattern="yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    private Date updateTime;

    private Long createUserId;

    private Long updateUserId;

    private Integer delete;

    private String roleTypeName;

    private String updateUserName;
    private String createUserName;

    private boolean checked;

    public RoleResult() {
    }

    public RoleResult(Role role) {
        BeanUtils.copyProperties(role, this);
        if (this.roleType != null) {
            this.roleTypeName = this.roleType == Role.ROLE_TYPE_ANONYMOUS ? "系统角色" : "普通角色";
        }
    }

    public boolean isChecked() {
        return checked;
    }

    public void setChecked(boolean checked) {
        this.checked = checked;
    }

    @Override
    public String[][] idNameFieldMapping() {
        return new String[][]{{"createUserId","createUserName"},{"updateUserId","updateUserName"}};
    }

    public String getUpdateUserName() {
        return updateUserName;
    }

    public void setUpdateUserName(String updateUserName) {
        this.updateUserName = updateUserName;
    }

    public String getCreateUserName() {
        return createUserName;
    }

    public void setCreateUserName(String createUserName) {
        this.createUserName = createUserName;
    }

    public String getRoleTypeName() {
        return roleTypeName;
    }

    public void setRoleTypeName(String roleTypeName) {
        this.roleTypeName = roleTypeName;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getDepartId() {
        return departId;
    }

    public void setDepartId(Long departId) {
        this.departId = departId;
    }

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    public Integer getRoleType() {
        return roleType;
    }

    public void setRoleType(Integer roleType) {
        this.roleType = roleType;
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

    public Integer getDelete() {
        return delete;
    }

    public void setDelete(Integer delete) {
        this.delete = delete;
    }
}
