package org.singledog.websql.query.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.features.jpa.domain.Page;
import org.apache.ibatis.features.jpa.domain.Pageable;
import org.apache.ibatis.features.jpa.mapper.JpaMapper;
import org.singledog.websql.query.entity.QueryLog;
import org.singledog.websql.query.param.QueryHistoryParam;

import java.util.List;

/**
 * Created by Adam on 2017/9/20.
 */
@Mapper
public interface QueryLogMapper extends JpaMapper<QueryLog, String> {

    Page<QueryLog> queryHistory(@Param("param") QueryHistoryParam param, @Param("pageable") Pageable pageable);

}
