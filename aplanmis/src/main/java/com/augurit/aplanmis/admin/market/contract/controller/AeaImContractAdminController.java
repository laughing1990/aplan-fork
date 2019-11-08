package com.augurit.aplanmis.admin.market.contract.controller;

import com.augurit.agcloud.framework.ui.pager.EasyuiPageInfo;
import com.augurit.agcloud.framework.ui.pager.PageHelper;
import com.augurit.agcloud.framework.ui.result.ContentResultForm;
import com.augurit.agcloud.framework.ui.result.ResultForm;
import com.augurit.aplanmis.admin.market.contract.service.AeaImContractAdminService;
import com.augurit.aplanmis.common.domain.AeaImContract;
import com.augurit.aplanmis.common.service.file.FileUtilsService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageInfo;
import org.apache.catalina.servlet4preview.http.HttpServletRequest;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletResponse;

/**
 * @author tiantian
 * @date 2019/6/20
 */
@RestController
@RequestMapping("/supermarket/contract")
public class AeaImContractAdminController {

    @Autowired
    private AeaImContractAdminService aeaImContractAdminService;

    @Autowired
    private FileUtilsService fileUtilsService;

    @RequestMapping("/index.do")
    public ModelAndView index() {
        return new ModelAndView("ui-jsp/supermarket_manage/contract/index");
    }

    @RequestMapping("/listAuditAeaImContractByPage.do")
    public EasyuiPageInfo<AeaImContract> listAuditAeaImContractByPage(String keyword, String auditFlag, Page page) throws Exception {
        return PageHelper.toEasyuiPageInfo(new PageInfo(aeaImContractAdminService.listAuditAeaImContractByPage(keyword, auditFlag, page)));
    }

    @RequestMapping("/getAeaImContractByContractId.do")
    public ResultForm getAeaImContractByContractId(String contractId) throws Exception {
        AeaImContract aeaImContract = aeaImContractAdminService.getAeaImContractByContractId(contractId);

        if (aeaImContract != null) {
            return new ContentResultForm(true, aeaImContract);

        }
        return new ResultForm(false);
    }

    @RequestMapping("/auditContract.do")
    public ResultForm auditContract(String contractId, String auditFlag, String auditOpinion) throws Exception {
        aeaImContractAdminService.auditContract(contractId, auditFlag, auditOpinion);
        return new ResultForm(true);
    }


    @RequestMapping("/downloadFile.do")
    public void auditContract(String detailId, HttpServletResponse response, HttpServletRequest request) throws Exception {
        if (StringUtils.isNotBlank(detailId)) {
//            attachmentAdminService.downloadFileStrategy(detailId, response);
            fileUtilsService.downloadAttachment(detailId, response, request, false);
        }
    }
}
