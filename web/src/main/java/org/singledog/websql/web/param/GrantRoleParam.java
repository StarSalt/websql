package org.singledog.websql.web.param;

import java.util.List;

/**
 * Created by adam on 9/18/17.
 */
public class GrantRoleParam {

    private Long roleId;
    private List<Long> resourceIds;
    private boolean append;

    public boolean isAppend() {
        return append;
    }

    public void setAppend(boolean append) {
        this.append = append;
    }

    public Long getRoleId() {
        return roleId;
    }

    public void setRoleId(Long roleId) {
        this.roleId = roleId;
    }

    public List<Long> getResourceIds() {
        return resourceIds;
    }

    public void setResourceIds(List<Long> resourceIds) {
        this.resourceIds = resourceIds;
    }
}
