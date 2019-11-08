package com.augurit.aplanmis.common.service.guide.impl;

import com.augurit.aplanmis.common.domain.AeaItemServiceWindowRel;
import com.augurit.aplanmis.common.mapper.AeaItemServiceWindowRelMapper;
import com.augurit.aplanmis.common.service.guide.AeaItemServiceWindowRelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.security.InvalidParameterException;
import java.util.List;

@Service
public class AeaItemServiceWindowRelServiceImpl implements AeaItemServiceWindowRelService {
    @Autowired
    private AeaItemServiceWindowRelMapper aeaItemServiceWindowRelMapper;


    @Override
    public List<AeaItemServiceWindowRel> listAeaItemServiceWindowRel(String stageId,String rootOrgId) throws Exception {
        if(org.apache.commons.lang.StringUtils.isBlank(stageId)) throw new InvalidParameterException("参数stageId为空！");
        AeaItemServiceWindowRel query=new AeaItemServiceWindowRel();
        query.setPkName("STAGE_ID");
        query.setTableName("AEA_PAR_STAGE");
        query.setRecordId(stageId);
        query.setRootOrgId(rootOrgId);
        return aeaItemServiceWindowRelMapper.listAeaItemServiceWindowRelAndWindowInfo(query);
    }

    @Override
    public List<AeaItemServiceWindowRel> listAeaItemServiceWindowRelByItemVerId(String itemVerId,String rootOrgId) throws Exception {
        if(org.apache.commons.lang.StringUtils.isBlank(itemVerId)) throw new InvalidParameterException("参数itemVerId为空！");
        AeaItemServiceWindowRel query=new AeaItemServiceWindowRel();
        query.setTableName("AEA_ITEM_VER");
        query.setPkName("ITEM_VER_ID");
        query.setRecordId(itemVerId);
        query.setRootOrgId(rootOrgId);
        return aeaItemServiceWindowRelMapper.listAeaItemServiceWindowRelAndWindowInfo(query);
    }

}
