package org.singledog.websql.user.entity;

import java.io.Serializable;

/**
 * Created by Adam on 2017/8/21.
 *
 * Logical delete, set a delete flag
 *
 */
public interface SoftDelete extends Serializable {

    int deleted = 1;
    int normal = 0;

    Integer getDelete();

    void setDelete(Integer delete);


}
