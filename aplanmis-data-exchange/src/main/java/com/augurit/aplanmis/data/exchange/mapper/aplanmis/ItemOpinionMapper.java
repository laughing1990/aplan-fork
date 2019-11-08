package com.augurit.aplanmis.data.exchange.mapper.aplanmis;

import com.augurit.aplanmis.data.exchange.domain.spgl.SpglXmspsxblxxxxb;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

/**
 * @author yinlf
 * @date 2019/08/31
 * Copyright(c) 2013 广州奥格智能科技有限公司 版权所有
 */
@Mapper
@Repository
public interface ItemOpinionMapper {

    List<SpglXmspsxblxxxxb> listSpglXmspsxblxxxxb(SpglXmspsxblxxxxb spglXmspsxblxxxxb);

    SpglXmspsxblxxxxb getSpglXmspsxblxxxxbById(@Param("id") String id);

    List<SpglXmspsxblxxxxb> listSpglXmspsxblxxxxbByTimeRange(@Param("startTime") Date startTime, @Param("endTime") Date endTime);

    List<SpglXmspsxblxxxxb> listSpglXmspfssxblxxxxbByTimeRange(@Param("startTime") Date startTime, @Param("endTime") Date endTime);
}