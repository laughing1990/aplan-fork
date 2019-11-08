package com.augurit.aplanmis.admin.market.purchase.service;

import com.augurit.aplanmis.admin.market.purchase.vo.UnitRequireQualVo;
import com.augurit.aplanmis.common.domain.AeaImProjPurchase;
import com.augurit.aplanmis.common.vo.AeaImProjPurchaseDetailVo;
import com.github.pagehelper.Page;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
* -Service服务调用接口类
*/
@Repository
public interface PurchaseService {
    public List<AeaImProjPurchase> listAeaImProPurchase(AeaImProjPurchase aeaImProjPurchase, Page page) throws Exception;

    public AeaImProjPurchaseDetailVo getAeaImProPurchase(String projPurchaseId) throws Exception;

    public void audit(AeaImProjPurchase aeaImProjPurchase) throws Exception;

    public List<UnitRequireQualVo> getMajorTreeByUnitRequireId(String unitRequireId) throws Exception;
}