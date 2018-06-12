package org.singledog.websql.web.admin.controller;

import org.apache.ibatis.features.jpa.domain.Page;
import org.singledog.websql.user.entity.DataBase;
import org.singledog.websql.user.mapper.DatabaseMapper;
import org.singledog.websql.user.param.DatabaseParam;
import org.singledog.websql.user.param.DatabaseParam4Update;
import org.singledog.websql.user.service.DatabaseService;
import org.singledog.websql.user.service.UserService;
import org.singledog.websql.user.util.StringUtil;
import org.singledog.websql.web.result.CommonResult;
import org.singledog.websql.web.result.ListResult;
import org.singledog.websql.web.result.ResultCode;
import org.singledog.websql.web.vo.DatabaseResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by Adam on 2017/9/15.
 */
@RequestMapping("/admin/database")
@RestController
public class DatabaseController {
    private static final Logger logger = LoggerFactory.getLogger(DatabaseController.class);

    @Autowired
    private DatabaseService databaseService;
    @Autowired
    private UserService userService;
    @Autowired
    private DatabaseMapper databaseMapper;

    @RequestMapping(value = "/add", method = RequestMethod.POST)
    public CommonResult<String> addDatabase(@RequestBody @Valid DatabaseParam databaseParam) {
        try {
            DataBase db = this.databaseService.addDatabase(databaseParam);
            return new CommonResult<String>().fillResult(ResultCode.SUCCESS);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return new CommonResult<String>().fillResult(ResultCode.SERVER_ERROR).setMessage(e.getMessage());
        }
    }

    @RequestMapping("/detail/{id}")
    public CommonResult<DatabaseResult> detail(@PathVariable("id") Long id) {
        DataBase dataBase = databaseMapper.findById(id);
        if (dataBase == null)
            return new CommonResult<DatabaseResult>().fillResult(ResultCode.NO_RESULT);

        return new CommonResult<DatabaseResult>().fillResult(ResultCode.SUCCESS).setData(new DatabaseResult(dataBase));
    }

    @RequestMapping(value = "/delete/{id}", method = RequestMethod.POST)
    public CommonResult<String> delete(@PathVariable("id") Long id) {
        int i = databaseService.deleteById(id);
        if (i == 1) {
            return CommonResult.newInstance(ResultCode.SUCCESS, String.class);
        } else {
            return CommonResult.newInstance(ResultCode.PARAM_ERROR, String.class).setMessage("id不存在");
        }
    }

    @RequestMapping(value = "/update", method = RequestMethod.POST)
    public CommonResult<String> updateDatabase(@RequestBody @Valid DatabaseParam4Update databaseParam) {
        if (databaseParam.getId() == null)
            return new CommonResult<String>().fillResult(ResultCode.PARAM_ERROR);

        try {
            DataBase db = this.databaseService.updateDatabase(databaseParam);
            return new CommonResult<String>().fillResult(ResultCode.SUCCESS);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return new CommonResult<String>().fillResult(ResultCode.SERVER_ERROR).setMessage(e.getMessage());
        }
    }

    @RequestMapping(value = "/list")
    public ListResult<DatabaseResult> listDatabases(String name, String type, String url,
                                                     Integer rows, Integer page) {
        if (rows == null)
            rows = 20;
        if (page == null)
            page = 1;

        Page<DataBase> dataBasePage = databaseService.searchDatabases(StringUtil.emptyToNull(name),
                StringUtil.emptyToNull(url), StringUtil.emptyToNull(type), page - 1, rows);
        ListResult<DatabaseResult> listResult = new ListResult<>(dataBasePage);
        List<DatabaseResult> databaseResults = dataBasePage.getContent()
                .stream()
                .map(DatabaseResult::new)
                .collect(Collectors.toList());
        userService.loadUserName(databaseResults);
        listResult.setRows(databaseResults);
        return listResult;
    }

    @RequestMapping(value = "/listnopage")
    public ListResult<DatabaseResult> listDatabasesNoPage() {
        ListResult<DatabaseResult> dbResults = new ListResult<>();
        List<DataBase> dataBasePage = databaseService.getAllDatabases();
        List<DatabaseResult> databaseResults = dataBasePage
                .stream()
                .map(DatabaseResult::new)
                .collect(Collectors.toList());
        userService.loadUserName(databaseResults);
        dbResults.setRows(databaseResults);
        return dbResults;
    }

}
