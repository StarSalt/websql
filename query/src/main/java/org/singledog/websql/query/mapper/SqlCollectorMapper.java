package org.singledog.websql.query.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.features.jpa.domain.Page;
import org.apache.ibatis.features.jpa.domain.Pageable;
import org.apache.ibatis.features.jpa.mapper.JpaMapper;
import org.singledog.websql.query.entity.SqlCollector;

/**
 * Created by Adam on 2017/12/6.
 */
@Mapper
public interface SqlCollectorMapper extends JpaMapper<SqlCollector, Long> {

    Page<SqlCollector> findByUserId(Long userId, Pageable pageable);

    Page<SqlCollector> findByUserIdAndTitleLike(Long userId, String title, Pageable pageable);

    int increaseUseCount(@Param("id") Long id, @Param("userId") Long userId);

}
