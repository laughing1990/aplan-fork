package com.augurit.aplanmis.data.exchange.mapper.aplanmis;

import com.augurit.aplanmis.data.exchange.domain.spgl.SpglXmjbxxb;
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
public interface ApplyProjMapper {

    List<SpglXmjbxxb> listSpglXmjbxxb(SpglXmjbxxb spglXmjbxxb);

    SpglXmjbxxb getSpglXmjbxxbByDfsjzj(@Param("dfsjzj") String dfsjzj);

    List<SpglXmjbxxb> getSpglXmjbxxbByXzqhdmAndGcdM(@Param("xzqhdm") String xzqhdm, @Param("gcdm") String gcdm);

    List<SpglXmjbxxb> listSpglXmjbxxbByTimeRange(@Param("startTime") Date startTime, @Param("endTime") Date endTime);
}