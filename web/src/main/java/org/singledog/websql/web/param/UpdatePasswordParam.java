package org.singledog.websql.web.param;

import org.hibernate.validator.constraints.NotEmpty;

/**
 * Created by Adam on 2017/9/20.
 */
public class UpdatePasswordParam {
    @NotEmpty
    private String currentPassword;
    @NotEmpty
    private String newPassword;

    public String getCurrentPassword() {
        return currentPassword;
    }

    public void setCurrentPassword(String currentPassword) {
        this.currentPassword = currentPassword;
    }

    public String getNewPassword() {
        return newPassword;
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }
}
