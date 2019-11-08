package com.augurit.aplanmis.data.exchange.mapper.spgl;

import com.augurit.aplanmis.data.exchange.domain.spgl.SpglXmspsxblxxb;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author yinlf
 * @date 2019/08/31
 * Copyright(c) 2013 广州奥格智能科技有限公司 版权所有
 */
@Mapper
@Repository
public interface SpglXmspsxblxxbMapper {

    void insertSpglXmspsxblxxb(SpglXmspsxblxxb spglXmspsxblxxb);

    void updateSpglXmspsxblxxb(SpglXmspsxblxxb spglXmspsxblxxb);

    void deleteSpglXmspsxblxxb(@Param("id") String id);

    List<SpglXmspsxblxxb> listSpglXmspsxblxxb(SpglXmspsxblxxb spglXmspsxblxxb);

    SpglXmspsxblxxb getSpglXmspsxblxxbById(@Param("id") String id);

    void batchInsertSpglXmspsxblxxb(@Param("list") List<SpglXmspsxblxxb> list);
}