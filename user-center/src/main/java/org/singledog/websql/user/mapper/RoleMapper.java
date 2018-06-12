package org.singledog.websql.user.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.features.jpa.domain.Page;
import org.apache.ibatis.features.jpa.domain.Pageable;
import org.apache.ibatis.features.jpa.mapper.JpaMapper;
import org.singledog.websql.user.entity.Role;
import org.singledog.websql.user.param.RoleSearchParam;

import java.util.List;

/**
 * Created by Adam on 2017/9/8.
 */
@Mapper
public interface RoleMapper extends JpaMapper<Role, Long> {

    Page<Role> searchRoles(@Param("rsp") RoleSearchParam roleSearchParam, Pageable pageable);

    List<Role> findByIdInAndRoleTypeAndDelete(List<Long> ids, Integer roleType, Integer delete);

    int softDelete(Long id);
}
