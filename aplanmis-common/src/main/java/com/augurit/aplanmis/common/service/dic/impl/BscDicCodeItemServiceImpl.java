package com.augurit.aplanmis.common.service.dic.impl;

import com.augurit.agcloud.bsc.domain.BscDicCodeItem;
import com.augurit.agcloud.bsc.mapper.BscDicCodeMapper;
import com.augurit.agcloud.framework.security.SecurityContext;
import com.augurit.agcloud.framework.util.StringUtils;
import com.augurit.aplanmis.common.service.dic.BscDicCodeItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.security.InvalidParameterException;
import java.util.List;

@Service
public class BscDicCodeItemServiceImpl implements BscDicCodeItemService {
    @Autowired
    private BscDicCodeMapper bscDicCodeMapper;

    @Override
    public BscDicCodeItem getItemByTypeCodeAndItemCodeAndOrgId(String typeCode, String itemCode, String orgId) throws Exception {
        if(StringUtils.isBlank(typeCode)) throw new InvalidParameterException("参数typeCode为空！");
        if(StringUtils.isBlank(itemCode)) throw new InvalidParameterException("参数itemCode为空！");
        orgId= StringUtils.isBlank(orgId)?SecurityContext.getCurrentOrgId():orgId;
        return   bscDicCodeMapper.getItemByTypeCodeAndItemCodeAndOrgId(typeCode,itemCode,orgId);
    }

    @Override
    public List<BscDicCodeItem> getActiveItemsByTypeCode(String typeCode, String orgId) throws Exception {
        if(StringUtils.isBlank(typeCode)) throw new InvalidParameterException("参数typeCode为空！");
        orgId= StringUtils.isBlank(orgId)?SecurityContext.getCurrentOrgId():orgId;
        return  bscDicCodeMapper.getActiveItemsByTypeCode(typeCode, orgId);
    }

    @Override
    public List<BscDicCodeItem> getAllActiveItemsByTypeCode( String orgId) throws Exception{
        return bscDicCodeMapper.getAllItems(orgId);
    }
}
