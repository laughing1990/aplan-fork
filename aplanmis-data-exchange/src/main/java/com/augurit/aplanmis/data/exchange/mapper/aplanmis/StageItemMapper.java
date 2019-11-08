package com.augurit.aplanmis.data.exchange.mapper.aplanmis;

import com.augurit.aplanmis.data.exchange.domain.spgl.SpglDfxmsplcjdsxxxb;
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
public interface StageItemMapper {
    /**
     * 查询
     *
     * @param spglDfxmsplcjdsxxxb
     * @return
     */
    List<SpglDfxmsplcjdsxxxb> listSpglDfxmsplcjdsxxxb(SpglDfxmsplcjdsxxxb spglDfxmsplcjdsxxxb);

    /**
     * 查询
     *
     * @param id
     * @return
     */
    SpglDfxmsplcjdsxxxb getSpglDfxmsplcjdsxxxbById(@Param("id") String id);

    List<SpglDfxmsplcjdsxxxb> listSpglDfxmsplcjdsxxxbByTimeRange(@Param("startTime") Date startTime, @Param("endTime") Date endTime);

    /**
     * 辅线事项查询
     */
    List<SpglDfxmsplcjdsxxxb> listSpglDfxmsplcjdfxsxxxbByTimeRange(@Param("startTime") Date startTime, @Param("endTime") Date endTime);
}