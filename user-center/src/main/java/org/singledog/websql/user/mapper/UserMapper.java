package org.singledog.websql.user.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;
import org.apache.ibatis.features.jpa.domain.Page;
import org.apache.ibatis.features.jpa.domain.Pageable;
import org.apache.ibatis.features.jpa.mapper.JpaMapper;
import org.omg.PortableInterceptor.USER_EXCEPTION;
import org.singledog.websql.user.entity.UserEntity;
import org.singledog.websql.user.entity.UserRole;
import org.singledog.websql.user.param.UserSearchParam;

import java.util.Date;

/**
 * Created by Adam on 2017/9/8.
 */
@Mapper
public interface UserMapper extends JpaMapper<UserEntity, Long> {

    @Update("update user_info set status=#{lock},update_user_id=#{updateUser},lock_time=now()," +
            " update_time=now() where id=#{userId}")
    int updateUserLock(@Param("userId") Long userId, @Param("updateUser") Long updateUser,
                       @Param("lock") Byte lock);

    @Update("update user_info set del=#{delete}, update_time=now(), update_user_id=#{uu}" +
            " where id=#{userId}")
    int updateUserDelete(@Param("userId") Long userId, @Param("delete") Integer delete, @Param("uu") Long updateUser);

    Page<UserEntity> searchUsers(@Param("usp") UserSearchParam userSearchParam, @Param("page") Pageable pageable);

    UserEntity findByUserName(String userName);

    UserEntity findByEmail(String email);
}
