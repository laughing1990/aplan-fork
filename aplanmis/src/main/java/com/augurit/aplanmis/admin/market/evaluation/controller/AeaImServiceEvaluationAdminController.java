package com.augurit.aplanmis.admin.market.evaluation.controller;

import com.augurit.agcloud.framework.ui.pager.EasyuiPageInfo;
import com.augurit.agcloud.framework.ui.pager.PageHelper;
import com.augurit.agcloud.framework.ui.result.ContentResultForm;
import com.augurit.agcloud.framework.ui.result.ResultForm;
import com.augurit.aplanmis.admin.market.evaluation.service.AeaImServiceEvaluationAdminService;
import com.augurit.aplanmis.common.domain.AeaImServiceEvaluation;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

/**
 * @author tiantian
 * @date 2019/6/21
 */
@RestController
@RequestMapping("/supermarket/evaluation")
public class AeaImServiceEvaluationAdminController {
    @Autowired
    private AeaImServiceEvaluationAdminService aeaImServiceEvaluationAdminService;

    @RequestMapping("/index.do")
    public ModelAndView index() {
        return new ModelAndView("ui-jsp/supermarket_manage/evaluation/index");
    }

    @RequestMapping("/listAuditServiceEvaluationByPage.do")
    public EasyuiPageInfo<AeaImServiceEvaluation> listAuditServiceEvaluationByPage(String keyword, String auditFlag, Page page) throws Exception{
        return PageHelper.toEasyuiPageInfo(new PageInfo(aeaImServiceEvaluationAdminService.listAuditServiceEvaluationByPage(keyword,auditFlag,page)));
    }

    @RequestMapping("/getAeaImServiceEvaluationByServiceEvaluationId.do")
    public ResultForm getAeaImServiceEvaluationByServiceEvaluationId(String serviceEvaluationId) throws Exception{
        AeaImServiceEvaluation aeaImServiceEvaluation = aeaImServiceEvaluationAdminService.getAeaImServiceEvaluationByServiceEvaluationId(serviceEvaluationId);

        if(aeaImServiceEvaluation!=null){
            return new ContentResultForm(true,aeaImServiceEvaluation);

        }
        return new ResultForm(false);
    }

    @RequestMapping("/auditEvaluation.do")
    public ResultForm auditEvaluation(String serviceEvaluationId,String auditFlag,String auditOpinion) throws Exception{
        aeaImServiceEvaluationAdminService.auditEvaluation(serviceEvaluationId,auditFlag,auditOpinion);
        return new ResultForm(true);
    }
}
