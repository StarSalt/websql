package org.singledog.websql.user.service;

import com.alibaba.fastjson.JSON;
import org.apache.ibatis.features.jpa.domain.Page;
import org.apache.ibatis.features.jpa.domain.PageRequest;
import org.apache.ibatis.features.jpa.domain.Sort;
import org.apache.ibatis.utils.CollectionUtils;
import org.singledog.websql.user.entity.DataBase;
import org.singledog.websql.user.entity.DbResource;
import org.singledog.websql.user.entity.SoftDelete;
import org.singledog.websql.user.entity.UserEntity;
import org.singledog.websql.user.event.EventType;
import org.singledog.websql.user.event.LoggerEventBuilder;
import org.singledog.websql.user.exceptions.NotLoginException;
import org.singledog.websql.user.exceptions.ServiceException;
import org.singledog.websql.user.mapper.DatabaseMapper;
import org.singledog.websql.user.mapper.DbResourceMapper;
import org.singledog.websql.user.param.DatabaseParam;
import org.singledog.websql.user.param.DatabaseParam4Update;
import org.singledog.websql.user.param.HttpInfoHolder;
import org.singledog.websql.user.util.AES;
import org.singledog.websql.user.util.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.Collections;
import java.util.Date;
import java.util.List;

/**
 * Created by Adam on 2017/9/8.
 */
@Service
public class DatabaseService {
    private static final Logger logger = LoggerFactory.getLogger(DatabaseService.class);

    @Autowired
    private ApplicationContext applicationContext;
    @Autowired
    private DatabaseMapper databaseMapper;
    @Autowired
    private DbResourceMapper dbResourceMapper;

    /**
     * add a database connection
     * @param databaseParam
     * @return
     */
    @Transactional
    public DataBase addDatabase(DatabaseParam databaseParam) {
        DataBase dataBase = new DataBase();
        dataBase.defaultProperty();
        dataBase.setDbName(databaseParam.getDbName());
        dataBase.setDbType(databaseParam.getDbType());
        dataBase.setUserName(databaseParam.getUserName());
        dataBase.setUrl(databaseParam.getUrl());
        dataBase.setPassword(AES.getInstance().encryptString(databaseParam.getPassword()));
        if (dataBase.getCreateUserId() == null)
            throw new NotLoginException();
        try {
            this.databaseMapper.saveAutoIncrementKey(dataBase);
        } catch (DuplicateKeyException e) {
            logger.error(e.getMessage(), e);
            throw  new ServiceException("数据重复!");
        }
        databaseParam.setPassword("");
        applicationContext.publishEvent(LoggerEventBuilder
                .getInstance(DatabaseService.class)
                .eventType(EventType.ADD_DATABASE)
                .userEntity(HttpInfoHolder.getUserEntity())
                .params(JSON.toJSONString(databaseParam))
                .remark("database id: ".concat(String.valueOf(dataBase.getId())))
                .build());
        return dataBase;
    }
    /**
     * add a database connection
     * @param databaseParam
     * @return
     */
    @Transactional
    public DataBase updateDatabase(DatabaseParam4Update databaseParam) {
        UserEntity userEntity = HttpInfoHolder.getAdmin();
        DataBase dataBase = databaseMapper.findById(databaseParam.getId());
        if (!StringUtils.isEmpty(databaseParam.getDbName()))
            dataBase.setDbName(databaseParam.getDbName());
        if (!StringUtils.isEmpty(databaseParam.getDbType()))
            dataBase.setDbType(databaseParam.getDbType());
        if (!StringUtils.isEmpty(databaseParam.getUserName()))
            dataBase.setUserName(databaseParam.getUserName());
        if (!StringUtils.isEmpty(databaseParam.getUrl()))
            dataBase.setUrl(databaseParam.getUrl());
        if (!StringUtils.isEmpty(databaseParam.getPassword()))
            dataBase.setPassword(AES.getInstance().encryptString(databaseParam.getPassword()));
        dataBase.setUpdateUserId(userEntity.getId());
        dataBase.setUpdateTime(new Date());
        if (dataBase.getUpdateUserId() == null)
            throw new NotLoginException();
        try {
            this.databaseMapper.updateByPrimaryKeySelective(dataBase);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            throw  new ServiceException("数据重复!");
        }
        applicationContext.publishEvent(LoggerEventBuilder
                .getInstance(DatabaseService.class)
                .eventType(EventType.UPDATE_DATABASE)
                .userEntity(HttpInfoHolder.getUserEntity())
                .params(JSON.toJSONString(databaseParam))
                .remark("database id: ".concat(String.valueOf(dataBase.getId())))
                .build());
        return dataBase;
    }

    /**
     * get all database info
     * @return
     */
    public List<DataBase> getAllDatabases() {
        return databaseMapper.findByDelete(SoftDelete.normal, new Sort(Sort.Direction.DESC, "createTime"));
    }

    /**
     * get all database info
     * @return
     */
    public Page<DataBase> searchDatabases(String name, String url, String type, Integer pageNum, Integer pageSize) {
        if (!StringUtils.isEmpty(name))
            name = StringUtil.wrapLike(name);
        if (!StringUtils.isEmpty(url))
            url = StringUtil.wrapLike(url);

        return databaseMapper.searchDatabase(name, url, type,
                PageRequest.of(pageNum, pageSize, new Sort(Sort.Direction.DESC, "createTime")));
    }

    /**
     * get all database resources
     * @return
     */
    public List<DbResource> getResources(Long databaseId) {
        return dbResourceMapper.findByDatabaseIdAndDelete(databaseId, SoftDelete.normal, new Sort(Sort.Direction.ASC, "tableName"));
    }

    /**
     * save or update resource
     *
     * @param dbResource
     * @return
     */
    @Transactional
    public DbResource mergeResource(DbResource dbResource) {
        DbResource exists = dbResourceMapper.findByDatabaseIdAndTableName(dbResource.getDatabaseId(), dbResource.getTableName());
        if (exists != null) {
            logger.info("resource already exists ! update ... ");
            if (exists.getDelete() == null || exists.getDelete() == SoftDelete.deleted) {
                logger.info("change delete flag !");
                exists.setDelete(SoftDelete.normal);
            }
            exists.setUpdateTime(new Date());
            this.dbResourceMapper.updateByPrimaryKey(exists);
            return exists;
        } else {
            logger.info("resource not exists ! insert ... ");
            this.dbResourceMapper.saveAutoIncrementKey(dbResource);
            return dbResource;
        }
    }

    public int deleteById(Long id) {
        int i = this.databaseMapper.softDeleteById(id);
        applicationContext.publishEvent(LoggerEventBuilder
                .getInstance(DatabaseService.class)
                .eventType(EventType.DELETE_DATABASE)
                .userEntity(HttpInfoHolder.getUserEntity())
                .params(JSON.toJSONString(id))
                .remark("database id: ".concat(String.valueOf(id)))
                .build());
        return i;
    }

    /**
     * find database info by id list
     * @param idList
     * @return
     */
    public List<DataBase> findByIds(List<Long> idList) {
        if (CollectionUtils.isEmpty(idList))
            return Collections.emptyList();

        return databaseMapper.findByIdInAndDelete(idList, SoftDelete.normal);
    }

    public DataBase findById(Long id) {
        return databaseMapper.findByIdAndDelete(id, SoftDelete.normal);
    }
}
