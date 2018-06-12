package org.singledog.websql.user.event;

/**
 * Created by Adam on 2017/9/12.
 */
public enum EventType {
    ADD_DEPART("添加部门"),
    UPDATE_DEPART("更新部门"),
    DELETE_DEPART("删除部门"),
    ADD_DATABASE("添加数据库"),
    DELETE_DATABASE("删除数据库"),
    UPDATE_DATABASE("更新数据库"),
    UPDATE_TABLE_DEL_STATE("更新表删除状态"),
    SYNC_RESOURCE("同步数据库表信息"),
    CREATE_USER("创建用户"),
    BLOCK_UNBLOCK_USER("LOCK/UNLOCK 用户"),
    DELETE_USER("删除用户"),
    QUERY_USER_DETAIL("查看用户详情"),
    UPDATE_USER("更新用户"),
    CREATE_ROLE("创建角色"),
    CREATE_ANONYMOUS_ROLE("创建匿名角色"),
    DELETE_ROLE("删除角色"),
    UPDATE_ROLE("更新角色"),
    GRANT_RESOURCE_TO_ROLE("赋予角色资源"),
    MERGE_RESOURCE_TO_ROLE("添加角色资源"),
    GRANT_ROLE_TO_USER("赋予用户角色"),
    REVOKE_ROLE_FROM_USER("回收用户角色"),
    COLLECT_SQL("收藏SQL"),
    QUERY_SQL("查询SQL"),
    LOGIN("登录"),
    LOGOUT("退出登录"),
    UPDATE_PASSWORD("修改密码"),
    ;

    private String desc;

    private EventType(String desc) {
        this.desc = desc;
    }

    public String getDesc() {
        return desc;
    }
}
