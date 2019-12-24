package com.augurit.aplanmis.data.exchange.mapper.spgl;

import com.augurit.aplanmis.data.exchange.domain.spgl.SpglXmspsxbltbcxxxb;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author ylf_i
 * @date 2019/12/17
 * Copyright(c) 2013 广州奥格智能科技有限公司 版权所有
 */
@Mapper
@Repository
public interface SpglXmspsxbltbcxxxbMapper {

    void insertSpglXmspsxbltbcxxxb(SpglXmspsxbltbcxxxb spglXmspsxbltbcxxxb);

    void updateSpglXmspsxbltbcxxxb(SpglXmspsxbltbcxxxb spglXmspsxbltbcxxxb);

    void deleteSpglXmspsxbltbcxxxb(@Param("id") String id);

    List<SpglXmspsxbltbcxxxb> listSpglXmspsxbltbcxxxb(SpglXmspsxbltbcxxxb spglXmspsxbltbcxxxb);

    SpglXmspsxbltbcxxxb getSpglXmspsxbltbcxxxbById(@Param("id") String id);

    void batchInsertSpglXmspsxbltbcxxxb(@Param("list") List<SpglXmspsxbltbcxxxb> list);
}