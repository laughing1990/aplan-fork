package com.augurit.aplanmis.front.check.service;

import com.augurit.agcloud.framework.ui.result.ContentResultForm;
import com.augurit.agcloud.framework.ui.result.ResultForm;
import com.augurit.aplanmis.common.check.CheckItemResultInfo;
import com.augurit.aplanmis.common.check.CheckStageResultInfo;
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

import java.util.List;

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

    public ResultForm itemFrontCheck(List<String> itemVerIds, String projInfoId) {
        List<AeaItemBasic> aeaItemBasics = aeaItemBasicMapper.listAeaItemBasicByItemVerIds(itemVerIds.toArray(new String[0]));
        CheckerInfo checkerInfo = new CheckerInfo();
        checkerInfo.setProjInfoId(projInfoId);
        CheckItemResultInfo resultInfo = checkerManager.itemCheck(aeaItemBasics, new CheckerContext(checkerInfo));
        return resultInfo.isPassed() ? new ResultForm(true, "前置条件检查通过！") : new ContentResultForm<>(false, resultInfo, resultInfo.getMessage());
    }

    public ResultForm stageFrontCheck(String stageId, String projInfoId) {
        AeaParStage parStage = aeaParStageMapper.getAeaParStageById(stageId);
        CheckerInfo checkerInfo = new CheckerInfo();
        checkerInfo.setProjInfoId(projInfoId);
        CheckStageResultInfo checkStageResultInfo = checkerManager.stageCheck(parStage, new CheckerContext(checkerInfo));
        return checkStageResultInfo.isPassed() ? new ResultForm(true, "前置条件检查通过！") : new ContentResultForm<>(false, checkStageResultInfo, checkStageResultInfo.getMessage());
    }

}
