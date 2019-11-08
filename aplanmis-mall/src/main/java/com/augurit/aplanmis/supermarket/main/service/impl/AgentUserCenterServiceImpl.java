package com.augurit.aplanmis.supermarket.main.service.impl;

import com.augurit.agcloud.framework.ui.pager.PageHelper;
import com.augurit.aplanmis.common.domain.*;
import com.augurit.aplanmis.common.mapper.*;
import com.augurit.aplanmis.supermarket.main.service.AgentUserCenterService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class AgentUserCenterServiceImpl implements AgentUserCenterService {
    private static Logger logger = LoggerFactory.getLogger(AgentUserCenterServiceImpl.class);

    @Autowired
    private AeaImServiceMapper aeaServiceTypeMapper;
    @Autowired
    private AeaImUnitServiceMapper aeaImUnitServiceMapper;
    @Autowired
    private AeaItemBasicMapper aeaItemBasicMapper;
    @Autowired
    private AeaImServiceMapper aeaImServiceMapper;
    @Autowired
    private AeaProjInfoMapper aeaProjInfoMapper;

    @Autowired
    private AeaImProjPurchaseMapper aeaImProjPurchaseMapper;
    @Autowired
    private AeaImUnitBiddingMapper aeaImUnitBiddingMapper;

    @Value("${dg.sso.access.platform.org.top-org-id}")
    protected String topOrgId;


    @Override
    public PageInfo<AeaImUnitBidding> listAeaUnitBidding(AeaImUnitBidding aeaImUnitBidding, Page page) throws Exception {
        aeaImUnitBidding.setRootOrgId(topOrgId);
        PageHelper.startPage(page);
        //List<AeaUnitBidding> list = aeaUnitBiddingMapper.listAeaUnitBidding(aeaUnitBidding);
        List<AeaImUnitBidding> list = aeaImUnitBiddingMapper.listAeaImUnitBidding(aeaImUnitBidding);
        return new PageInfo<AeaImUnitBidding>(list);
    }

    @Override
    public List<AeaImService> listAeaServiceType(AeaImService aeaServiceType) throws Exception {
        aeaServiceType.setRootOrgId(topOrgId);
        return aeaServiceTypeMapper.listAeaImService(aeaServiceType);
    }

    @Override
    public List<AeaImUnitService> listAeaImUnitService(AeaImUnitService aeaImUnitService) throws Exception {
        aeaImUnitService.setRootOrgId(topOrgId);
        return aeaImUnitServiceMapper.listAeaImUnitService(aeaImUnitService);
    }

    @Override
    public AeaImUnitService getAeaImUnitService(AeaImUnitService aeaImUnitService) throws Exception {
        return aeaImUnitServiceMapper.getAeaImUnitService(aeaImUnitService);
    }

    @Override
    public PageInfo<AeaImUnitService> listAeaImUnitServicePage(AeaImUnitService aeaImUnitService, Page page) throws Exception {
        aeaImUnitService.setRootOrgId(topOrgId);
        PageHelper.startPage(page);
        List<AeaImUnitService> list = aeaImUnitServiceMapper.listAeaImUnitService(aeaImUnitService);
        return new PageInfo<AeaImUnitService>(list);
    }

    @Override
    public void insertAeaImUnitService(AeaImUnitService aeaImUnitService) throws Exception {
        aeaImUnitService.setRootOrgId(topOrgId);
        aeaImUnitServiceMapper.insertAeaImUnitService(aeaImUnitService);
    }

    @Override
    public void updateAeaImUnitService(AeaImUnitService aeaImUnitService) throws Exception {
        aeaImUnitServiceMapper.updateAeaImUnitService(aeaImUnitService);
    }

    @Override
    public void deleteAeaImUnitService(String id) throws Exception {
        aeaImUnitServiceMapper.deleteAeaImUnitService(id);
    }

    @Override
    public List<AeaItemBasic> getListAeaItemBasic(AeaItemBasic aeaItemBasic) throws Exception {
        List<AeaItemBasic> list = aeaItemBasicMapper.listAeaItemBasic(aeaItemBasic);
        return list;
    }

    @Override
    public List<AeaImService> getAeaImServiceTypeList(AeaImService aeaImService) throws Exception {
        aeaImService.setRootOrgId(topOrgId);
        List<AeaImService> list = aeaImServiceMapper.listAeaImService(aeaImService);
        return list;
    }

    @Override
    public List<AeaImProjPurchase> listCanjoin() {
        List<AeaImProjPurchase> canJoinList = aeaImProjPurchaseMapper.listCanjoin();
        return canJoinList;
    }

    @Override
    public AeaImProjPurchase listDetail(String projServiceId) {
        AeaImProjPurchase projDetailList = aeaImProjPurchaseMapper.listDetail(projServiceId);
        return projDetailList;
    }

    @Override
    public List<AeaImUnitBidding> listJoining() {
        List<AeaImUnitBidding> joiningList = aeaImUnitBiddingMapper.listJoining();
        return joiningList;
    }

    @Override
    public AeaImProjPurchase listbid(String projServiceId) {
        AeaImProjPurchase projDetailList = aeaImProjPurchaseMapper.listbid(projServiceId);
        return projDetailList;
    }


    @Override
    public void updateAeaImUnitBidding(AeaImUnitBidding aeaImUnitBidding) throws Exception {
        aeaImUnitBiddingMapper.updateAeaImUnitBidding(aeaImUnitBidding);
    }


    /*@Override
    public AeaImUnitService getAeaImUnitServiceForm(AeaImUnitService aeaImUnitService) throws Exception {
        return aeaImUnitServiceMapper.getAeaImUnitServiceForm(aeaImUnitService);
    }*/


    @Override
    public void insertAeaImUnitBidding(AeaImUnitBidding aeaImUnitBidding) throws Exception {
        aeaImUnitBidding.setRootOrgId(topOrgId);
        aeaImUnitBiddingMapper.insertAeaImUnitBidding(aeaImUnitBidding);
    }

    @Override
    public List<AeaImUnitBidding> listAeaImUnitBidding(AeaImUnitBidding aeaImUnitBidding) throws Exception {
        aeaImUnitBidding.setRootOrgId(topOrgId);
        List<AeaImUnitBidding> list = aeaImUnitBiddingMapper.listAeaImUnitBidding(aeaImUnitBidding);
        return list;
    }

    @Override
    public void insertOfferPrice(AeaImUnitBidding aeaImUnitBidding) throws Exception {
        aeaImUnitBidding.setRootOrgId(topOrgId);
        aeaImUnitBiddingMapper.insertAeaImUnitBidding(aeaImUnitBidding);
    }


}