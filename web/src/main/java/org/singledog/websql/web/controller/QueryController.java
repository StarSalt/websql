package org.singledog.websql.web.controller;

import org.apache.ibatis.features.jpa.domain.Page;
import org.singledog.websql.query.CollectorService;
import org.singledog.websql.query.QueryService;
import org.singledog.websql.query.entity.SqlCollector;
import org.singledog.websql.query.result.KV;
import org.singledog.websql.user.entity.*;
import org.singledog.websql.user.param.HttpInfoHolder;
import org.singledog.websql.user.service.DatabaseService;
import org.singledog.websql.user.service.RoleService;
import org.singledog.websql.user.service.UserRoleService;
import org.singledog.websql.web.param.CollectParam;
import org.singledog.websql.web.param.QueryParam;
import org.singledog.websql.web.result.CommonResult;
import org.singledog.websql.web.result.ListResult;
import org.singledog.websql.web.result.ResultCode;
import org.singledog.websql.web.vo.CollectSqlVo;
import org.singledog.websql.web.vo.ComboBoxVo;
import org.singledog.websql.web.vo.TableResourceVo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.web.querydsl.QuerydslPredicateArgumentResolver;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by Adam on 2017/9/14.
 */
@RequestMapping("/query")
@RestController
public class QueryController {

    private static final Logger logger = LoggerFactory.getLogger(QueryController.class);

    @Autowired
    private UserRoleService userRoleService;
    @Autowired
    private RoleService roleService;
    @Autowired
    private DatabaseService databaseService;
    @Autowired
    private QueryService queryService;
    @Autowired
    private CollectorService collectorService;

    @RequestMapping("/allDbTypes")
    public List<ComboBoxVo> allDbTypes() {
        List<ComboBoxVo> list = new ArrayList<>();
        for (DbType type : DbType.values()) {
            list.add(new ComboBoxVo(type.name(), type.name()));
        }
        return list;
    }

    @RequestMapping("/allDbs")
    public List<ComboBoxVo> authedDatabases(HttpServletRequest request) {
        UserEntity userEntity = HttpInfoHolder.getUserEntityNotNull();
        logger.info("search authorized database for user : {}", userEntity.getId());
        List<RoleResource> roleResources = userRoleService.listRoleResources(userEntity.getId(), null);
        Set<Long> databaseIds = roleResources.stream().map(RoleResource::getDatabaseId).collect(Collectors.toSet());
        if (CollectionUtils.isEmpty(databaseIds))
            return Collections.emptyList();

        List<DataBase> dataBases = databaseService.findByIds(new ArrayList<>(databaseIds));
        List<ComboBoxVo> vos = new ArrayList<>(dataBases.size());
        for (DataBase db : dataBases) {
            vos.add(new ComboBoxVo(String.valueOf(db.getId()), db.getDbName()));
        }

        return vos;
    }

    @RequestMapping("/tables/{id}")
    public CommonResult<List<String>> getTables(@PathVariable("id") Long databaseId) {
        UserEntity entity = HttpInfoHolder.getUserEntityNotNull();
        List<RoleResource> roleResources = userRoleService.listRoleResources(entity.getId(), databaseId);
        List<String> tableNames = new ArrayList<>(roleResources.size());
        for (RoleResource roleResource : roleResources) {
            String tableName = roleResource.getTableName();
            if (tableNames.contains(tableName))
                continue;
            tableNames.add(tableName);
        }

        return new CommonResult<List<String>>().fillResult(ResultCode.SUCCESS).setData(tableNames);
    }

    @RequestMapping(value = "/exec", method = RequestMethod.POST)
    public CommonResult<List> exec(@RequestBody @Valid QueryParam param) {
        try {
            List<List<KV>> result = queryService.query(param.getDid(), param.getSql(),
                    Objects.equals(1, param.getSel()), param.getCid());
            if (param.getCid() != null) {
                increaseUseCount(param.getCid());
            }
            return CommonResult.newInstance(ResultCode.SUCCESS, List.class).setData(result);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return CommonResult.newInstance(ResultCode.SERVER_ERROR, List.class).setMessage(e.getMessage());
        }
    }

    @RequestMapping(value = "/collect", method = RequestMethod.POST)
    public CommonResult<String> collect(@RequestBody @Valid CollectParam collectParam) {
        collectorService.collectSql(collectParam.getTitle(), collectParam.getSql());
        return CommonResult.newInstance(ResultCode.SUCCESS, String.class);
    }

    @RequestMapping("/listCollect")
    public ListResult<CollectSqlVo> listCollect(Integer rows, Integer page, String q) {
        Page<SqlCollector> pageList = collectorService.list(HttpInfoHolder.getUserEntityNotNull().getId(),
                q, page, rows);
        return new ListResult<CollectSqlVo>(pageList).setRows(pageList
                .getContent()
                .stream()
                .map(CollectSqlVo::new)
                .collect(Collectors.toList()));
    }

    @RequestMapping(value = "/incrUseCount", method = RequestMethod.POST)
    public CommonResult<String> increaseUseCount(Long id) {
        collectorService.increaseUseCount(id, HttpInfoHolder.getUserEntityNotNull().getId());
        return CommonResult.newInstance(ResultCode.SUCCESS, String.class);
    }

}
