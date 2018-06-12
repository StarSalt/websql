package org.singledog.websql.user.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.features.jpa.domain.Page;
import org.apache.ibatis.features.jpa.domain.Pageable;
import org.apache.ibatis.features.jpa.domain.Sort;
import org.apache.ibatis.features.jpa.mapper.JpaMapper;
import org.singledog.websql.user.entity.DbResource;

import java.util.List;

/**
 * Created by Adam on 2017/9/8.
 */
@Mapper
public interface DbResourceMapper extends JpaMapper<DbResource, Long> {

    DbResource findByDatabaseIdAndTableName(Long databaseId, String tableName);

    List<DbResource> findByDatabaseIdAndDelete(Long databaseId, Integer delete, Sort sort);

    List<DbResource> findByIdInAndDelete(List<Long> ids, Integer delete);

    int updateDelete(@Param("id") Long id, @Param("flag") Integer flag);

    Page<DbResource> searchDbResource(@Param("dbId") Long dbId, @Param("name") String name, @Param("isView") Integer isView,
                                      Pageable pageable);

    List<DbResource> searchDbResourceNoPage(@Param("dbId") Long dbId, @Param("name") String name, @Param("isView") Integer isView);
}
