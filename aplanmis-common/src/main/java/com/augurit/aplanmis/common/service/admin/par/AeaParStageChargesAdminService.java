package com.augurit.aplanmis.common.service.admin.par;

import com.augurit.aplanmis.common.domain.AeaParStageCharges;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageInfo;

import java.util.List;

/**
* 阶段办事指南收费项目信息-Service服务调用接口类
*/
public interface AeaParStageChargesAdminService {

     void saveAeaParStageCharges(AeaParStageCharges aeaParStageCharges);
     void updateAeaParStageCharges(AeaParStageCharges aeaParStageCharges);
     void deleteAeaParStageChargesById(String id);
     PageInfo<AeaParStageCharges> listAeaParStageCharges(AeaParStageCharges aeaParStageCharges, Page page);
     AeaParStageCharges getAeaParStageChargesById(String id);
     List<AeaParStageCharges> listAeaParStageCharges(AeaParStageCharges aeaParStageCharges);
     Long getMaxSortNo(String stageId, String rootOrgId);
     void batchDelChargesByIds(String[] ids);
}
