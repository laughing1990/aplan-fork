package com.augurit.aplanmis.common.service.admin.par;

import com.augurit.agcloud.framework.ui.pager.EasyuiPageInfo;
import com.augurit.agcloud.framework.ui.ztree.ZtreeNode;
import com.augurit.aplanmis.common.domain.AeaParStage;
import com.augurit.aplanmis.common.domain.InFormBill;
import com.augurit.aplanmis.common.vo.AppProcCaseDefPlusAdminVo;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageInfo;

import java.util.List;

/**
 * -Service服务调用接口类
 */
public interface AeaParStageAdminService {

    void saveAeaParStage(AeaParStage aeaParStage) throws Exception;

    void updateAeaParStage(AeaParStage aeaParStage);

    void deleteAeaParStageById(String id);

    void deleteAeaParStageByIds(String[] stageIds);

    PageInfo<AeaParStage> listAeaParStage(AeaParStage aeaParStage, Page page);

    AeaParStage getAeaParStageById(String id);

    List<AeaParStage> listAeaParStage(AeaParStage aeaParStage);

    List<AeaParStage> listStageByThemeId(String themeId) throws Exception;

    List<InFormBill> getPrintInFormBillData(String stageinstId) throws Exception;

    EasyuiPageInfo<AeaParStage> listAeaParStagePage(AeaParStage aeaParStage, Page page);

    //String synchronizedStageLinkState(String projInfoId, String stageId) throws Exception;

    List<ZtreeNode> syncListStageStateMat(String stageId);

    Long getMaxSortNoByThemeVerId(String themeVerId, String rootOrgId);

    List<AppProcCaseDefPlusAdminVo> listAppProcCaseDefVo(String stageId, String keyword) throws Exception;
}
