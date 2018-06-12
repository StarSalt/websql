package org.singledog.websql.user.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.features.jpa.domain.Page;
import org.apache.ibatis.features.jpa.domain.Pageable;
import org.apache.ibatis.features.jpa.domain.Sort;
import org.apache.ibatis.features.jpa.mapper.JpaMapper;
import org.singledog.websql.user.entity.DataBase;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * Created by Adam on 2017/9/8.
 */
@Mapper
public interface DatabaseMapper extends JpaMapper<DataBase, Long> {

    List<DataBase> findByDelete(Integer delete, Sort sort);

    List<DataBase> findByIdInAndDelete(List<Long> id, Integer delete);

    DataBase findByIdAndDelete(Long id, Integer delete);

    Page<DataBase> searchDatabase(@Param("name") String name, @Param("url") String url,
                                  @Param("type") String type, Pageable pageable);

    int softDeleteById(Long id);
}
