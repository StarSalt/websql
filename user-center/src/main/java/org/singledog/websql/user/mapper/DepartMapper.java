package org.singledog.websql.user.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.features.jpa.mapper.JpaMapper;
import org.singledog.websql.user.entity.Depart;

import java.util.List;

/**
 * Created by Adam on 2017/9/8.
 */
@Mapper
public interface DepartMapper extends JpaMapper<Depart, Long> {

    List<Depart> searchDepart(@Param("name") String name, @Param("delete") Integer delete);

    List<Depart> searchSonDepart(@Param("pid") Long parentId, @Param("name") String name, @Param("delete") Integer delete);

    int softDelete(Long id);
}
