package com.augurit.aplanmis.data.exchange.mapper.spgl;

import com.augurit.aplanmis.data.exchange.domain.spgl.SpglXmjbxxb;
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
public interface SpglXmjbxxbMapper {

    void insertSpglXmjbxxb(SpglXmjbxxb spglXmjbxxb);

    void updateSpglXmjbxxb(SpglXmjbxxb spglXmjbxxb);

    void deleteSpglXmjbxxb(@Param("id") String id);

    List<SpglXmjbxxb> listSpglXmjbxxb(SpglXmjbxxb spglXmjbxxb);

    SpglXmjbxxb getSpglXmjbxxbById(@Param("id") String id);

    List<SpglXmjbxxb> getSpglXmjbxxbByXzqhdmAndGcdM(@Param("xzqhdm") String xzqhdm, @Param("gcdm") String gcdm);

    void batchInsertSpglXmjbxxb(@Param("list") List<SpglXmjbxxb> list);
}