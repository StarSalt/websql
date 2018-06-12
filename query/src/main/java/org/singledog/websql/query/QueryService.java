package org.singledog.websql.query;

import net.sf.jsqlparser.JSQLParserException;
import net.sf.jsqlparser.parser.CCJSqlParserUtil;
import net.sf.jsqlparser.statement.Statement;
import net.sf.jsqlparser.statement.select.*;
import net.sf.jsqlparser.util.TablesNamesFinder;
import org.singledog.websql.query.entity.QueryLog;
import org.singledog.websql.query.mapper.QueryLogMapper;
import org.singledog.websql.query.result.KV;
import org.singledog.websql.user.entity.DataBase;
import org.singledog.websql.user.entity.DbType;
import org.singledog.websql.user.entity.UserEntity;
import org.singledog.websql.user.exceptions.NoAuthorityException;
import org.singledog.websql.user.exceptions.ServiceException;
import org.singledog.websql.user.param.HttpInfoHolder;
import org.singledog.websql.user.service.DatabaseService;
import org.singledog.websql.user.service.UserRoleService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by Adam on 2017/9/20.
 */
@Service
public class QueryService {

    private static final Logger logger = LoggerFactory.getLogger(QueryService.class);

    @Autowired
    private UserRoleService userRoleService;

    @Autowired
    private ConnectionManager connectionManager;
    @Autowired
    private DatabaseService databaseService;
    @Autowired
    private QueryLogMapper queryLogMapper;

    @Transactional
    public List<List<KV>> query(Long databaseId, String sql, boolean selectedSql, Long collectId) throws JSQLParserException {
        UserEntity userEntity = HttpInfoHolder.getUserEntityNotNull();
        DataBase dataBase = databaseService.findById(databaseId);
        if (dataBase == null) {
            logger.error("No database found for id : {}", databaseId);
            throw new ServiceException("数据库不存在!");
        }

        if (!selectedSql && collectId == null) {
            sql = StringUtils.trimWhitespace(sql);
            if (StringUtils.isEmpty(sql))
                throw new ServiceException("请输入SQL!");
            String[] arr = sql.split("\n");
            for (String s : arr) {
                if (StringUtils.isEmpty(s)) {
                    continue;
                }

                s = StringUtils.trimWhitespace(s);
                if (StringUtils.isEmpty(s) || s.startsWith("--")) {
                    continue;
                }

                sql = s;
                break;
            }
        }

        List<String>[] tableNames = this.checkAuthority(userEntity, dataBase, sql);
        sql = tableNames[0].get(0);
        JdbcTemplate jdbcTemplate = connectionManager.getTemplate(dataBase);
        if (jdbcTemplate == null) {
            throw new ServiceException("未找到JdbcTemplate!");
        }

        long start = System.currentTimeMillis();
        List<List<KV>> list = jdbcTemplate.query(sql, new RowMapper<List<KV>>() {
            @Override
            public List<KV> mapRow(ResultSet rs, int rowNum) throws SQLException {
                int columnNum = rs.getMetaData().getColumnCount();
                List<KV> list = new ArrayList<KV>(columnNum);
                for (int i = 0; i < columnNum; i++) {
                    String columnName = rs.getMetaData().getColumnLabel(i + 1);
                    String value = String.valueOf(rs.getObject(i + 1));
                    list.add(new KV(columnName, value));
                }

                return list;
            }
        });

        logger.info("query success for sql: {}", sql);
        QueryLog log = new QueryLog();
        log.defaultProperty();
        log.setUserId(userEntity.getId());
        log.setCostTime(System.currentTimeMillis() - start);
        log.setDbId(databaseId);
        log.setDbName(dataBase.getDbName());
        log.setSql(sql);
        log.setCollectId(collectId);
        log.setTableName(Arrays.toString(tableNames[1].toArray(new String[tableNames[1].size()])));
        log.setUserName(userEntity.getUserName());
        log.setUserEmail(userEntity.getEmail());
        queryLogMapper.saveAutoIncrementKey(log);

        return list;
    }

    private List<String>[] checkAuthority(UserEntity userEntity, DataBase dataBase, String sql) throws JSQLParserException {
        Statement statement = CCJSqlParserUtil.parse(sql);
        if (!(statement instanceof Select)) {
            logger.error("Update Operation Not Allowed !");
            throw new NoAuthorityException("没有修改数据的权限! ");
        }

        List<String> authorized = userRoleService.listTableNames(userEntity.getId(), dataBase.getId());
        Set<String> authorizedSet = new HashSet<>(authorized.stream().map(String::toLowerCase).collect(Collectors.toList()));

        Select select = (Select) statement;

        TablesNamesFinder namesFinder = new TablesNamesFinder();
        List<String> tableNames = namesFinder.getTableList(select);
        if (!CollectionUtils.isEmpty(tableNames)) {
            for (String tableName : tableNames) {
                if (!userRoleService.hasAuthority(authorizedSet, dataBase.getId(), tableName.toLowerCase())) {
                    logger.error("No authority to access table : {}, database: {}", tableName, dataBase.getId());
                    throw new NoAuthorityException("No access rights for table "
                            + tableName + ", database: " + dataBase.getId());
                }
            }
        }

        SelectBody selectBody = select.getSelectBody();
        if (selectBody instanceof PlainSelect) {
            PlainSelect body = (PlainSelect) selectBody;
            if (DbType.MYSQL.name().equals(dataBase.getDbType())) {
                Limit limit = body.getLimit();
                if (limit == null ) {
                    limit = new Limit();
                    limit.setRowCount(1000);
                    body.setLimit(limit);
                } else if (limit.getRowCount() > 1000) {
                    limit.setRowCount(1000);
                    body.setLimit(limit);
                }
            } else if (DbType.SQL_SERVER.name().equals(dataBase.getDbType())) {
                Top top = body.getTop();
                if (top == null ) {
                    top = new Top();
                    top.setRowCount(1000);
                    body.setTop(top);
                } else if (top.getRowCount() > 1000) {
                    top.setRowCount(1000);
                    body.setTop(top);
                }
            }
        }

        return new List[]{Collections.singletonList(select.toString()), tableNames};
    }

}
