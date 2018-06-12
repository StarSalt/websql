package org.singledog.websql.web.admin.controller;

import org.apache.ibatis.features.jpa.domain.Page;
import org.apache.ibatis.features.jpa.domain.PageRequest;
import org.apache.ibatis.features.jpa.domain.Sort;
import org.singledog.websql.query.entity.QueryLog;
import org.singledog.websql.query.mapper.QueryLogMapper;
import org.singledog.websql.query.param.QueryHistoryParam;
import org.singledog.websql.user.util.StringUtil;
import org.singledog.websql.web.result.ListResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by Adam on 2018-06-11.
 */
@RestController
@RequestMapping("/admin/history")
public class AdminHistoryController {

    @Autowired
    private QueryLogMapper queryLogMapper;

    @RequestMapping("/list")
    public ListResult<QueryLog> serachDepart(QueryHistoryParam param) {
        if (param.getPage() == null) {
            param.setPage(0);
        } else {
            param.setPage(param.getPage() - 1);
        }

        if (param.getRows() == null)
            param.setRows(30);

        param.setUserName(StringUtil.emptyToNull(param.getUserName()));
        if (!StringUtils.isEmpty(param.getUserName())) {
            param.setUserName(StringUtil.wrapLikeRight(param.getUserName()));
        }

        param.setUserEmail(StringUtil.emptyToNull(param.getUserEmail()));
        if (!StringUtils.isEmpty(param.getUserEmail())) {
            param.setUserEmail(StringUtil.wrapLikeRight(param.getUserEmail()));
        }

        param.setTableName(StringUtil.emptyToNull(param.getTableName()));
        if (!StringUtils.isEmpty(param.getTableName())) {
            param.setTableName(StringUtil.wrapLike(param.getUserEmail()));
        }

        PageRequest pageRequest = PageRequest.of(param.getPage(), param.getRows(), Sort.Direction.DESC, "createTime");

        Page<QueryLog> departs = queryLogMapper.queryHistory(param, pageRequest);
        ListResult<QueryLog> listResult = new ListResult<>(departs);
        listResult.setRows(departs.getContent());

        return listResult;
    }

}
