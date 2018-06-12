package org.singledog.websql.user.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.features.jpa.mapper.JpaMapper;
import org.singledog.websql.user.entity.UserRole;

import java.util.List;

/**
 * Created by Adam on 2017/9/8.
 */
@Mapper
public interface UserRoleMapper extends JpaMapper<UserRole, Long> {

    List<UserRole> findByUserId(Long userId);

    int deleteByUserIdAndRoleIdIn(Long userId, List<Long> roleIds);

    int deleteByUserId(Long userId);

}
