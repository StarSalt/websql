package org.singledog.websql.user.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.features.jpa.domain.Page;
import org.apache.ibatis.features.jpa.domain.Pageable;
import org.apache.ibatis.features.jpa.mapper.JpaMapper;
import org.singledog.websql.user.entity.OperateLog;
import org.singledog.websql.user.param.OplogQueryParam;

/**
 * Created by Adam on 2017/9/12.
 */
@Mapper
public interface OperateLogMapper extends JpaMapper<OperateLog, Long> {

    Page<OperateLog> findByUserId(Long userId, Pageable pageable);

    Page<OperateLog> findForList(@Param("op") OplogQueryParam param, @Param("pageable") Pageable pageable);

}
