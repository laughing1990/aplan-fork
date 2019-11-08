package com.augurit.aplanmis.data.exchange.mapper.spgl;

import com.augurit.aplanmis.data.exchange.domain.spgl.SpglDfxmsplcjdsxxxb;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author yinlf
 * @date 2019/09/02
 * Copyright(c) 2013 广州奥格智能科技有限公司 版权所有
 */
@Mapper
@Repository
public interface SpglDfxmsplcjdsxxxbMapper {

    void insertSpglDfxmsplcjdsxxxb(SpglDfxmsplcjdsxxxb spglDfxmsplcjdsxxxb);

    void updateSpglDfxmsplcjdsxxxb(SpglDfxmsplcjdsxxxb spglDfxmsplcjdsxxxb);

    void deleteSpglDfxmsplcjdsxxxb(@Param("id") String id);

    List<SpglDfxmsplcjdsxxxb> listSpglDfxmsplcjdsxxxb(SpglDfxmsplcjdsxxxb spglDfxmsplcjdsxxxb);

    SpglDfxmsplcjdsxxxb getSpglDfxmsplcjdsxxxbById(@Param("id") String id);

    void batchInsertSpglDfxmsplcjdsxxxb(@Param("list") List<SpglDfxmsplcjdsxxxb> list);

    Integer findActiveSpglDfxmsplcjdsxxxbByUnique(@Param("splcbm") String splcbm, @Param("splcbbh") Double splcbbh, @Param("spsxbm") String spsxbm, @Param("spsxbbh") Double spsxbbh);
}