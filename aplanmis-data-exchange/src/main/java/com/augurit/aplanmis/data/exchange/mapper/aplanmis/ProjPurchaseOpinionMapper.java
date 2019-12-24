package com.augurit.aplanmis.data.exchange.mapper.aplanmis;

import com.augurit.aplanmis.data.exchange.domain.spgl.SpglZjfwsxblgcxxb;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

/**
 * @author ylf_i
 * @date 2019/12/17
 * Copyright(c) 2013 广州奥格智能科技有限公司 版权所有
 */
@Mapper
@Repository
public interface ProjPurchaseOpinionMapper {

    SpglZjfwsxblgcxxb getSpglZjfwsxblgcxxbById(@Param("id") String id);

    List<SpglZjfwsxblgcxxb> listSpglZjfwsxblgcxxb(SpglZjfwsxblgcxxb spglZjfwsxblgcxxb);

    List<SpglZjfwsxblgcxxb> listSpglZjfwsxblgcxxbByTimeRange(@Param("startTime") Date startTime, @Param("endTime") Date endTime);
}