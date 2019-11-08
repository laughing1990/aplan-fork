package com.augurit.aplanmis.common.service.admin.approve.Impl;


import com.augurit.agcloud.framework.security.SecurityContext;
import com.augurit.aplanmis.common.domain.*;
import com.augurit.aplanmis.common.mapper.AeaItemBasicMapper;
import com.augurit.aplanmis.common.mapper.AeaParStageMapper;
import com.augurit.aplanmis.common.service.admin.approve.DgAeaApproveService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Transactional
@Service
public class DgAeaApproveServiceImpl implements DgAeaApproveService {

    private static Logger logger = LoggerFactory.getLogger(DgAeaApproveServiceImpl.class);
    @Autowired
    private AeaParStageMapper aeaParStageMapper;
    @Autowired
    private AeaItemBasicMapper aeaItemBasicMapper;

    @Override
    public Map<String, Object> getAeaStateAndItemsByStageId(String itemVerId, String currentBusiType) {
        Map<String, Object> map = new HashMap<>();
        if ("stage".equals(currentBusiType)) {
            AeaParStage parStage = aeaParStageMapper.getAeaParStageById(itemVerId);
            //20190315 修改为事项版本
            List<AeaItemBasic> aeaItemBasics = aeaItemBasicMapper.listAeaItemBasicByStageId(new AeaItemBasic(), itemVerId);
            //List<AeaItem> items = aeaItemMapper.listAeaItemByStageId(new AeaItem(), currentBusiId);
            map.put("stage", parStage);
            map.put("items", aeaItemBasics);
        } else if ("item".equals(currentBusiType)) {
            List<AeaItemBasic> items = new ArrayList<>();
            items.add(aeaItemBasicMapper.getOneByItemVerId(itemVerId, SecurityContext.getCurrentOrgId()));
            map.put("items", items);
        }
        return map;
    }


}
