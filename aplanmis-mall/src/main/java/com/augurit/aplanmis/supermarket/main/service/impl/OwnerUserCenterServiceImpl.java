package com.augurit.aplanmis.supermarket.main.service.impl;

import com.augurit.aplanmis.common.domain.AeaImProjPurchase;
import com.augurit.aplanmis.common.domain.AeaItemBasic;
import com.augurit.aplanmis.common.domain.AeaUnitInfo;
import com.augurit.aplanmis.common.mapper.AeaImProjPurchaseMapper;
import com.augurit.aplanmis.common.mapper.AeaItemBasicMapper;
import com.augurit.aplanmis.common.mapper.AeaUnitInfoMapper;
import com.augurit.aplanmis.supermarket.main.service.OwnerUserCenterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class OwnerUserCenterServiceImpl implements OwnerUserCenterService {
/*
    @Autowired
    private AeaProjServiceMapper aeaProjServiceMapper;*/

    @Autowired
    private AeaItemBasicMapper aeaItemBasicMapper;

    @Autowired
    private AeaImProjPurchaseMapper aeaImProjPurchaseMapper;

    @Autowired
    private AeaUnitInfoMapper aeaUnitInfoMapper;

    @Value("${dg.sso.access.platform.org.top-org-id}")
    private String topOrgId;

    @Override
    public List<AeaUnitInfo> listAeaUnitInfo(AeaUnitInfo aeaUnitInfo) {
        List<AeaUnitInfo> list = aeaUnitInfoMapper.listAeaUnitInfo(aeaUnitInfo);
        return list;
    }

    @Override
    public void saveAeaImProjPurchase(AeaImProjPurchase aeaImProjPurchase) throws Exception {
        aeaImProjPurchase.setRootOrgId(topOrgId);
        aeaImProjPurchaseMapper.insertAeaImProjPurchase(aeaImProjPurchase);
    }

    @Override
    public List<AeaImProjPurchase> listAeaImProjPurchase(AeaImProjPurchase aeaImProjPurchase) throws Exception {
        aeaImProjPurchase.setRootOrgId(topOrgId);
        List<AeaImProjPurchase> list = aeaImProjPurchaseMapper.listAeaImProjPurchase(aeaImProjPurchase);
        return list;
    }

    /*@Override
    public void saveAeaProjService(AeaProjService aeaProjService) throws Exception {
        aeaProjServiceMapper.insertAeaProjService(aeaProjService);
    }

    @Override
    public void updateAeaProjService(AeaProjService aeaProjService) throws Exception {
        aeaProjServiceMapper.updateAeaProjService(aeaProjService);
    }

    @Override
    public void deleteAeaProjServiceById(String id) throws Exception {
        if (id == null) {
            throw new InvalidParameterException(id);
        }
        aeaProjServiceMapper.deleteAeaProjServiceById(id);
    }

    @Override
    public AeaProjService getAeaProjServiceByProjInfoId(String projInfoId) throws Exception {
        if (projInfoId == null) {
            throw new InvalidParameterException(projInfoId);
        }
        return aeaProjServiceMapper.getAeaProjServiceByProjInfoId(projInfoId);
    }

    @Override
    public PageInfo<AeaProjService> listAeaProjServicePage(AeaProjService aeaProjService, Page page) throws Exception {
        PageHelper.startPage(page);
        List<AeaProjService> list = aeaProjServiceMapper.listAeaProjService(aeaProjService);
        return new PageInfo<AeaProjService>(list);
    }

    @Override
    public List<AeaProjService> listAeaProjService(AeaProjService aeaProjService) throws Exception {
        List<AeaProjService> list = aeaProjServiceMapper.listAeaProjService(aeaProjService);
        return list;
    }*/

    @Override
    public List<AeaItemBasic> listAeaItemBasicAll(String serviceTypeId) {
        List<AeaItemBasic> list = aeaItemBasicMapper.listAeaItemBasicAll(serviceTypeId);
        return list;
    }

    @Override
    public List<AeaItemBasic> listAeaItemBasicAll(AeaItemBasic aeaItemBasic) throws Exception {
        List<AeaItemBasic> list = aeaItemBasicMapper.listAeaItemBasic(aeaItemBasic);
        return list;
    }

    @Override
    public List<AeaImProjPurchase> listAeaImProjPurchaseForMyProj(AeaImProjPurchase aeaImProjPurchase) {
        List<AeaImProjPurchase> list = aeaImProjPurchaseMapper.listAeaImProjPurchaseForMyProj(aeaImProjPurchase);
        return list;
    }

    @Override
    public AeaImProjPurchase aeaImProjPurchaseForMyProj(AeaImProjPurchase aeaImProjPurchase) {
        AeaImProjPurchase ah = aeaImProjPurchaseMapper.aeaImProjPurchaseForMyProj(aeaImProjPurchase);
        return ah;
    }

    @Override
    public List<AeaUnitInfo> listUnit(String itemVerId) {
        List<AeaUnitInfo> unitList = aeaUnitInfoMapper.listUnit(itemVerId);
        return unitList;
    }


}

