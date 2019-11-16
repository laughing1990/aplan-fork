package com.augurit.aplanmis.front.form.service.impl;

import com.augurit.agcloud.framework.security.SecurityContext;
import com.augurit.agcloud.framework.ui.result.ResultForm;
import com.augurit.aplanmis.common.domain.AeaExProjMoney;
import com.augurit.aplanmis.common.domain.AeaProjInfo;
import com.augurit.aplanmis.common.service.form.AeaExProjMoneyService;
import com.augurit.aplanmis.common.service.project.AeaProjInfoService;
import com.augurit.aplanmis.front.form.service.AeaExProjCertBuildService;
import com.augurit.aplanmis.front.form.service.RestExProjFormService;
import com.augurit.aplanmis.front.form.vo.ExProjFormVo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
@Transactional
public class RestExProjFormServiceImpl implements RestExProjFormService {

    @Autowired
    private AeaExProjMoneyService aeaExProjMoneyService;

    @Autowired
    private AeaProjInfoService aeaProjInfoService;
    @Autowired
    private AeaExProjCertBuildService aeaExProjCertBuildService;

    @Override
    public ExProjFormVo getExProjForm(String projInfoId) throws Exception {
        // 查询项目信息
        AeaProjInfo aeaProjInfo = aeaProjInfoService.getAeaProjInfoByProjInfoId(projInfoId);
        if (aeaProjInfo == null) {
            return null;
        }

        ExProjFormVo exProjFormVo = new ExProjFormVo();
        BeanUtils.copyProperties(aeaProjInfo, exProjFormVo);

        AeaExProjMoney aeaExProjMoney = new AeaExProjMoney();
        aeaExProjMoney.setProjInfoId(projInfoId);
        aeaExProjMoney.setRootOrgId(SecurityContext.getCurrentOrgId());
        List<AeaExProjMoney> aeaExProjMoneyList = aeaExProjMoneyService.listAeaExProjMoney(aeaExProjMoney);

        if (aeaExProjMoneyList != null && aeaExProjMoneyList.size() > 0) {
            BeanUtils.copyProperties(aeaExProjMoneyList.get(0), exProjFormVo);
        } else {
            exProjFormVo.setGovFinance("0");
            exProjFormVo.setStateEnterprice("0");
            exProjFormVo.setStateInvestment("0");
            exProjFormVo.setInternationalInvestment("0");
            exProjFormVo.setCollectiveInvestment("0");
            exProjFormVo.setForeignInvestment("0");
            exProjFormVo.setHkInvestment("0");
            exProjFormVo.setPrivateInvestment("0");
            exProjFormVo.setOtherInvestment("0");
        }
        return exProjFormVo;
    }

    @Override
    public ResultForm saveExProjForm(ExProjFormVo exProjFormVo) throws Exception {
        String projInfoId = exProjFormVo.getProjInfoId();
        // 查询项目信息
        AeaProjInfo aeaProjInfo = aeaProjInfoService.getAeaProjInfoByProjInfoId(projInfoId);
        if (aeaProjInfo == null) {
            return new ResultForm(false, "项目不存在");
        }

        BeanUtils.copyProperties(exProjFormVo, aeaProjInfo);
        aeaProjInfoService.updateAeaProjInfo(aeaProjInfo);

        AeaExProjMoney aeaExProjMoney = new AeaExProjMoney();
        aeaExProjMoney.setProjInfoId(projInfoId);
        aeaExProjMoney.setRootOrgId(SecurityContext.getCurrentOrgId());
        List<AeaExProjMoney> aeaExProjMoneyList = aeaExProjMoneyService.listAeaExProjMoney(aeaExProjMoney);
        BeanUtils.copyProperties(exProjFormVo, aeaExProjMoney);

        if (aeaExProjMoneyList != null && aeaExProjMoneyList.size() > 0) {
            aeaExProjMoney.setMoneyId(aeaExProjMoneyList.get(0).getMoneyId());
            aeaExProjMoney.setModifier(SecurityContext.getCurrentUserName());
            aeaExProjMoney.setModifyTime(new Date());
            aeaExProjMoneyService.updateAeaExProjMoney(aeaExProjMoney);
        } else {
            aeaExProjMoney.setMoneyId(UUID.randomUUID().toString());
            aeaExProjMoney.setCreater(SecurityContext.getCurrentUserName());
            aeaExProjMoney.setCreateTime(new Date());
            aeaExProjMoneyService.saveAeaExProjMoney(aeaExProjMoney);
        }
        aeaExProjCertBuildService.SynchronizeDataByExProjForm(exProjFormVo);//同步信息
        return new ResultForm(true, "建设项目登记信息保存成功！");
    }
}
