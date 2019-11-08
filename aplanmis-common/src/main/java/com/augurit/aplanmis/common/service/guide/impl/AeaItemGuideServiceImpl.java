package com.augurit.aplanmis.common.service.guide.impl;

import com.augurit.aplanmis.common.domain.AeaItemGuide;
import com.augurit.aplanmis.common.mapper.AeaItemGuideMapper;
import com.augurit.aplanmis.common.service.guide.AeaItemGuideService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.security.InvalidParameterException;
import java.util.List;

@Service
public class AeaItemGuideServiceImpl implements AeaItemGuideService {
    @Autowired
    private AeaItemGuideMapper aeaItemGuideMapper;


    @Override
    public AeaItemGuide getAeaItemGuideListByItemVerId(String itemVerId,String topOrgId) throws Exception {
        if(org.apache.commons.lang.StringUtils.isBlank(itemVerId)) throw new InvalidParameterException("参数itemVerId为空！");
        AeaItemGuide guide = new AeaItemGuide();
        guide.setItemVerId(itemVerId);
        guide.setRootOrgId(topOrgId);
        List<AeaItemGuide> list = aeaItemGuideMapper.listAeaItemGuide(guide);
        if(list!=null && list.size()>0){
            return list.get(0);
        }else{
            return guide;
        }
    }
}
