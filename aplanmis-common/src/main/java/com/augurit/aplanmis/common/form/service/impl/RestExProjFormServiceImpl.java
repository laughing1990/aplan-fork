package com.augurit.aplanmis.common.form.service.impl;

import com.augurit.agcloud.bpm.admin.sto.service.impl.AbstractFormDataOptManager;
import com.augurit.agcloud.bpm.common.constant.EDataOpt;
import com.augurit.agcloud.bpm.common.domain.ActStoForm;
import com.augurit.agcloud.bpm.common.domain.ActStoForminst;
import com.augurit.agcloud.bpm.common.domain.vo.FormDataOptResult;
import com.augurit.agcloud.framework.security.SecurityContext;
import com.augurit.agcloud.framework.ui.result.ResultForm;
import com.augurit.agcloud.framework.util.StringUtils;
import com.augurit.aplanmis.common.domain.AeaApplyinstForminst;
import com.augurit.aplanmis.common.domain.AeaExProjMoney;
import com.augurit.aplanmis.common.domain.AeaProjInfo;
import com.augurit.aplanmis.common.form.service.AeaExProjCertBuildService;
import com.augurit.aplanmis.common.form.service.RestExProjFormService;
import com.augurit.aplanmis.common.form.vo.ExProjFormVo;
import com.augurit.aplanmis.common.service.form.AeaExProjMoneyService;
import com.augurit.aplanmis.common.service.project.AeaProjInfoService;
import com.augurit.aplanmis.front.basis.stage.service.RestStageService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.UUID;

@Service
@Transactional
public class RestExProjFormServiceImpl extends AbstractFormDataOptManager implements RestExProjFormService {

    @Autowired
    private AeaExProjMoneyService aeaExProjMoneyService;

    @Autowired
    private AeaProjInfoService aeaProjInfoService;
    @Autowired
    private AeaExProjCertBuildService aeaExProjCertBuildService;
    @Autowired
    private RestStageService restStageService;

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

            if (StringUtils.isBlank(exProjFormVo.getFormId())) throw new Exception("缺少formId");
            FormDataOptResult formDataOptResult = this.formSave(exProjFormVo.getFormId(), aeaExProjMoney.getMoneyId(), EDataOpt.INSERT.getOpareteType(), null);
            //关联表单实例和申请实例
            AeaApplyinstForminst aeaApplyinstForminst = new AeaApplyinstForminst();
            aeaApplyinstForminst.setApplyinstId(exProjFormVo.getRefEntityId());
            aeaApplyinstForminst.setForminstId(formDataOptResult.getActStoForminst().getStoForminstId());
            aeaApplyinstForminst.setStoFormId(exProjFormVo.getFormId());
            aeaApplyinstForminst.setCreateTime(new Date());
            aeaApplyinstForminst.setCreater(SecurityContext.getCurrentUserId());
            restStageService.bindForminst(aeaApplyinstForminst);
        }
        aeaExProjCertBuildService.SynchronizeDataByExProjForm(exProjFormVo);//同步信息
        return new ResultForm(true, "建设项目登记信息保存成功！");
    }

    @Override
    public FormDataOptResult doformSave(String formId, String metaTableId, Integer opType, Object dataEntity) throws Exception {
        FormDataOptResult result = new FormDataOptResult();
        result.setSuccess(true);
        ActStoForminst actStoForminst = new ActStoForminst();
        actStoForminst.setFormId(formId);
        actStoForminst.setFormPrimaryKey(metaTableId);
        result.setActStoForminst(actStoForminst);
        result.setDataOpt(EDataOpt.INSERT);
        return result;
    }

    @Override
    public FormDataOptResult doformDelete(ActStoForm formVo, Object dataEntity) throws Exception {
        return null;
    }
}
