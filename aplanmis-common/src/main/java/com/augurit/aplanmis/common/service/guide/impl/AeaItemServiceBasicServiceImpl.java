package com.augurit.aplanmis.common.service.guide.impl;

import com.augurit.aplanmis.common.domain.AeaItemBasic;
import com.augurit.aplanmis.common.domain.AeaItemServiceBasic;
import com.augurit.aplanmis.common.mapper.AeaItemServiceBasicMapper;
import com.augurit.aplanmis.common.service.guide.AeaItemServiceBasicService;
import com.augurit.aplanmis.common.service.item.AeaItemBasicService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class AeaItemServiceBasicServiceImpl implements AeaItemServiceBasicService {
    @Autowired
    private AeaItemServiceBasicMapper aeaItemServiceBasicMapper;
    @Autowired
    private AeaItemBasicService aeaItemBasicService;

    @Override
    public List<AeaItemServiceBasic> getAeaItemServiceBasicListByStageId(String stageId,String rootOrgId) throws Exception {
        if (org.apache.commons.lang.StringUtils.isBlank(stageId)) throw new InvalidParameterException("参数stageId为空！");
        AeaItemServiceBasic query = new AeaItemServiceBasic();
        query.setPkName("STAGE_ID");
        query.setTableName("AEA_PAR_STAGE");
        query.setRecordId(stageId);
        query.setRootOrgId(rootOrgId);
        return aeaItemServiceBasicMapper.listAeaItemServiceBasicAndClauseContent(query);
    }

    @Override
    public List<Map> getSubAeaItemServiceBasicListByStageId(String stageId, String rootOrgId) throws Exception {
        if (org.apache.commons.lang.StringUtils.isBlank(stageId)) throw new InvalidParameterException("参数stageId为空！");
        List<Map> list = new ArrayList<>();
        List<AeaItemBasic> itemList = aeaItemBasicService.getAeaItemBasicListByStageId(stageId, null, null, rootOrgId);
        if (itemList.size() > 0) {
            for (AeaItemBasic aeaItemBasic : itemList) {
                String itemVerId = "";
                if ("1".equals(aeaItemBasic.getIsCatalog())) {//标准事项
                    //找到市级的实施事项,暂取第一条
                    List<AeaItemBasic> ssxxList = aeaItemBasicService.getSssxByItemIdAndRegionalism(aeaItemBasic.getItemId(), null, null, rootOrgId);
                    if (ssxxList.size() > 0) {
                        itemVerId = ssxxList.get(0).getItemVerId();
                    } else {
                        itemVerId = aeaItemBasic.getItemVerId();
                    }
                } else {
                    itemVerId = aeaItemBasic.getItemVerId();
                }
                Map<String, Object> map = new HashMap<>();
                AeaItemServiceBasic query = new AeaItemServiceBasic();
                query.setPkName("ITEM_VER_ID");
                query.setTableName("AEA_ITEM_VER");
                query.setRecordId(itemVerId);
                query.setRootOrgId(rootOrgId);
                map.put("itemName", aeaItemBasic.getItemName());
                map.put("list", aeaItemServiceBasicMapper.listAeaItemServiceBasicAndClauseContent(query));
                list.add(map);
            }
        }
        return list;
    }

    @Override
    public List<AeaItemServiceBasic> listAeaItemServiceBasicByitemVerId(String itemVerId,String rootOrgId) throws Exception {
        if (org.apache.commons.lang.StringUtils.isBlank(itemVerId))
            throw new InvalidParameterException("参数itemVerId为空！");
        AeaItemServiceBasic query = new AeaItemServiceBasic();
        query.setPkName("ITEM_VER_ID");
        query.setTableName("AEA_ITEM_VER");
        query.setRecordId(itemVerId);
        query.setRootOrgId(rootOrgId);
        return aeaItemServiceBasicMapper.listAeaItemServiceBasicAndClauseContent(query);
    }
}
