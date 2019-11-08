package com.augurit.aplanmis.data.exchange.mapper.spgl;

import com.augurit.aplanmis.data.exchange.domain.spgl.SpglXmdwxxb;
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
public interface SpglXmdwxxbMapper {

    void insertSpglXmdwxxb(SpglXmdwxxb spglXmdwxxb);

    void updateSpglXmdwxxb(SpglXmdwxxb spglXmdwxxb);

    void deleteSpglXmdwxxb(@Param("id") String id);

    List<SpglXmdwxxb> listSpglXmdwxxb(SpglXmdwxxb spglXmdwxxb);

    SpglXmdwxxb getSpglXmdwxxbById(@Param("id") String id);

    void batchInsertSpglXmdwxxb(@Param("list") List<SpglXmdwxxb> list);
}