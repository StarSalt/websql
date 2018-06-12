package org.singledog.websql.web.admin.controller;

import org.singledog.websql.user.entity.Depart;
import org.singledog.websql.user.exceptions.ServiceException;
import org.singledog.websql.user.service.DepartService;
import org.singledog.websql.user.util.StringUtil;
import org.singledog.websql.web.param.AddDepartParam;
import org.singledog.websql.web.result.CommonResult;
import org.singledog.websql.web.result.ResultCode;
import org.singledog.websql.web.result.TreeResult;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Created by adam on 9/17/17.
 */
@RestController
@RequestMapping("/admin/depart")
public class AdminDepartController {

    private static final Logger logger = org.slf4j.LoggerFactory.getLogger(AdminDepartController.class);

    @Autowired
    private DepartService departService;

    @RequestMapping("/list")
    public List<TreeResult> serachDepart(String name) {
        if (!StringUtils.isEmpty(name))
            name = StringUtil.wrapLike(name);

        List<Depart> departs = departService.queryAll(name);
        return Collections.singletonList(composeTree(departs));
    }

    @RequestMapping(value = "/add", method = RequestMethod.POST)
    public CommonResult<String> addDepart(@RequestBody AddDepartParam addDepartParam) {
        try {
            Depart depart = departService.addDepart(addDepartParam.getName(), addDepartParam.getParentId());
            return new CommonResult<String>().fillResult(ResultCode.SUCCESS);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return CommonResult.newInstance(ResultCode.PARAM_ERROR, String.class).setMessage(e.getMessage());
        }
    }

    @RequestMapping(value = "/del/{id}", method = RequestMethod.POST)
    public CommonResult<String> delDepart(@PathVariable("id") Long id) {
        int i = departService.softDelete(id);
        if (i == 1)
            return CommonResult.newInstance(ResultCode.SUCCESS, String.class);

        return CommonResult.newInstance(ResultCode.SUCCESS, String.class).setMessage("删除成功! 影响： ".concat(String.valueOf(i)));
    }

    private TreeResult composeTree(List<Depart> departs) {
        TreeResult root = new TreeResult(null, "所有部门");
        if (CollectionUtils.isEmpty(departs))
            return root;

        Map<Long, List<Depart>> departMap = departs.stream().map(d -> {
            if (d.getParentId() == null)
                d.setParentId(0L);

            return d;
        }).collect(Collectors.groupingBy(Depart::getParentId));
        this.appendChild(root, departMap, 0L);
        return root;
    }

    private void appendChild(TreeResult treeResult, Map<Long, List<Depart>> departMap, Long parentId) {
        List<Depart> parents = departMap.get(parentId);
        if (CollectionUtils.isEmpty(parents))
            return;
        for (Depart parent : parents) {
            TreeResult current = new TreeResult(parent);
            treeResult.getChildren().add(current);
            appendChild(current, departMap, parent.getId());
        }
    }

}
