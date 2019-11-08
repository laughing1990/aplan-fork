package com.augurit.aplanmis.common.service.admin.credit;

import com.augurit.agcloud.framework.ui.elementui.ElementUiRsTreeNode;
import com.augurit.aplanmis.common.domain.AeaCreditRedblack;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageInfo;

import java.util.List;

/**
* 信用管理-红黑名单管理-Service服务调用接口类
*/
public interface AeaCreditRedblackService {

     void saveAeaCreditRedblack(AeaCreditRedblack aeaCreditRedblack) ;

     void updateAeaCreditRedblack(AeaCreditRedblack aeaCreditRedblack) ;

     void deleteAeaCreditRedblackById(String id) ;

     PageInfo<AeaCreditRedblack> listAeaCreditRedblack(AeaCreditRedblack aeaCreditRedblack, Page page) ;

     AeaCreditRedblack getAeaCreditRedblackById(String id) ;

     List<AeaCreditRedblack> listAeaCreditRedblack(AeaCreditRedblack aeaCreditRedblack) ;

     PageInfo<AeaCreditRedblack> listAeaCreditRedblackRelInfo(AeaCreditRedblack aeaCreditRedblack, Page page) ;

     List<AeaCreditRedblack> listAeaCreditRedblackRelInfo(AeaCreditRedblack aeaCreditRedblack);

    List<ElementUiRsTreeNode> gtreeLinkmanForEui(String keyword, String rootOrgId);

    List<ElementUiRsTreeNode> gtreeUnitInfoForEui(String keyword, String rootOrgId);

    void enOrDisableIsValid(String redblackId);

    List<AeaCreditRedblack> listPersonOrUnitBlackByBizId(String bizType, String bizId);

    List<AeaCreditRedblack> listPersonOrUnitBlackByBizCode(String bizType, String bizCode);
}
