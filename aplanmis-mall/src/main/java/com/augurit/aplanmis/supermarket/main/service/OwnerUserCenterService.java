package com.augurit.aplanmis.supermarket.main.service;

import com.augurit.aplanmis.common.domain.AeaImProjPurchase;
import com.augurit.aplanmis.common.domain.AeaItemBasic;
import com.augurit.aplanmis.common.domain.AeaUnitInfo;

import java.util.List;

public interface OwnerUserCenterService {
    //public void saveAeaProjService(AeaProjService aeaProjService) throws Exception;
    //public void updateAeaProjService(AeaProjService aeaProjService) throws Exception;
    //public void deleteAeaProjServiceById(String id) throws Exception;
    //public AeaProjService getAeaProjServiceByProjInfoId(String projInfoId) throws Exception;
    //public List<AeaProjService> listAeaProjService(AeaProjService aeaProjService) throws Exception;
    //public PageInfo<AeaProjService> listAeaProjServicePage(AeaProjService aeaProjService, Page page) throws Exception;
    public List<AeaItemBasic> listAeaItemBasicAll(String serviceTypeId);
    public List<AeaItemBasic> listAeaItemBasicAll(AeaItemBasic aeaItemBasic) throws Exception;

    public List<AeaImProjPurchase> listAeaImProjPurchase(AeaImProjPurchase aeaImProjPurchase) throws Exception;
    public void saveAeaImProjPurchase(AeaImProjPurchase aeaImProjPurchase) throws Exception;

    public List<AeaUnitInfo> listAeaUnitInfo(AeaUnitInfo aeaUnitInfo);

    public List<AeaImProjPurchase> listAeaImProjPurchaseForMyProj(AeaImProjPurchase aeaImProjPurchase);

    public AeaImProjPurchase aeaImProjPurchaseForMyProj(AeaImProjPurchase aeaImProjPurchase);
    public  List<AeaUnitInfo> listUnit(String itemVerId);

}
