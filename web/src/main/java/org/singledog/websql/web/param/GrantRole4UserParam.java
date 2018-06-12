package org.singledog.websql.web.param;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * Created by Adam on 2017/9/20.
 */
public class GrantRole4UserParam {

    @NotNull
    private Long userId;

    private List<Long> roleIds;

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public List<Long> getRoleIds() {
        return roleIds;
    }

    public void setRoleIds(List<Long> roleIds) {
        this.roleIds = roleIds;
    }
}
