package org.singledog.websql.user.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.features.jpa.mapper.JpaMapper;
import org.singledog.websql.user.entity.RoleResource;

import java.util.List;

/**
 * Created by Adam on 2017/9/8.
 */
@Mapper
public interface RoleResourceMapper extends JpaMapper<RoleResource, Long> {

    List<RoleResource> findByRoleIdIn(@Param("list") List<Long> roleIds, @Param("dbId") Long databaseId);

    int deleteByRoleId(Long roleId);

}
