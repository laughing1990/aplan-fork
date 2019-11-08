package com.augurit.aplanmis.common.service.guide.impl;

import com.augurit.aplanmis.common.domain.AeaItemGuideCharges;
import com.augurit.aplanmis.common.mapper.AeaItemGuideChargesMapper;
import com.augurit.aplanmis.common.service.guide.AeaItemGuideChargesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.security.InvalidParameterException;
import java.util.List;

@Service
public class AeaItemGuideChargesServiceImpl implements AeaItemGuideChargesService {
    @Autowired
    private AeaItemGuideChargesMapper aeaItemGuideChargesMapper;

    @Override
    public List<AeaItemGuideCharges> getAeaItemGuideChargesListByItemVerId(String itemVerId) throws Exception {
        if(org.apache.commons.lang.StringUtils.isBlank(itemVerId)) throw new InvalidParameterException("参数itemVerId为空！");
        AeaItemGuideCharges query=new AeaItemGuideCharges();
        query.setItemVerId(itemVerId);
        return aeaItemGuideChargesMapper.listAeaItemGuideCharges(query);
    }
}
