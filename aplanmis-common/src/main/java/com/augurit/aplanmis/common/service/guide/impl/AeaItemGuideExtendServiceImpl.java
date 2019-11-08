package com.augurit.aplanmis.common.service.guide.impl;

import com.augurit.aplanmis.common.domain.AeaItemGuideExtend;
import com.augurit.aplanmis.common.mapper.AeaItemGuideExtendMapper;
import com.augurit.aplanmis.common.service.guide.AeaItemGuideExtendService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AeaItemGuideExtendServiceImpl implements AeaItemGuideExtendService {
    @Autowired
    private AeaItemGuideExtendMapper aeaItemGuideExtendMapper;

    @Override
    public AeaItemGuideExtend getAeaItemGuideExtendByItemVerId(String itemVerId) throws Exception {
        AeaItemGuideExtend extend = new AeaItemGuideExtend();
        extend.setItemVerId(itemVerId);
        List<AeaItemGuideExtend> extendList=aeaItemGuideExtendMapper.listAeaItemGuideExtend(extend);
        if(extendList!=null && extendList.size()>0){
            return extendList.get(0);
        }else {
            return extend;
        }
    }
}
