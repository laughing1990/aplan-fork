package com.augurit.aplanmis.mall.userCenter.service.impl;

import com.augurit.agcloud.bsc.domain.BscAttFileAndDir;
import com.augurit.agcloud.bsc.mapper.BscAttDetailMapper;
import com.augurit.agcloud.framework.security.SecurityContext;
import com.augurit.agcloud.framework.util.StringUtils;
import com.augurit.aplanmis.common.domain.AeaHiApplyinst;
import com.augurit.aplanmis.common.domain.AeaHiItemMatinst;
import com.augurit.aplanmis.common.domain.AeaHiIteminst;
import com.augurit.aplanmis.common.mapper.AeaHiApplyinstMapper;
import com.augurit.aplanmis.common.service.instance.AeaHiApplyinstService;
import com.augurit.aplanmis.common.service.instance.AeaHiItemMatinstService;
import com.augurit.aplanmis.common.service.instance.AeaHiIteminstService;
import com.augurit.aplanmis.common.service.instance.AeaHiParStageinstService;
import com.augurit.aplanmis.common.service.mat.AeaItemMatService;
import com.augurit.aplanmis.mall.userCenter.service.RestMyMatService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class RestMyMatServiceImpl implements RestMyMatService {

    @Autowired
    AeaHiApplyinstService aeaHiApplyinstService;
    @Autowired
    AeaHiIteminstService aeaHiIteminstService;
    @Autowired
    AeaHiItemMatinstService aeaHiItemMatinstService;
    @Autowired
    AeaHiParStageinstService aeaHiParStageinstService;
    @Autowired
    BscAttDetailMapper bscAttDetailMapper;
    @Autowired
    AeaHiApplyinstMapper aeaHiApplyinstMapper;


    public PageInfo<BscAttFileAndDir> getMyMatListByUser(String unitInfoId, String userInfoId, String keyword, int pageNum, int pageSize) throws Exception {

            //1.根据当前登录用户查询所有申请实例
            List<AeaHiApplyinst> applyinsts = aeaHiApplyinstMapper.listApplyInstListByUser(unitInfoId, userInfoId);
            if (applyinsts == null || applyinsts.size() == 0) return new PageInfo<>(new ArrayList<>());

            //2.根据申请实例查询所有事项实例
            List<AeaHiIteminst> iteminstList = new ArrayList<>();
            if (applyinsts == null || applyinsts.size() == 0) return new PageInfo<>();
            List<String> applyinstIds = applyinsts.stream().map(AeaHiApplyinst::getApplyinstId).collect(Collectors.toList());
            if (applyinstIds.size() == 0) return new PageInfo<>();
            iteminstList.addAll(aeaHiIteminstService.getAeaHiIteminstListByApplyinstIds(applyinstIds, null));
            if (iteminstList.size() == 0) return new PageInfo<>();
            List<String> iteminstIds = iteminstList.stream().map(AeaHiIteminst::getIteminstId).collect(Collectors.toList());

            //3.根据事项申请实例ID查询所有材料实例
            List<AeaHiItemMatinst> aeaHiItemMatinstList = aeaHiItemMatinstService.getMatinstListByIteminstIdsAndKeyword(iteminstIds.toArray(new String[iteminstIds.size()]), "1", null);
            if (aeaHiItemMatinstList.size() == 0) return new PageInfo<>(new ArrayList<>());
            List<String> matInstIds = aeaHiItemMatinstList.stream().map(AeaHiItemMatinst::getMatinstId).distinct().collect(Collectors.toList());
            String[] matinstArr = matInstIds.stream().toArray(String[]::new);
            //4.根据材料实例ID查询所有文件
            if (StringUtils.isBlank(keyword)) keyword = null;
            PageHelper.startPage(pageNum,pageSize);
            List<BscAttFileAndDir> list = bscAttDetailMapper.searchFileAndDirsSimple(keyword, SecurityContext.getCurrentOrgId(), "AEA_HI_ITEM_MATINST", "MATINST_ID", matinstArr);
                return new PageInfo<>(list);
        }
}
