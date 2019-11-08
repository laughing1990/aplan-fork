package com.augurit.aplanmis.supermarket.main.service;

import com.augurit.aplanmis.common.domain.*;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageInfo;

import java.util.List;

public interface AgentUserCenterService {

    PageInfo<AeaImUnitBidding> listAeaUnitBidding(AeaImUnitBidding aeaImUnitBidding, Page page) throws Exception;

    List<AeaImService> listAeaServiceType(AeaImService aeaServiceType) throws Exception;

    AeaImUnitService getAeaImUnitService(AeaImUnitService aeaImUnitService) throws Exception;

    List<AeaImUnitService> listAeaImUnitService(AeaImUnitService AeaImUnitService) throws Exception;

    PageInfo<AeaImUnitService> listAeaImUnitServicePage(AeaImUnitService AeaImUnitService, Page page) throws Exception;

    void insertAeaImUnitService(AeaImUnitService aeaImUnitService) throws Exception;

    void updateAeaImUnitService(AeaImUnitService aeaImUnitService) throws Exception;

    void deleteAeaImUnitService(String id) throws Exception;

    List<AeaItemBasic> getListAeaItemBasic(AeaItemBasic aeaItemBasic) throws Exception;

    List<AeaImService> getAeaImServiceTypeList(AeaImService aeaImService) throws Exception;

    void insertAeaImUnitBidding(AeaImUnitBidding aeaImUnitBidding) throws Exception;

    List<AeaImUnitBidding> listAeaImUnitBidding(AeaImUnitBidding aeaImUnitBidding) throws Exception;

    void insertOfferPrice(AeaImUnitBidding aeaImUnitBidding) throws Exception;

    void updateAeaImUnitBidding(AeaImUnitBidding aeaImUnitBidding) throws Exception;

    /*AeaImUnitService getAeaImUnitServiceForm(AeaImUnitService aeaImUnitService) throws Exception;*/

    List<AeaImProjPurchase> listCanjoin();

    AeaImProjPurchase listDetail(String projServiceId);

    List<AeaImUnitBidding> listJoining();

    AeaImProjPurchase listbid(String projServiceId);
}