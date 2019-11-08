package com.augurit.aplanmis.common.service.guide.impl;

import com.augurit.agcloud.framework.security.SecurityContext;
import com.augurit.aplanmis.common.domain.AeaParStageCharges;
import com.augurit.aplanmis.common.mapper.AeaParStageChargesMapper;
import com.augurit.aplanmis.common.service.guide.AeaParStageChargesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.security.InvalidParameterException;
import java.util.List;

@Service
public class AeaParStageChargesServiceImpl implements AeaParStageChargesService {
    @Autowired
    private AeaParStageChargesMapper aeaParStageChargesMapper;


    @Override
    public List<AeaParStageCharges> getAeaParStageChargesListByStageId(String stageId,String rootOrgId) throws Exception {
        if(org.apache.commons.lang.StringUtils.isBlank(stageId)) throw new InvalidParameterException("参数stageId为空！");
        AeaParStageCharges query=new AeaParStageCharges();
        query.setStageId(stageId);
        query.setRootOrgId(rootOrgId);
        return aeaParStageChargesMapper.listAeaParStageCharges(query);
    }
}
