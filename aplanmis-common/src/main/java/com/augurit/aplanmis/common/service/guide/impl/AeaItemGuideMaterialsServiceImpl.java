package com.augurit.aplanmis.common.service.guide.impl;

import com.augurit.aplanmis.common.domain.AeaItemGuideMaterials;
import com.augurit.aplanmis.common.mapper.AeaItemGuideMaterialsMapper;
import com.augurit.aplanmis.common.service.guide.AeaItemGuideMaterialsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.security.InvalidParameterException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class AeaItemGuideMaterialsServiceImpl implements AeaItemGuideMaterialsService {
    @Autowired
    private AeaItemGuideMaterialsMapper aeaItemGuideMaterialsMapper;

    @Override
    public List<AeaItemGuideMaterials> listAeaitemGuideMaterials(String itemVerId) throws Exception {
        if(org.apache.commons.lang.StringUtils.isBlank(itemVerId)) throw new InvalidParameterException("参数itemVerId为空！");
        AeaItemGuideMaterials materials = new AeaItemGuideMaterials();
        materials.setItemVerId(itemVerId);
        List<AeaItemGuideMaterials> materialsList = aeaItemGuideMaterialsMapper.listAeaItemGuideMaterials(materials);
        if(!materialsList.isEmpty()){
            Map<String,AeaItemGuideMaterials> map = new HashMap<>();
            for(AeaItemGuideMaterials aeaItemGuideMaterials:materialsList){
                String serviceCode = aeaItemGuideMaterials.getIntermediaryservicescode();
                if(map.containsKey(serviceCode)){
                    continue;
                }else{
                    map.put(serviceCode,aeaItemGuideMaterials);
                }
            }
            materialsList.clear();
            for(String key:map.keySet()){
                materialsList.add(map.get(key));
            }
        }
        return materialsList;
    }
}
