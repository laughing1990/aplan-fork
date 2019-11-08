package com.augurit.aplanmis.common.service.guide;

import com.augurit.aplanmis.common.domain.AeaParStageGuide;

import javax.servlet.http.HttpServletRequest;

public interface AeaParStageGuideService {

    /**
     *  根据阶段ID查询并联办事指南信息
     * @param stageId
     * @return
     */
    AeaParStageGuide getAeaParStageGuideByStageId(String stageId,String rootOrgId);

    AeaParStageGuide getByStageId(String stageId, String rootOrgId);

    int saveAeaParStageGuide(AeaParStageGuide aeaParStageGuide ,HttpServletRequest request) throws Exception;

    int updateAeaParStageGuide(AeaParStageGuide aeaParStageGuide) throws Exception;

    void insertAeaParStageGuide(AeaParStageGuide aeaParStageGuide ,HttpServletRequest request) throws Exception;

    void saveAttFile(AeaParStageGuide aeaParStageGuide, HttpServletRequest request) throws Exception;

    void delStageGuideAttFile(String detailId, String guideId, String type) throws Exception;


}
