package com.augurit.aplanmis.common.service.admin.par;

import com.augurit.aplanmis.common.domain.AeaParFrontStage;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageInfo;

import java.util.List;

/**
 * 阶段的前置阶段检测表-Service服务调用接口类
 */
public interface AeaParFrontStageService {

    void saveAeaParFrontStage(AeaParFrontStage aeaParFrontStage) throws Exception;

    void updateAeaParFrontStage(AeaParFrontStage aeaParFrontStage) throws Exception;

    void deleteAeaParFrontStageById(String id) throws Exception;

    PageInfo<AeaParFrontStage> listAeaParFrontStage(AeaParFrontStage aeaParFrontStage, Page page) throws Exception;

    AeaParFrontStage getAeaParFrontStageById(String id) throws Exception;

    List<AeaParFrontStage> listAeaParFrontStage(AeaParFrontStage aeaParFrontStage) throws Exception;

    PageInfo<AeaParFrontStage> listAeaParFrontStageVoByPage(AeaParFrontStage aeaParFrontStage, Page page) throws Exception;

    Long getMaxSortNo(String stageId, String rootOrgId) throws Exception;

    AeaParFrontStage getAeaParFrontStageVoById(String frontStageId) throws Exception;

    List<AeaParFrontStage> listSelectParFrontStage(AeaParFrontStage aeaParFrontStage) throws Exception;

    List<AeaParFrontStage> getHistStageByStageId(String stageId);

    PageInfo<AeaParFrontStage> listSelectParFrontStageByPage(AeaParFrontStage aeaParFrontStage,Page page) throws Exception;

    void batchSaveAeaParFrontStage(String stageId, String[] histStageIds)throws Exception;

    List<AeaParFrontStage> listAeaParFrontStageVoByNoPage(AeaParFrontStage aeaParFrontStage) throws Exception;

    void changIsActive(String id, String rootOrgId);
}
