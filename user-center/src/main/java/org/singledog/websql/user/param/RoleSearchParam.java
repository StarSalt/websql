package org.singledog.websql.user.param;

/**
 * Created by Adam on 2017/9/8.
 */
public class RoleSearchParam extends PagingParam {

    private Long departId;
    private String roleName;
    private Integer delete;
    private Integer roleType;

    public Integer getDelete() {
        return delete;
    }

    public void setDelete(Integer delete) {
        this.delete = delete;
    }

    public Integer getRoleType() {
        return roleType;
    }

    public void setRoleType(Integer roleType) {
        this.roleType = roleType;
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
}
