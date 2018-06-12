package org.singledog.websql.query.sync;

import com.alibaba.fastjson.JSON;
import org.singledog.websql.query.ConnectionManager;
import org.singledog.websql.user.entity.DataBase;
import org.singledog.websql.user.entity.DbResource;
import org.singledog.websql.user.entity.SoftDelete;
import org.singledog.websql.user.event.EventType;
import org.singledog.websql.user.event.LoggerEventBuilder;
import org.singledog.websql.user.exceptions.ServiceException;
import org.singledog.websql.user.mapper.DatabaseMapper;
import org.singledog.websql.user.param.HttpInfoHolder;
import org.singledog.websql.user.service.DatabaseService;
import org.singledog.websql.user.service.DbResourceService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.DatabaseMetaData;
import java.sql.ResultSet;

/**
 * Created by adam on 9/17/17.
 */
@Service
public class DbSynchronizer {

    private static final Logger logger = LoggerFactory.getLogger(DbSynchronizer.class);

    @Autowired
    private DatabaseMapper databaseMapper;
    @Autowired
    private ConnectionManager connectionManager;
    @Autowired
    private DbResourceService dbResourceService;
    @Autowired
    private ApplicationContext applicationContext;

    @Transactional
    public int sync(Long dbId) {
        DataBase dataBase = databaseMapper.findByIdAndDelete(dbId, SoftDelete.normal);
        if (dataBase == null)
            throw new ServiceException("数据库不存在!");

        applicationContext.publishEvent(LoggerEventBuilder
                .getInstance(DatabaseService.class)
                .eventType(EventType.SYNC_RESOURCE)
                .userEntity(HttpInfoHolder.getUserEntity())
                .params(JSON.toJSONString("dbid: " + dbId))
                .remark("dbid: ".concat(String.valueOf(dbId)))
                .build());

        connectionManager.doWithConnection(dataBase, conn -> {
            DatabaseMetaData metaData = conn.getMetaData();
            ResultSet tableRs = metaData.getTables(null,"%","%", new String[]{"TABLE", "VIEW"});
            while (tableRs.next()) {
                String tableName = tableRs.getString("TABLE_NAME");
                if ("VIEW".equals(tableRs.getString("TABLE_TYPE"))) {
                    dbResourceService.mergeResource(dbId, tableName, DbResource.IS_VIEW_Y);
                } else {
                    dbResourceService.mergeResource(dbId, tableName, DbResource.IS_VIEW_N);
                }
            }
            return null;
        });

        return 0;
    }

}
