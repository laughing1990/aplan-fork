package com.augurit.aplanmis.front.solicit.service;

import com.augurit.agcloud.bsc.domain.BscRuleCode;
import com.augurit.agcloud.bsc.mapper.BscRuleCodeMapper;
import com.augurit.agcloud.bsc.sc.rule.code.service.AutoCodeNumberService;
import com.augurit.aplanmis.common.domain.AeaHiSolicit;
import com.augurit.aplanmis.common.mapper.AeaHiSolicitMapper;
import com.augurit.aplanmis.common.utils.DateUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

@Service
@Transactional
@Slf4j
public class SolicitCodeService {
    @Autowired
    private BscRuleCodeMapper bscRuleCodeMapper;
    @Autowired
    private AutoCodeNumberService autoCodeNumberService;
    @Autowired
    private AeaHiSolicitMapper aeaHiSolicitMapper;

    public String createSolicitCode() throws Exception {
        String codeIc ="SOLICIT_CODE-"+ this.getCurrentDay("yyyyMMdd");
        BscRuleCode ruleCode = bscRuleCodeMapper.getBscRuleCodeByCodeIc(codeIc, "ROOT");

        if (null == ruleCode) {
            //新增当日流水号规则
            this.insertBscRuleCode();
            ruleCode = bscRuleCodeMapper.getBscRuleCodeByCodeIc(codeIc, "ROOT");
        }
        String solicitCode = autoCodeNumberService.generateRootOrg(ruleCode.getCodeIc());

        return this.getDistinctCode(solicitCode, ruleCode);
    }

    //当申报失败时，回退nextValue
    public void decreaseNextValue() {
        String codeIc ="SOLICIT_CODE-"+ this.getCurrentDay("yyyyMMdd");
        BscRuleCode ruleCode = bscRuleCodeMapper.getBscRuleCodeByCodeIc(codeIc, "ROOT");
        if (null != ruleCode) {
            Long codeValueNext = ruleCode.getCodeValueNext();
            Long codeValueIncrease = ruleCode.getCodeValueIncrease();
            ruleCode.setCodeValueNext(codeValueNext - codeValueIncrease);
            bscRuleCodeMapper.updateBscRuleCode(ruleCode);
        }
    }

    //递归获取不重复的 solicitCode
    private String getDistinctCode(String solicitCode, BscRuleCode ruleCode) throws Exception {
        AeaHiSolicit aeaHiSolicit = aeaHiSolicitMapper.getAeaHiSolicitBySolicitCode(solicitCode);
        if (null != aeaHiSolicit) {
            solicitCode = autoCodeNumberService.generateRootOrg(ruleCode.getCodeIc());
            return getDistinctCode(solicitCode, ruleCode);
        }
        return solicitCode;
    }

    //获取当天时间字符串
    private String getCurrentDay(String formatTemplate) {
        return DateUtils.convertDateToString(new Date(), formatTemplate);
    }

    //新增当天流水号规则
    private void insertBscRuleCode() {
        String codeId = this.getCurrentDay("yyyyMMddHHmmss");
        String currentDay = this.getCurrentDay("yyyyMMdd");
        String codeIc = "SOLICIT_CODE-" + currentDay;
        String codeName = "意见征询流水号-" + currentDay;
        Long codeValueNext = 1L;
        Long codeValueIncrease = 1L;
        Long codeValueCharCount = 4L;
        String codeTemplate = currentDay + "-{value}";
        String codeMemo =currentDay;
        String orgId = "ROOT";
        BscRuleCode ruleCode = new BscRuleCode();
        ruleCode.setOrgId(orgId);
        ruleCode.setCodeIc(codeIc);
        ruleCode.setCodeId(codeId);
        ruleCode.setCodeName(codeName);
        ruleCode.setCodeValueNext(codeValueNext);
        ruleCode.setCodeValueIncrease(codeValueIncrease);
        ruleCode.setCodeValueCharCount(codeValueCharCount);
        ruleCode.setCodeTemplate(codeTemplate);
        ruleCode.setCodeMemo(codeMemo);
        bscRuleCodeMapper.insertBscRuleCode(ruleCode);

    }
}
