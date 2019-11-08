package com.augurit.aplanmis.data.exchange.mapper.spgl;

import com.augurit.aplanmis.data.exchange.domain.spgl.SpglXmspsxpfwjxxb;
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
public interface SpglXmspsxpfwjxxbMapper {

    void insertSpglXmspsxpfwjxxb(SpglXmspsxpfwjxxb spglXmspsxpfwjxxb);

    void updateSpglXmspsxpfwjxxb(SpglXmspsxpfwjxxb spglXmspsxpfwjxxb);

    void deleteSpglXmspsxpfwjxxb(@Param("id") String id);

    List<SpglXmspsxpfwjxxb> listSpglXmspsxpfwjxxb(SpglXmspsxpfwjxxb spglXmspsxpfwjxxb);

    SpglXmspsxpfwjxxb getSpglXmspsxpfwjxxbById(@Param("id") String id);

    void batchInsertSpglXmspsxpfwjxxb(@Param("list") List<SpglXmspsxpfwjxxb> list);
}