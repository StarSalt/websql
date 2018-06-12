package org.singledog.websql.user.audit;

import org.apache.ibatis.features.jpa.domain.Page;
import org.apache.ibatis.features.jpa.domain.PageRequest;
import org.apache.ibatis.features.jpa.domain.Sort;
import org.singledog.websql.user.entity.OperateLog;
import org.singledog.websql.user.mapper.OperateLogMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by Adam on 2017/9/12.
 */
@Service
public class OperateLogService {

    private static final Logger logger = LoggerFactory.getLogger(OperateLogService.class);

    @Autowired
    private OperateLogMapper operateLogMapper;

    @Transactional
    public int save(OperateLog operateLog) {
        return operateLogMapper.saveAutoIncrementKey(operateLog);
    }

    public Page<OperateLog> listOperateLogs(Long userId, int pageNum, int pageSize) {
        PageRequest pageRequest = PageRequest.of(pageNum, pageSize, new Sort(Sort.Direction.DESC, "opTime"));
        if (userId == null) {
            return operateLogMapper.findAllByPage(pageRequest);
        }

        return operateLogMapper.findByUserId(userId, pageRequest);
    }

}
