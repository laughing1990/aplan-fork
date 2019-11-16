package com.augurit.aplanmis.common.service.admin.oneform;

import com.augurit.agcloud.bpm.common.domain.ActStoForm;
import com.augurit.aplanmis.common.domain.AeaParStagePartform;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageInfo;

import java.util.List;

public interface AeaParStagePartformService {

    void saveStagePartform(AeaParStagePartform aeaParStagePartform);

    void updateStagePartform(AeaParStagePartform aeaParStagePartform);

    void deleteStagePartformById(String id);

    void batchDelStagePartformByIds(String[] ids);

    AeaParStagePartform getStagePartformById(String id);

    List<AeaParStagePartform> listStagePartform(AeaParStagePartform aeaParStagePartform);

    PageInfo<AeaParStagePartform> listStagePartform(AeaParStagePartform aeaParStagePartform, Page page);

    List<AeaParStagePartform> listStagePartformRelForm(AeaParStagePartform aeaPartform);

    PageInfo<AeaParStagePartform> listStagePartformRelForm(AeaParStagePartform aeaParStagePartform, Page page);

    Long getMaxSortNo(String stageId);

    List<AeaParStagePartform> listPartFormNoSelectForm(AeaParStagePartform partform);

    PageInfo<AeaParStagePartform> listPartFormNoSelectFormByPage(AeaParStagePartform partform, Page page);

    void createAndUpdateDevForm(String formCode, String formName, String formLoadUrl, String formId, String stagePartformId) throws Exception;

    ActStoForm getStageDevformByFormId(String formId);
}
