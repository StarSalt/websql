package org.singledog.websql.user.service;

import com.alibaba.fastjson.JSON;
import org.apache.ibatis.features.jpa.domain.Page;
import org.apache.ibatis.features.jpa.domain.PageRequest;
import org.apache.ibatis.features.jpa.domain.Sort;
import org.singledog.websql.user.entity.DataBase;
import org.singledog.websql.user.entity.DbResource;
import org.singledog.websql.user.entity.SoftDelete;
import org.singledog.websql.user.event.EventType;
import org.singledog.websql.user.event.LoggerEventBuilder;
import org.singledog.websql.user.exceptions.ServiceException;
import org.singledog.websql.user.mapper.DatabaseMapper;
import org.singledog.websql.user.mapper.DbResourceMapper;
import org.singledog.websql.user.param.HttpInfoHolder;
import org.singledog.websql.user.util.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.List;

/**
 * Created by adam on 9/17/17.
 */
@Service
public class DbResourceService {

    private static final Logger logger = LoggerFactory.getLogger(DbResourceService.class);

    @Autowired
    private DbResourceMapper dbResourceMapper;
    @Autowired
    private DatabaseMapper databaseMapper;
    @Autowired
    private ApplicationContext applicationContext;

    public Page<DbResource> searchResource(Long dbId, String name, Integer isView, Integer page, Integer pageSize) {
        if (!StringUtils.isEmpty(name)) {
            name = StringUtil.wrapLike(name);
        }

        if (dbId == null)
            throw new ServiceException("请选择数据库");

        return dbResourceMapper.searchDbResource(dbId, name, isView, PageRequest.of(page, pageSize,
                new Sort(Sort.Direction.ASC, "table_name")));
    }

    public List<DbResource> searchResourceNoPage(Long dbId, String name, Integer isView) {
        if (!StringUtils.isEmpty(name)) {
            name = StringUtil.wrapLike(name);
        }

        if (dbId == null)
            throw new ServiceException("请选择数据库");

        return dbResourceMapper.searchDbResourceNoPage(dbId, name, isView);
    }

    @Transactional
    public int mergeResource(Long databaseId, String tableName, Integer isview) {
        DbResource dbResource = dbResourceMapper.findByDatabaseIdAndTableName(databaseId, tableName);
        if (dbResource != null) {
            return 0;
        }

        dbResource = new DbResource();
        dbResource.defaultProperty();
        dbResource.setDatabaseId(databaseId);
        dbResource.setTableName(tableName);
        dbResource.setIsView(isview);
        this.dbResourceMapper.saveAutoIncrementKey(dbResource);
        return 1;
    }

    @Transactional
    public int updateDelete(Long id, Integer flag) {
        HttpInfoHolder.getAdmin();
        int i = dbResourceMapper.updateDelete(id, flag);
        applicationContext.publishEvent(LoggerEventBuilder
                .getInstance(DatabaseService.class)
                .eventType(EventType.UPDATE_TABLE_DEL_STATE)
                .userEntity(HttpInfoHolder.getUserEntity())
                .params(JSON.toJSONString("id: " + id + " -- flag: " + flag))
                .remark("table id: ".concat(String.valueOf(id)))
                .build());

        return i;
    }

}
