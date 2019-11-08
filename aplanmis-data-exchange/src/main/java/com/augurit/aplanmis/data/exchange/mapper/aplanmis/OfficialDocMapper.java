package com.augurit.aplanmis.data.exchange.mapper.aplanmis;

import com.augurit.aplanmis.data.exchange.domain.spgl.SpglXmspsxpfwjxxb;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

/**
 * @author yinlf
 * @date 2019/08/31
 * Copyright(c) 2013 广州奥格智能科技有限公司 版权所有
 */
@Mapper
public interface OfficialDocMapper {

    List<SpglXmspsxpfwjxxb> listSpglXmspsxpfwjxxb(SpglXmspsxpfwjxxb spglXmspsxpfwjxxb);

    SpglXmspsxpfwjxxb getSpglXmspsxpfwjxxbById(@Param("id") String id);

    List<SpglXmspsxpfwjxxb> listSpglXmspsxpfwjxxbByTimeRange(@Param("startTime") Date startTime, @Param("endTime") Date endTime);
}