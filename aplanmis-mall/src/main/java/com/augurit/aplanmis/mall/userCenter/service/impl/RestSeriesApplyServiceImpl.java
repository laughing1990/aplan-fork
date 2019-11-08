package com.augurit.aplanmis.mall.userCenter.service.impl;

import com.augurit.agcloud.framework.security.SecurityContext;
import com.augurit.agcloud.framework.util.StringUtils;
import com.augurit.aplanmis.common.domain.AeaItemBasic;
import com.augurit.aplanmis.common.domain.AeaItemCond;
import com.augurit.aplanmis.common.domain.AeaItemState;
import com.augurit.aplanmis.common.domain.AeaProjInfo;
import com.augurit.aplanmis.common.service.item.AeaItemBasicService;
import com.augurit.aplanmis.common.service.item.AeaItemCondService;
import com.augurit.aplanmis.common.service.project.AeaProjInfoService;
import com.augurit.aplanmis.common.service.state.AeaItemStateService;
import com.augurit.aplanmis.mall.main.vo.BaseInfoVo;
import com.augurit.aplanmis.mall.userCenter.service.RestSeriesApplyService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class RestSeriesApplyServiceImpl implements RestSeriesApplyService {

    @Autowired
    AeaItemBasicService aeaItemBasicService;
    @Autowired
    AeaProjInfoService aeaProjInfoService;
    @Autowired
    AeaItemCondService aeaItemCondService;
    @Autowired
    AeaItemStateService aeaItemStateService;

    @Override
    public BaseInfoVo getBaseInfoByItemVerIdAndProjInfoId(String itemVerId, String projInfoId) throws Exception {
        BaseInfoVo vo = new BaseInfoVo();
        AeaProjInfo projInfo = aeaProjInfoService.getAeaProjInfoByProjInfoId(projInfoId);
        if(projInfo==null) return vo;
        vo.setAeaProjInfo(projInfo);
        AeaItemBasic itemBasic = aeaItemBasicService.getAeaItemBasicByItemVerId(itemVerId);
        if(itemBasic!=null){
            vo.setIsNeedState(itemBasic.getIsNeedState());
            vo.setIsNeedFrontCond(itemBasic.getIsNeedFrontCond());
            vo.setItemVerId(itemBasic.getItemVerId());
            vo.setItemName(itemBasic.getItemName());
        }
//        AeaItemBasic itemBasic = aeaItemBasicService.getAeaItemBasicByItemVerId(itemVerId);
//        if(itemBasic==null) return vo;
//        String flag=itemBasic.getIsCatalog();
//        String itemName=itemBasic.getItemName();
//        String itemVerId1=itemBasic.getItemVerId();
//        if("1".equals(flag)){//如果是标准事项，需查出实施事项
//            List<AeaItemBasic> list = aeaItemBasicService.getSssxByItemIdAndRegionalism(itemBasic.getItemId(), projInfo.getRegionalism(), null, SecurityContext.getCurrentOrgId());
//            if(list.size()>0) {
//                itemBasic=list.get(0);
//            }
//            itemBasic.setBaseItemName(itemName);
//            itemBasic.setBaseItemVerId(itemVerId1);
//        }
//        BeanUtils.copyProperties(itemBasic,vo);
//        itemBasic.setIsCatalog(flag);
        return vo;
    }

    @Override
    public List<AeaItemCond> getAeaItemCondTopListByItemVerId(String itemVerId) throws Exception {
        AeaItemBasic aeaItemBasic = aeaItemBasicService.getAeaItemBasicByItemVerId(itemVerId);
        if (StringUtils.isBlank(aeaItemBasic.getIsNeedFrontCond())|| "0".equals(aeaItemBasic.getIsNeedFrontCond())) return new ArrayList<>();
        return aeaItemCondService.getAeaItemCondTopListByItemVerId(itemVerId);
    }

    @Override
    public List<AeaItemCond> getTreeCondByItemVerId(String itemVerId) throws Exception {
        AeaItemBasic aeaItemBasic = aeaItemBasicService.getAeaItemBasicByItemVerId(itemVerId);
        if (StringUtils.isBlank(aeaItemBasic.getIsNeedFrontCond())|| "0".equals(aeaItemBasic.getIsNeedFrontCond())) return new ArrayList<>();
        return aeaItemCondService.getAeaItemCondTreeListByItemVerId(itemVerId);
    }

    @Override
    public List<AeaItemState> listRootAeaItemStateByItemVerId(String itemVerId) throws Exception {
        AeaItemBasic aeaItemBasic = aeaItemBasicService.getAeaItemBasicByItemVerId(itemVerId);
        if ("0".equals(aeaItemBasic.getIsNeedState())) return new ArrayList<>();
        return aeaItemStateService.listAeaItemStateByParentId(itemVerId,"","ROOT", SecurityContext.getCurrentOrgId());
    }


}
