package com.augurit.aplanmis.rest.userCenter.service.impl;

import com.augurit.aplanmis.common.domain.AeaHiApplyinst;
import com.augurit.aplanmis.common.domain.AeaHiItemMatinst;
import com.augurit.aplanmis.common.domain.AeaHiIteminst;
import com.augurit.aplanmis.common.domain.AeaHiParStageinst;
import com.augurit.aplanmis.common.service.instance.AeaHiApplyinstService;
import com.augurit.aplanmis.common.service.instance.AeaHiItemMatinstService;
import com.augurit.aplanmis.common.service.instance.AeaHiIteminstService;
import com.augurit.aplanmis.common.service.instance.AeaHiParStageinstService;
import com.augurit.aplanmis.rest.userCenter.service.RestMyMatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

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

    public List<AeaHiItemMatinst> getMyMatListByUser(String unitInfoId, String userInfoId) throws Exception {

        List<AeaHiItemMatinst> matinstList = new ArrayList<>();
        List<AeaHiApplyinst> applyinsts = aeaHiApplyinstService.listApplyInstIdAndStateByProjInfoIdAndUser("", "", unitInfoId, userInfoId);
        if (applyinsts == null || applyinsts.size() == 0) return matinstList;
        for (AeaHiApplyinst applyinst : applyinsts) {
            //AeaHiApplyinst applyinst = aeaHiApplyinstService.getAeaHiApplyinstById(applyinstId);
            if (applyinst == null) return matinstList;
            if ("1".equals(applyinst.getIsSeriesApprove())) {
                List<AeaHiIteminst> iteminsts = aeaHiIteminstService.getAeaHiIteminstListByApplyinstId(applyinst.getApplyinstId());
                if (iteminsts.size() == 0) return matinstList;
                String iteminstId = iteminsts.get(0).getIteminstId();
                matinstList.addAll(aeaHiItemMatinstService.getMatinstListByIteminstIds(new String[]{iteminstId}, "1"));
            } else {//并联
                AeaHiParStageinst stageinst = aeaHiParStageinstService.getAeaHiParStageinstByApplyinstId(applyinst.getApplyinstId());
                if (stageinst == null) return matinstList;
                matinstList.addAll(aeaHiItemMatinstService.getMatinstListByStageinstId(stageinst.getStageinstId(), "1"));
            }
        }
        return matinstList;
    }

}
