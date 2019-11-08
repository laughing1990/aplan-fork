package com.augurit.aplanmis.data.exchange.mapper.spgl;

import com.augurit.aplanmis.data.exchange.domain.spgl.SpglDfxmsplcxxb;
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
public interface SpglDfxmsplcxxbMapper {

    void insertSpglDfxmsplcxxb(SpglDfxmsplcxxb spglDfxmsplcxxb);

    void updateSpglDfxmsplcxxb(SpglDfxmsplcxxb spglDfxmsplcxxb);

    void deleteSpglDfxmsplcxxb(@Param("id") String id);

    List<SpglDfxmsplcxxb> listSpglDfxmsplcxxb(SpglDfxmsplcxxb spglDfxmsplcxxb);

    SpglDfxmsplcxxb getSpglDfxmsplcxxbById(@Param("id") String id);

    void batchInsertSpglDfxmsplcxxb(@Param("list") List<SpglDfxmsplcxxb> list);

    Integer findActiveSpglDfxmsplcxxbByUnique(@Param("splcbm")String splcbm,@Param("splcbbh") Double splcbbh);
}