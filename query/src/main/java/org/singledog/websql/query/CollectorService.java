package org.singledog.websql.query;

import org.apache.ibatis.features.jpa.domain.Page;
import org.apache.ibatis.features.jpa.domain.PageRequest;
import org.apache.ibatis.features.jpa.domain.Sort;
import org.singledog.websql.query.entity.SqlCollector;
import org.singledog.websql.query.mapper.SqlCollectorMapper;
import org.singledog.websql.user.entity.UserEntity;
import org.singledog.websql.user.event.EventType;
import org.singledog.websql.user.event.LoggerEventBuilder;
import org.singledog.websql.user.exceptions.ServiceException;
import org.singledog.websql.user.param.HttpInfoHolder;
import org.singledog.websql.user.util.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.Objects;

/**
 * Created by Adam on 2017/12/6.
 */
@Service
public class CollectorService {

    @Autowired
    private SqlCollectorMapper collectorMapper;
    @Autowired
    private ApplicationContext applicationContext;

    /**
     * 收藏
     * @param title
     * @param sql
     */
    @Transactional
    public void collectSql(String title, String sql) {
        UserEntity userEntity = HttpInfoHolder.getUserEntityNotNull();
        SqlCollector collector = new SqlCollector();
        collector.defaultProperty();
        collector.setUserId(userEntity.getId());
        collector.setUserName(userEntity.getUserName());
        collector.setTitle(title);
        collector.setSql(sql);
        collectorMapper.save(collector);

        applicationContext.publishEvent(LoggerEventBuilder
                .getInstance(CollectorService.class)
                .eventType(EventType.COLLECT_SQL)
                .userEntity(userEntity)
                .params("title:".concat(title).concat(", sql: ").concat(sql))
                .remark("collect id: ".concat(String.valueOf(collector.getId())))
                .build());
    }

    @Transactional
    public int delete(Long id) {
        UserEntity userEntity = HttpInfoHolder.getUserEntityNotNull();
        SqlCollector collector = collectorMapper.findById(id);
        if (collector != null) {
            if (!Objects.equals(userEntity.getId(), collector.getUserId())) {
                throw new ServiceException("没有权限!");
            }

            return collectorMapper.deleteById(id);
        }

        return 0;
    }

    /**
     *
     * @param pageNum 0-based
     * @param pageSize
     * @return
     */
    public Page<SqlCollector> list(Long userId, String title, Integer pageNum, Integer pageSize) {
        if (userId == null) {
            userId = HttpInfoHolder.getUserEntityNotNull().getId();
        }

        if (pageNum == null)
            pageNum = 0;
        if (pageSize == null)
            pageSize = 20;

        if (StringUtils.isEmpty(title)) {
            return collectorMapper.findByUserId(userId, PageRequest.of(pageNum, pageSize,
                    Sort.Direction.DESC, "useCount","createTime"));
        }

        return collectorMapper.findByUserIdAndTitleLike(userId, StringUtil.wrapLike(title),
                PageRequest.of(pageNum, pageSize, Sort.Direction.DESC, "useCount","createTime"));
    }

    @Transactional
    public int increaseUseCount(Long id, Long userId) {
        return collectorMapper.increaseUseCount(id, userId);
    }



}
