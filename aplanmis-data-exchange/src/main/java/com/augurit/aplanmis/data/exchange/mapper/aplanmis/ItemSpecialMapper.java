package com.augurit.aplanmis.data.exchange.mapper.aplanmis;

import com.augurit.aplanmis.data.exchange.domain.spgl.SpglXmspsxbltbcxxxb;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

/**
 * @author yinlf
 * @date 2019/12/20
 * Copyright(c) 2013 广州奥格智能科技有限公司 版权所有
 */
@Mapper
@Repository
public interface ItemSpecialMapper {

    SpglXmspsxbltbcxxxb getSpglXmspsxbltbcxxxbById(@Param("id") String id);

    List<SpglXmspsxbltbcxxxb> listSpglXmspsxbltbcxxxb(SpglXmspsxbltbcxxxb spglXmspsxbltbcxxxb);

    List<SpglXmspsxbltbcxxxb> listSpglXmspsxbltbcxxxbByTimeRange(@Param("startTime") Date startTime, @Param("endTime") Date endTime);
}
