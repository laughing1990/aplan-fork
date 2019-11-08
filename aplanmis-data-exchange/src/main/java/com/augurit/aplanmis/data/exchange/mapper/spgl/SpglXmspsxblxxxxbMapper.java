package com.augurit.aplanmis.data.exchange.mapper.spgl;

import com.augurit.aplanmis.data.exchange.domain.spgl.SpglXmspsxblxxxxb;
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
public interface SpglXmspsxblxxxxbMapper {

    void insertSpglXmspsxblxxxxb(SpglXmspsxblxxxxb spglXmspsxblxxxxb);

    void updateSpglXmspsxblxxxxb(SpglXmspsxblxxxxb spglXmspsxblxxxxb);

    void deleteSpglXmspsxblxxxxb(@Param("id") String id);

    List<SpglXmspsxblxxxxb> listSpglXmspsxblxxxxb(SpglXmspsxblxxxxb spglXmspsxblxxxxb);

    SpglXmspsxblxxxxb getSpglXmspsxblxxxxbById(@Param("id") String id);

    void batchInsertSpglXmspsxblxxxxb(@Param("list") List<SpglXmspsxblxxxxb> list);
}