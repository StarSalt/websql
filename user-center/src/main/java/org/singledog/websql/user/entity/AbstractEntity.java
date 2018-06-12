package org.singledog.websql.user.entity;

import org.singledog.websql.user.param.HttpInfoHolder;

import java.util.Date;

/**
 * Created by Adam on 2017/9/8.
 */
public abstract class AbstractEntity implements DefaultPropertySupport {

    public AbstractEntity() {

    }

    public abstract void setUpdateUserId(Long updateUserId);

    public abstract void setCreateUserId(Long createUserId);

    public abstract void setCreateTime(Date createTime);

    public abstract void setUpdateTime(Date updateTme);

    @Override
    public void defaultProperty() {
        Date d = new Date();
        this.setCreateTime(d);
        this.setUpdateTime(d);
        UserEntity userEntity = HttpInfoHolder.getUserEntity();
        if (userEntity != null) {
            this.setUpdateUserId(userEntity.getId());
            this.setCreateUserId(userEntity.getId());
        }
    }
}
