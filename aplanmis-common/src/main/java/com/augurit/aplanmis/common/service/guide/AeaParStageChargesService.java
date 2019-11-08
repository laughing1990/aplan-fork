package com.augurit.aplanmis.common.service.guide;

import com.augurit.aplanmis.common.domain.AeaParStageCharges;

import java.util.List;

public interface AeaParStageChargesService {
    /**
     *  根据阶段ID查询收费项目
     * @param stageId 必须参数 阶段ID
     * @return
     * @throws Exception
     */
    List<AeaParStageCharges> getAeaParStageChargesListByStageId(String stageId,String rootOrgId) throws Exception;
}
