package com.augurit.aplanmis.common.service.dic;

import com.augurit.agcloud.bsc.domain.BscRuleCode;
import com.augurit.agcloud.bsc.mapper.BscRuleCodeMapper;
import com.augurit.agcloud.bsc.sc.rule.code.service.AutoCodeNumberService;
import com.augurit.aplanmis.common.domain.AeaHiApplyinst;
import com.augurit.aplanmis.common.service.instance.AeaHiApplyinstService;
import com.augurit.aplanmis.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class ApplyinstCodeService {
    @Autowired
    private BscRuleCodeMapper bscRuleCodeMapper;
    @Autowired
    private AutoCodeNumberService autoCodeNumberService;
    @Autowired
    private AeaHiApplyinstService aeaHiApplyinstService;

    /**
     * 获取当天最大申报流水号
     *
     * @return applyinstCode;
     */
    public String getApplyinstCodeCurrentDay() throws Exception {
        String codeId = this.getCurrentDay("yyyyMMdd");
        BscRuleCode ruleCode = bscRuleCodeMapper.getBscRuleCodeById(codeId);

        if (null == ruleCode) {
            //新增当日流水号规则
            this.insertBscRuleCode();
            ruleCode = bscRuleCodeMapper.getBscRuleCodeById(codeId);
        }
        String applyinstCode = autoCodeNumberService.generateRootOrg(ruleCode.getCodeIc());

        return this.getDistinctCode(applyinstCode, ruleCode);
    }


    //当申报失败时，回退nextValue
    public void decreaseNextValue() {
        String codeId = this.getCurrentDay("yyyyMMdd");
        BscRuleCode ruleCode = bscRuleCodeMapper.getBscRuleCodeById(codeId);
        if (null != ruleCode) {
            Long codeValueNext = ruleCode.getCodeValueNext();
            Long codeValueIncrease = ruleCode.getCodeValueIncrease();
            ruleCode.setCodeValueNext(codeValueNext - codeValueIncrease);
            bscRuleCodeMapper.updateBscRuleCode(ruleCode);
        }
    }

    //递归获取不重复的applyinstCode
    private String getDistinctCode(String applyinstCode, BscRuleCode ruleCode) throws Exception {
        AeaHiApplyinst applyinst = aeaHiApplyinstService.getAeaHiApplyinstByCode(applyinstCode);
        if (null != applyinst) {
            applyinstCode = autoCodeNumberService.generateRootOrg(ruleCode.getCodeIc());
            return getDistinctCode(applyinstCode, ruleCode);
        }
        return applyinstCode;
    }

    //获取当天时间字符串
    private String getCurrentDay(String formatTemplate) {
        return DateUtils.convertDateToString(new Date(), formatTemplate);
    }

    //新增当天流水号规则
    private void insertBscRuleCode() {
        String currentDay = this.getCurrentDay("yyyyMMdd");
        String codeIc = "APPLYINST_CODE-" + currentDay;
        String codeName = "申报流水号-" + currentDay;
        String codeId = currentDay;
        Long codeValueNext = 1L;
        Long codeValueIncrease = 1L;
        Long codeValueCharCount = 4L;
        String codeTemplate = currentDay + "{value}";
        String codeMemo = "申报流水号";
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
