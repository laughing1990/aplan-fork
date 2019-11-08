package com.augurit.aplanmis.common.service.admin.credit;

import com.augurit.agcloud.framework.ui.elementui.ElementUiRsTreeNode;
import com.augurit.aplanmis.common.domain.AeaCreditUnitInfo;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageInfo;

import java.util.List;

/**
 * 单位表-Service服务调用接口类
 */
public interface AeaCreditUnitInfoService {

    void saveAeaCreditUnitInfo(AeaCreditUnitInfo aeaCreditUnitInfo);

    void updateAeaCreditUnitInfo(AeaCreditUnitInfo aeaCreditUnitInfo);

    void deleteAeaCreditUnitInfoById(String id);

    PageInfo<AeaCreditUnitInfo> listAeaCreditUnitInfo(AeaCreditUnitInfo aeaCreditUnitInfo, Page page);

    PageInfo<AeaCreditUnitInfo> listAeaCreditUnitInfoWithGlobalUnit(AeaCreditUnitInfo aeaCreditUnitInfo, Page page);

    AeaCreditUnitInfo getAeaCreditUnitInfoById(String id);

    List<AeaCreditUnitInfo> listAeaCreditUnitInfo(AeaCreditUnitInfo aeaCreditUnitInfo);

    List<ElementUiRsTreeNode> gtreeUnitInfoForEui(String keyword, String currentOrgId);

    void batchDelSelfAndAllChildById(String id);

    void batchDelSelfAndAllChildByIds(String[] ids);
}
