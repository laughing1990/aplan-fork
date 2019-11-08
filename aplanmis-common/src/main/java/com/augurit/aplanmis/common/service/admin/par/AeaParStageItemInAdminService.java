package com.augurit.aplanmis.common.service.admin.par;

import com.augurit.aplanmis.common.domain.AeaParStageItemIn;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageInfo;

import java.util.List;

/**
 * 阶段事项与输入关联定义表-Service服务调用接口类
 */
public interface AeaParStageItemInAdminService {

    void saveAeaParStageItemIn(AeaParStageItemIn aeaParStageItemIn);

    void updateAeaParStageItemIn(AeaParStageItemIn aeaParStageItemIn);

    void deleteAeaParStageItemInById(String id);

    PageInfo<AeaParStageItemIn> listAeaParStageItemIn(AeaParStageItemIn aeaParStageItemIn, Page page);

    AeaParStageItemIn getAeaParStageItemInById(String id);

    List<AeaParStageItemIn> listAeaParStageItemIn(AeaParStageItemIn aeaParStageItemIn);

    void batchSaveStageItemIn(String inId, String[] stageItemIds);

    void batchSaveStageItemInByStageItemId(String stageItemId, String[] inIds);

    void deleteAeaParStageItemInByStageItemId(String stageItemId);

    List<AeaParStageItemIn> listAeaParStageItemInByMatOrCertId(String matOrCertId,
                                                               String stageId,
                                                               String parStateId,
                                                               String fileType,
                                                               String isStateIn);
}
