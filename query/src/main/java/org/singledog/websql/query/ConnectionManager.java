package org.singledog.websql.query;

import org.apache.tomcat.jdbc.pool.PoolProperties;
import org.singledog.websql.user.entity.DataBase;
import org.singledog.websql.user.entity.DbType;
import org.singledog.websql.user.exceptions.ServiceException;
import org.singledog.websql.user.util.AES;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by adam on 9/17/17.
 */
@Service
public class ConnectionManager {

    private static final Logger logger = LoggerFactory.getLogger(ConnectionManager.class);

    private static final Map<Long, DataSource> connCache = new ConcurrentHashMap<>();
    private static final Map<Long, JdbcTemplate> templateCache = new ConcurrentHashMap<>();

    public Connection getConnection(DataBase dataBase) throws SQLException {
        return getDataSource(dataBase).getConnection();
    }

    public JdbcTemplate getTemplate(DataBase database) {
        JdbcTemplate jdbcTemplate = templateCache.get(database.getId());
        if (jdbcTemplate == null) {
            getDataSource(database);
            jdbcTemplate = templateCache.get(database.getId());
        }
        return jdbcTemplate;
    }

    public <T> T doWithConnection(DataBase dataBase, ConnectionCallback<T> callback) {
        try (Connection connection = getConnection(dataBase)){
            return callback.doWithConn(connection);
        } catch (Exception e) {
            logger.error(e.getMessage());
            throw new ServiceException(e.getMessage());
        }
    }

    private DataSource getDataSource(DataBase database) {
        DataSource dataSource = connCache.get(database.getId());
        if (dataSource == null) {
            PoolProperties p = new PoolProperties();
            p.setUrl(database.getUrl());
            p.setDriverClassName(DbType.valueOf(database.getDbType()).getDriver());
            p.setUsername(database.getUserName());
            p.setPassword(AES.getInstance().decryptString(database.getPassword()));
            p.setJmxEnabled(true);
            p.setTestWhileIdle(false);
            p.setTestOnBorrow(true);
            p.setValidationQuery("SELECT 1");
            p.setTestOnReturn(false);
            p.setValidationInterval(30000);
            p.setTimeBetweenEvictionRunsMillis(30000);
            p.setMaxActive(10);
            p.setInitialSize(5);
            p.setMaxWait(10000);
            p.setRemoveAbandonedTimeout(60);
            p.setMinEvictableIdleTimeMillis(30000);
            p.setMinIdle(5);
            p.setLogAbandoned(true);
            p.setRemoveAbandoned(true);
            p.setJdbcInterceptors("org.apache.tomcat.jdbc.pool.interceptor.ConnectionState;"+
                    "org.apache.tomcat.jdbc.pool.interceptor.StatementFinalizer");
            org.apache.tomcat.jdbc.pool.DataSource datasource = new org.apache.tomcat.jdbc.pool.DataSource();
            datasource.setPoolProperties(p);
            connCache.put(database.getId(), datasource);
            templateCache.put(database.getId(), new JdbcTemplate(datasource));
            dataSource = datasource;
        }

        return dataSource;
    }

}
