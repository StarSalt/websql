package org.singledog.websql.user.service;

import com.alibaba.fastjson.JSON;
import org.singledog.websql.user.entity.Depart;
import org.singledog.websql.user.entity.SoftDelete;
import org.singledog.websql.user.event.EventType;
import org.singledog.websql.user.event.LoggerEventBuilder;
import org.singledog.websql.user.exceptions.ServiceException;
import org.singledog.websql.user.mapper.DepartMapper;
import org.singledog.websql.user.param.HttpInfoHolder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.List;

/**
 * Created by Adam on 2017/9/8.
 */
@Service
public class DepartService {
    private static final Logger logger = LoggerFactory.getLogger(DepartService.class);

    @Autowired
    private DepartMapper departMapper;
    @Autowired
    private ApplicationContext applicationContext;

    @Transactional
    public Depart addDepart(String deptName, Long parentId) {
        HttpInfoHolder.getAdmin();
        Depart depart = new Depart();
        depart.defaultProperty();
        depart.setDepartName(deptName);
        if (parentId != null) {
            Depart parent = departMapper.findById(parentId);
            if (parent == null)
                throw new ServiceException("Parent depart is null !");
            depart.setParentId(parent.getId());
            depart.setRootId(parent.getRootId());
            if (parent.getParentId() == null)
                depart.setRootId(parentId);
        }

        try {
            this.departMapper.saveAutoIncrementKey(depart);
        } catch (DuplicateKeyException e) {
            logger.error(e.getMessage(), e);
            throw new ServiceException("数据重复!");
        }

        applicationContext.publishEvent(LoggerEventBuilder
                .getInstance(DatabaseService.class)
                .eventType(EventType.ADD_DEPART)
                .userEntity(HttpInfoHolder.getUserEntity())
                .params(JSON.toJSONString(deptName + "," + parentId))
                .remark("depart id: ".concat(String.valueOf(depart.getId())))
                .build());

        return depart;
    }

    @Transactional
    public int softDelete(Long id) {
        int i = departMapper.softDelete(id);
        applicationContext.publishEvent(LoggerEventBuilder
                .getInstance(DatabaseService.class)
                .eventType(EventType.DELETE_DEPART)
                .userEntity(HttpInfoHolder.getUserEntity())
                .params(JSON.toJSONString("id:" + id))
                .remark("depart id: ".concat(String.valueOf(id)))
                .build());
        return i;
    }

    public List<Depart> queryAll(String name) {
        if (!StringUtils.isEmpty(name))
            name = name.concat("%");
        return departMapper.searchDepart(name, SoftDelete.normal);
    }

    public List<Depart> findByParentId(Long parentId, String name) {
        if (!StringUtils.isEmpty(name))
            name = name.concat("%");

        return departMapper.searchSonDepart(parentId, name, SoftDelete.normal);
    }

}
