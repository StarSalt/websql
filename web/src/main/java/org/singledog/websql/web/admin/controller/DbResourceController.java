package org.singledog.websql.web.admin.controller;

import org.apache.ibatis.features.jpa.domain.Page;
import org.singledog.websql.query.sync.DbSynchronizer;
import org.singledog.websql.user.entity.*;
import org.singledog.websql.user.mapper.DatabaseMapper;
import org.singledog.websql.user.service.DatabaseService;
import org.singledog.websql.user.service.DbResourceService;
import org.singledog.websql.user.service.RoleService;
import org.singledog.websql.user.util.StringUtil;
import org.singledog.websql.web.result.CommonResult;
import org.singledog.websql.web.result.ListResult;
import org.singledog.websql.web.result.ResultCode;
import org.singledog.websql.web.vo.RoleResult;
import org.singledog.websql.web.vo.TableResourceVo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by adam on 9/17/17.
 */
@RequestMapping("/admin/dbresource")
@RestController
public class DbResourceController {

    private static final Logger logger = LoggerFactory.getLogger(DbResourceController.class);

    @Autowired
    private DbResourceService dbResourceService;
    @Autowired
    private DatabaseMapper databaseMapper;
    @Autowired
    private DbSynchronizer dbSynchronizer;
    @Autowired
    private RoleService roleService;

    @RequestMapping("/list")
    public ListResult<TableResourceVo> searchDbResource(Long dbId, String name, Integer isView, Integer rows, Integer page) {
        if (dbId == null)
            return new ListResult<>();


        if (page == null)
            page = 1;
        if (rows == null)
            rows = 20;

        DataBase dataBase = databaseMapper.findByIdAndDelete(dbId, SoftDelete.normal);
        if (dataBase == null) {
            return new ListResult<>();
        }

        Page<DbResource> dbResourcesPage = dbResourceService.searchResource(dbId, StringUtil.emptyToNull(name), isView, page - 1, rows);
        ListResult<TableResourceVo> voListResult = new ListResult<>(dbResourcesPage);
        List<TableResourceVo> voList = dbResourcesPage.getContent()
                .stream()
                .map(t -> {
                    TableResourceVo vo = new TableResourceVo(t);
                    vo.setDbType(dataBase.getDbType());
                    return vo;
                }).collect(Collectors.toList());

        voListResult.setRows(voList);
        return voListResult;
    }

    @RequestMapping("/listnopage")
    public ListResult<TableResourceVo> searchDbResourceNoPage(Long dbId, String name, Integer isView, Long roleId, Integer rows, Integer page) {
        if (dbId == null)
            return new ListResult<>();

        DataBase dataBase = databaseMapper.findByIdAndDelete(dbId, SoftDelete.normal);
        if (dataBase == null) {
            return new ListResult<>();
        }

        ListResult<TableResourceVo> dbResourcesPage = searchDbResource(dbId, StringUtil.emptyToNull(name), isView, rows, page);
        Set<Long> resourceId = new HashSet<>();
        if (roleId != null) {
            Role param = new Role();
            param.setId(roleId);
            List<RoleResource> roleResources = roleService.listResources(param);
            if (!CollectionUtils.isEmpty(roleResources)) {
                for (RoleResource r : roleResources) {
                    resourceId.add(r.getResourceId());
                }
            }
        }
        List<TableResourceVo> voList = dbResourcesPage.getRows()
                .stream()
                .map(t -> {
                    TableResourceVo vo = new TableResourceVo(t);
                    vo.setDbType(dataBase.getDbType());
                    if (resourceId.contains(t.getId()))
                        vo.setChecked(true);
                    return vo;
                }).collect(Collectors.toList());

        dbResourcesPage.setRows(voList);
        return dbResourcesPage;
    }

    @RequestMapping("/del/{id}/{flag}")
    public CommonResult<String> updateDelete(@PathVariable("id") Long id, @PathVariable("flag") Integer flag) {
        int i = dbResourceService.updateDelete(id, flag);
        return CommonResult.newInstance(ResultCode.SUCCESS, String.class);
    }

    @RequestMapping(value = "/sync/{id}", method = RequestMethod.POST)
    public CommonResult<String> sync(@PathVariable("id") Long id) {
        if (id == null)
            return CommonResult.newInstance(ResultCode.PARAM_ERROR, String.class);

        try {
            this.dbSynchronizer.sync(id);
        } catch (Exception e) {
            logger.error(e.getMessage(),e);
            return CommonResult.newInstance(ResultCode.SERVER_ERROR, String.class).setMessage(e.getMessage());
        }
        return CommonResult.newInstance(ResultCode.SUCCESS, String.class);
    }

}
