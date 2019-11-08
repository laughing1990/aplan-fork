package com.augurit.aplanmis.admin.par;

import com.augurit.agcloud.framework.ui.pager.EasyuiPageInfo;
import com.augurit.agcloud.framework.ui.pager.PageHelper;
import com.augurit.agcloud.framework.ui.result.ResultForm;
import com.augurit.agcloud.framework.util.StringUtils;
import com.augurit.aplanmis.common.domain.AeaParStateForm;
import com.augurit.aplanmis.common.service.admin.par.AeaParStateFormAdminService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 情形与表单关联定义表-Controller 页面控制转发类
 *
 * @author jjt
 * @date 2019/4/17
 */
@RestController
@RequestMapping("/aea/par/state/form")
public class AeaParStateFormAdminController {

    private static Logger logger = LoggerFactory.getLogger(AeaParStateFormAdminController.class);

    @Autowired
    private AeaParStateFormAdminService aeaParStateFormAdminService;

    /**
     * 获取表单以及情形关联表单
     * @param aeaParStateForm
     * @param pageNum
     * @param pageSize
     * @return
     */
    @RequestMapping("/listFormAndStateFormByPage.do")
    public EasyuiPageInfo<AeaParStateForm> listFormAndStateFormByPage(AeaParStateForm aeaParStateForm, Integer pageNum, Integer pageSize) {

        Page page = new Page();
        page.setPageSize(pageSize==null?10:pageSize);
        page.setPageNum(pageNum==null?1:pageNum);
        PageInfo pageInfo = aeaParStateFormAdminService.listFormAndStateFormByPage(aeaParStateForm, page);
        return PageHelper.toEasyuiPageInfo(pageInfo);
    }

    /**
     * 查询情形关联表单
     *
     * @param aeaParStateForm
     * @return
     */
    @RequestMapping("/listAeaParStateForm.do")
    public List<AeaParStateForm> listAeaParStateForm(AeaParStateForm aeaParStateForm) {

        return aeaParStateFormAdminService.listAeaParStateForm(aeaParStateForm);
    }

    /**
     *  保存情形表单
     *
     * @param parStateId
     * @param formIds
     * @return
     */
    @RequestMapping("/saveAeaParStateForms.do")
    public ResultForm saveAeaParStateForms(String parStageId, String parStateId,String isStateForm, String[] formIds) {

        if (StringUtils.isNotBlank(parStageId)) {
            aeaParStateFormAdminService.saveAeaParStateForms(parStageId, parStateId, isStateForm, formIds);
            return new ResultForm(true);
        }
        return new ResultForm(false, "操作参数parStageId为空!");
    }

    @RequestMapping("/saveAeaParStateFormsAndNotDelOld.do")
    public ResultForm saveAeaParStateFormsAndNotDelOld(String parStageId, String parStateId,String isStateForm, String[] formIds) {

        if (StringUtils.isNotBlank(parStageId)) {
            aeaParStateFormAdminService.saveAeaParStateFormsAndNotDelOld(parStageId, parStateId, isStateForm, formIds);
            return new ResultForm(true);
        }
        return new ResultForm(false, "操作参数parStageId为空!");
    }

    @RequestMapping("/delAeaParStateFormById.do")
    public ResultForm delAeaParStateFormById(String id) {

        if (StringUtils.isNotBlank(id)) {
            aeaParStateFormAdminService.deleteAeaParStateFormById(id);
            return new ResultForm(true);
        }
        return new ResultForm(false, "操作参数id为空!");
    }

    @RequestMapping("/batchDelAeaParStateFormByIds.do")
    public ResultForm batchDelAeaParStateFormByIds(String[] ids) {

        if (ids!=null&&ids.length>0) {
            aeaParStateFormAdminService.batchDelAeaParStateFormByIds(ids);
            return new ResultForm(true);
        }
        return new ResultForm(false, "操作参数ids为空!");
    }

    @RequestMapping("/listStageNoSelectFormByPage.do")
    public EasyuiPageInfo<AeaParStateForm> listStageNoSelectFormByPage(AeaParStateForm form, Page page) {

        PageInfo<AeaParStateForm> pageInfo = aeaParStateFormAdminService.listStageNoSelectFormByPage(form, page);
        return PageHelper.toEasyuiPageInfo(pageInfo);
    }

    @RequestMapping("/listStageNoSelectForm.do")
    public List<AeaParStateForm> listStageNoSelectForm(AeaParStateForm form) {

        return aeaParStateFormAdminService.listStageNoSelectForm(form);
    }
}
