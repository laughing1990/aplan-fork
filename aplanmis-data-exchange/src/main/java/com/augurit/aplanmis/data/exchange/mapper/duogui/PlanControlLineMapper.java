package com.augurit.aplanmis.data.exchange.mapper.duogui;

import com.augurit.aplanmis.data.exchange.domain.spgl.SpglDfghkzxxxb;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

/**
 * @author qhg
 * @date 2019/11/06
 * Copyright(c) 2013 广州奥格智能科技有限公司 版权所有
 */
@Mapper
@Repository
public interface PlanControlLineMapper {

    List<SpglDfghkzxxxb> listSpglDfghkzxxxb(SpglDfghkzxxxb spglDfghkzxxxb);

    SpglDfghkzxxxb getSpglDfghkzxxxbById(@Param("id") String id);

    List<SpglDfghkzxxxb> listSpglDfghkzxxxbByTimeRange(@Param("startTime") Date startTime, @Param("endTime") Date endTime);
}