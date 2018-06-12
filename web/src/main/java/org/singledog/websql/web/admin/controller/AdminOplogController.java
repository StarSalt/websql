package org.singledog.websql.web.admin.controller;

import org.apache.ibatis.features.jpa.domain.Page;
import org.apache.ibatis.features.jpa.domain.PageRequest;
import org.apache.ibatis.features.jpa.domain.Pageable;
import org.apache.ibatis.features.jpa.domain.Sort;
import org.singledog.websql.user.entity.OperateLog;
import org.singledog.websql.user.mapper.OperateLogMapper;
import org.singledog.websql.user.param.OplogQueryParam;
import org.singledog.websql.user.util.StringUtil;
import org.singledog.websql.web.result.ListResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by Adam on 2018-06-12.
 */
@RestController
@RequestMapping("/admin/oplog")
public class AdminOplogController {

    @Autowired
    private OperateLogMapper operateLogMapper;

    @RequestMapping("/list")
    public ListResult<OperateLog> find(OplogQueryParam param) {
        if (param.getPage() == null) {
            param.setPage(0);
        } else {
            param.setPage(param.getPage() - 1);
        }

        if (param.getRows() == null)
            param.setRows(30);

        param.setUserName(StringUtil.emptyToNull(param.getUserName()));
        param.setUserEmail(StringUtil.emptyToNull(param.getUserEmail()));
        param.setEventType(StringUtil.emptyToNull(param.getEventType()));
        if (!StringUtils.isEmpty(param.getUserName())) {
            param.setUserName(StringUtil.wrapLike(param.getUserName()));
        }

        if (!StringUtils.isEmpty(param.getUserEmail())) {
            param.setUserEmail(StringUtil.wrapLike(param.getUserEmail()));
        }

        Pageable pageable = PageRequest.of(param.getPage(), param.getRows(), Sort.Direction.DESC, "opTime");
        Page<OperateLog> page = operateLogMapper.findForList(param, pageable);
        ListResult<OperateLog> operateLogListResult = new ListResult<>(page);
        operateLogListResult.setRows(page.getContent());
        return operateLogListResult;
    }

}
