package com.augurit.aplanmis.data.exchange.mapper.aplanmis;

import com.augurit.aplanmis.data.exchange.domain.spgl.SpglDfxmsplcxxb;
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
public interface ThemeVerMapper {

    List<SpglDfxmsplcxxb> listSpglDfxmsplcxxb(SpglDfxmsplcxxb spglDfxmsplcxxb);

    SpglDfxmsplcxxb getSpglDfxmsplcxxbById(@Param("id") String id);

    List<SpglDfxmsplcxxb> listSpglDfxmsplcxxbByTimeRange(@Param("startTime") Date startTime, @Param("endTime") Date endTime);
}