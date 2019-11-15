package com.augurit.aplanmis.data.exchange.mapper.spgl;

import com.augurit.aplanmis.data.exchange.domain.spgl.SpglDfghkzxxxb;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author qhg
 * @date 2019/11/07
 * Copyright(c) 2013 广州奥格智能科技有限公司 版权所有
 */
@Mapper
@Repository
public interface SpglDfghkzxxxbMapper {

    void insertSpglDfghkzxxxb(SpglDfghkzxxxb spglDfghkzxxxb);

    void updateSpglDfghkzxxxb(SpglDfghkzxxxb spglDfghkzxxxb);

    void deleteSpglDfghkzxxxb(@Param("id") String id);

    List<SpglDfghkzxxxb> listSpglDfghkzxxxb(SpglDfghkzxxxb spglDfghkzxxxb);

    SpglDfghkzxxxb getSpglDfghkzxxxbById(@Param("id") String id);

    List<SpglDfghkzxxxb> getSpglDfghkzxxxbByXzqhdmAndGcdM(@Param("xzqhdm") String xzqhdm, @Param("gcdm") String gcdm);

    void batchInsertSpglDfghkzxxxb(@Param("list") List<SpglDfghkzxxxb> list);
}
