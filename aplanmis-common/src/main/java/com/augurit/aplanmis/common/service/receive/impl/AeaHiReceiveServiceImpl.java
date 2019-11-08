package com.augurit.aplanmis.common.service.receive.impl;

import com.augurit.agcloud.framework.security.SecurityContext;
import com.augurit.agcloud.framework.util.StringUtils;
import com.augurit.aplanmis.common.domain.AeaHiReceive;
import com.augurit.aplanmis.common.mapper.AeaHiReceiveMapper;
import com.augurit.aplanmis.common.service.receive.AeaHiReceiveService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class AeaHiReceiveServiceImpl implements AeaHiReceiveService {

    @Autowired
    private AeaHiReceiveMapper aeaHiReceiveMapper;


    @Override
    public AeaHiReceive getAeaHiReceiveByApplyinstIdAndReceiveType(String applyinstId, String receiptType) throws Exception {
        if(StringUtils.isBlank(applyinstId)) throw new InvalidParameterException("参数applyinstId为空！");
        if(StringUtils.isBlank(receiptType)) throw new InvalidParameterException("参数receiptType为空！");
        AeaHiReceive query=new AeaHiReceive();
        query.setApplyinstId(applyinstId);
        query.setReceiptType(receiptType);
        query.setRootOrgId(SecurityContext.getCurrentOrgId());
        List<AeaHiReceive> aeaHiReceives = aeaHiReceiveMapper.listAeaHiReceive(query);
        if(aeaHiReceives.size()>0){
            return aeaHiReceives.get(0);
        }
        return null;
    }

    @Override
    public List<AeaHiReceive> getReceiveListByApplyinstId(String applyinstId) throws Exception {
        if(StringUtils.isBlank(applyinstId)) throw new InvalidParameterException("参数applyinstId为空！");
        AeaHiReceive query = new AeaHiReceive();
        query.setApplyinstId(applyinstId);
        query.setRootOrgId(SecurityContext.getCurrentOrgId());
        return aeaHiReceiveMapper.listAeaHiReceive(query);
    }

    @Override
    public void saveAeaHiReceive(AeaHiReceive aeaHiReceive) throws Exception {
        if(StringUtils.isBlank(aeaHiReceive.getReceiveId())){
            aeaHiReceive.setReceiveId(UUID.randomUUID().toString());
            aeaHiReceive.setRootOrgId(SecurityContext.getCurrentOrgId());
            aeaHiReceiveMapper.insertAeaHiReceive(aeaHiReceive);
        }else{
            aeaHiReceiveMapper.updateAeaHiReceive(aeaHiReceive);
        }
    }

    @Override
    public void deleteAeaHiReceive(String id) throws Exception {
        if(StringUtils.isBlank(id)) throw new InvalidParameterException("参数id为空！");
        aeaHiReceiveMapper.deleteAeaHiReceive(id);
    }

    @Override
    public List<AeaHiReceive> listAeaHiReceive(AeaHiReceive aeaHiReceive) throws Exception {
        aeaHiReceive.setRootOrgId(SecurityContext.getCurrentOrgId());
        return aeaHiReceiveMapper.listAeaHiReceive(aeaHiReceive);
    }

    @Override
    public AeaHiReceive getAeaHiReceiveById(String id) throws Exception {
        if(StringUtils.isBlank(id)) throw new InvalidParameterException("参数id为空！");
        return aeaHiReceiveMapper.getAeaHiReceiveById(id);
    }

    @Override
    public PageInfo<AeaHiReceive> listAeaHiReceive(AeaHiReceive aeaHiReceive, Page page) throws Exception {
        aeaHiReceive.setRootOrgId(SecurityContext.getCurrentOrgId());
        PageHelper.startPage(page);
        List<AeaHiReceive> list=aeaHiReceiveMapper.listAeaHiReceive(aeaHiReceive);
        return new PageInfo<>(list);
    }
}
