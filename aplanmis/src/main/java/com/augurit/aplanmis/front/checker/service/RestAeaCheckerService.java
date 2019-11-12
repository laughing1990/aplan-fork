package com.augurit.aplanmis.front.checker.service;

import com.augurit.agcloud.framework.security.SecurityContext;
import com.augurit.agcloud.framework.ui.result.ResultForm;
import com.augurit.agcloud.framework.util.StringUtils;
import com.augurit.aplanmis.common.check.CheckerContext;
import com.augurit.aplanmis.common.check.CheckerInfo;
import com.augurit.aplanmis.common.check.CheckerManager;
import com.augurit.aplanmis.common.domain.AeaItemBasic;
import com.augurit.aplanmis.common.domain.AeaParStage;
import com.augurit.aplanmis.common.mapper.AeaItemBasicMapper;
import com.augurit.aplanmis.common.mapper.AeaParStageMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@Slf4j
public class RestAeaCheckerService {

    @Autowired
    private AeaItemBasicMapper aeaItemBasicMapper;

    @Autowired
    private CheckerManager checkerManager;

    @Autowired
    private AeaParStageMapper aeaParStageMapper;

    public ResultForm itemFrontCheck(String itemVerId, String projInfoId) throws Exception {
        AeaItemBasic aeaItemBasic = aeaItemBasicMapper.getAeaItemBasicByItemVerId(itemVerId, SecurityContext.getCurrentOrgId());
        CheckerInfo checkerInfo = new CheckerInfo();
        checkerInfo.setProjInfoId(projInfoId);
        String message = checkerManager.itemCheck(aeaItemBasic, new CheckerContext(checkerInfo));
        return StringUtils.isBlank(message) ? new ResultForm(true, "前置条件检查通过！") : new ResultForm(false, message);
    }

    public ResultForm stageFrontCheck(String stageId, String projInfoId) throws Exception {
        AeaParStage parStage = aeaParStageMapper.getAeaParStageById(stageId);
        CheckerInfo checkerInfo = new CheckerInfo();
        checkerInfo.setProjInfoId(projInfoId);
        String message = checkerManager.stageCheck(parStage, new CheckerContext(checkerInfo));
        return StringUtils.isBlank(message) ? new ResultForm(true, "前置条件检查通过！") : new ResultForm(false, message);
    }

}
