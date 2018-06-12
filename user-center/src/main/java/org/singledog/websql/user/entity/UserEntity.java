package org.singledog.websql.user.entity;

import org.apache.ibatis.features.jpa.annotation.Column;
import org.apache.ibatis.features.jpa.annotation.Entity;
import org.apache.ibatis.features.jpa.annotation.Id;
import org.apache.ibatis.features.jpa.annotation.Table;

import java.util.Date;

/**
 * Created by Adam on 2017/9/7.
 */
@Table(name = "user_info")
@Entity
public class UserEntity extends AbstractEntity implements SoftDelete, Identified<Long> {
    /**
     * 正常状态  normal status
     */
    public static final byte STATUS_OK = 0;
    /**
     * 锁定状态  user has been locked
     */
    public static final byte STATUS_LOCKED = 1;
    /**
     * 是admin  is admin
     */
    public static final byte ADMIN_Y = 1;
    /**
     * 不是admin not admin
     */
    public static final byte ADMIN_N = 0;

    /**
     * 系统用户  system user
     */
    public static final byte USER_TYPE_SYS = 0;
    /**
     * LDAP用户 ldap user, prepare for extension
     */
    public static final byte USER_TYPE_LDAP = 1;
    /**
     * 第三方登录系统用户 other sso system user, prepare for extension
     */
    public static final byte USER_TYPE_OTHER = 2;

    @Id
    private Long id;

    @Column(name = "depart_id")
    private Long departId;

    @Column(name = "user_name")
    private String userName;

    @Column(name = "password")
    private String password;

    @Column(name = "salt")
    private String salt;

    @Column(name = "email")
    private String email;

    @Column(name = "admin")
    private Byte admin;

    @Column(name = "user_type")
    private Byte userType;

    @Column(name = "create_time")
    private Date createTime;

    @Column(name = "update_time")
    private Date updateTime;

    @Column(name = "lock_time")
    private Date lockTime;

    @Column(name = "status")
    private Byte status;

    @Column(name = "create_user_id")
    private Long createUserId;

    @Column(name = "update_user_id")
    private Long updateUserId;

    @Column(name = "del")
    private Integer delete;

    public void defaultProperty() {
        super.defaultProperty();
        this.status = STATUS_OK;
        this.delete = SoftDelete.normal;
        this.admin = ADMIN_N;
        this.userType = USER_TYPE_SYS;
    }

    public Byte getUserType() {
        return userType;
    }

    public void setUserType(Byte userType) {
        this.userType = userType;
    }

    public Byte getAdmin() {
        return admin;
    }

    public void setAdmin(Byte admin) {
        this.admin = admin;
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

    public Long getDepartId() {
        return departId;
    }

    public void setDepartId(Long departId) {
        this.departId = departId;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Long getId() {
        return id;
    }

    public void generateID() {

    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getSalt() {
        return salt;
    }

    public void setSalt(String salt) {
        this.salt = salt;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public Date getLockTime() {
        return lockTime;
    }

    public void setLockTime(Date lockTime) {
        this.lockTime = lockTime;
    }

    public Byte getStatus() {
        return status;
    }

    public void setStatus(Byte status) {
        this.status = status;
    }

    public Integer getDelete() {
        return delete;
    }

    public void setDelete(Integer delete) {
        this.delete = delete;
    }
}
