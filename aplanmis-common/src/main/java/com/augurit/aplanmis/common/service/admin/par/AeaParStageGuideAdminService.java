package com.augurit.aplanmis.common.service.admin.par;

import com.augurit.aplanmis.common.domain.AeaParStageGuide;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * @author ZhangXinhui
 * @date 2019/7/9 009 17:04
 * @desc
 **/
public interface AeaParStageGuideAdminService {

    AeaParStageGuide getByStageId(String stageId, String rootOrgId);

    AeaParStageGuide getById(String recId) throws Exception;

    Map getById2(String recId) throws Exception;

    int saveAeaParStageGuide(AeaParStageGuide aeaParStageGuide, HttpServletRequest request) throws Exception;

    int updateAeaParStageGuide(AeaParStageGuide aeaParStageGuide) throws Exception;

    void insertAeaParStageGuide(AeaParStageGuide aeaParStageGuide, HttpServletRequest request) throws Exception;

    void saveAttFile(AeaParStageGuide aeaParStageGuide, HttpServletRequest request) throws Exception;

    void delStageGuideAttFile(String detailId, String guideId, String type) throws Exception;
}
