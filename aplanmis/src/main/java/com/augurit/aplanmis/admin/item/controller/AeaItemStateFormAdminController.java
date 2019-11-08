package com.augurit.aplanmis.admin.item.controller;

import com.augurit.agcloud.framework.ui.pager.EasyuiPageInfo;
import com.augurit.agcloud.framework.ui.pager.PageHelper;
import com.augurit.agcloud.framework.ui.result.ResultForm;
import com.augurit.agcloud.framework.util.StringUtils;
import com.augurit.aplanmis.common.domain.AeaItemStateForm;
import com.augurit.aplanmis.common.service.admin.item.AeaItemStateFormAdminService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.InvalidParameterException;
import java.util.List;

/**
 * 情形与表单关联定义表-Controller 页面控制转发类
 *
 * @author jjt
 * @date 2019/4/17
 */
@RestController
@RequestMapping("/aea/item/state/form")
public class AeaItemStateFormAdminController {

    private static Logger logger = LoggerFactory.getLogger(AeaItemStateFormAdminController.class);

    @Autowired
    private AeaItemStateFormAdminService aeaItemStateFormAdminService;

    /**
     * 获取表单以及情形关联表单
     *
     * @param form
     *
     * @param pageNum
     * @param pageSize
     * @return
     */
    @RequestMapping("/listCurOrgFormByPage.do")
    public EasyuiPageInfo<AeaItemStateForm> listCurOrgFormByPage(AeaItemStateForm form, Integer pageNum, Integer pageSize) {

        Page page = new Page();
        page.setPageSize(pageSize==null?10:pageSize);
        page.setPageNum(pageNum==null?1:pageNum);
        PageInfo pageInfo = aeaItemStateFormAdminService.listCurOrgFormByPage(form, page);
        return PageHelper.toEasyuiPageInfo(pageInfo);
    }

    /**
     * 查询情形关联表单
     *
     * @param AeaItemStateForm
     * @return
     */
    @RequestMapping("/listAeaItemStateForm.do")
    public List<AeaItemStateForm> listAeaItemStateForm(AeaItemStateForm AeaItemStateForm) {

        return aeaItemStateFormAdminService.listAeaItemStateForm(AeaItemStateForm);
    }

    /**
     * 保存情形表单
     *
     * @param itemVerId
     * @param itemStateVerId
     * @param isStateForm
     * @param itemStateId
     * @param formIds
     * @return
     */
    @RequestMapping("/saveAeaItemStateForms.do")
    public ResultForm saveAeaItemStateForms(String itemVerId, String itemStateVerId, String isStateForm, String itemStateId, String[] formIds) {

        if (StringUtils.isBlank(itemVerId)){
            throw new InvalidParameterException("参数itemVerId为空!");
        }
        if(StringUtils.isBlank(itemStateVerId)){
            throw new InvalidParameterException("参数itemStateVerId为空!");
        }
        if(StringUtils.isBlank(isStateForm)){
            throw new InvalidParameterException("参数isStateForm为空!");
        }
        if(isStateForm.equals("1")&&StringUtils.isBlank(itemStateId)){
            throw new InvalidParameterException("参数itemStateId为空!");
        }
        aeaItemStateFormAdminService.saveAeaItemStateForms(itemVerId, itemStateVerId, isStateForm, itemStateId, formIds);
        return new ResultForm(true);
    }

    @RequestMapping("/saveAeaItemStateFormsAndNotDelOld.do")
    public ResultForm saveAeaItemStateFormsAndNotDelOld(String itemVerId, String itemStateVerId, String isStateForm, String itemStateId, String[] formIds) {

        if (StringUtils.isBlank(itemVerId)){
            throw new InvalidParameterException("参数itemVerId为空!");
        }
        if(StringUtils.isBlank(itemStateVerId)){
            throw new InvalidParameterException("参数itemStateVerId为空!");
        }
        if(StringUtils.isBlank(isStateForm)){
            throw new InvalidParameterException("参数isStateForm为空!");
        }
        if(isStateForm.equals("1")&&StringUtils.isBlank(itemStateId)){
            throw new InvalidParameterException("参数itemStateId为空!");
        }
        aeaItemStateFormAdminService.saveAeaItemStateFormsAndNotDelOld(itemVerId, itemStateVerId, isStateForm, itemStateId, formIds);
        return new ResultForm(true);
    }

    @RequestMapping("/delAeaItemStateFormById.do")
    public ResultForm delAeaItemStateFormById(String id) {

        if (StringUtils.isNotBlank(id)) {
            aeaItemStateFormAdminService.deleteAeaItemStateFormById(id);
            return new ResultForm(true);
        }
        return new ResultForm(false, "操作参数id为空!");
    }

    @RequestMapping("/batchDelAeaItemStateFormByIds.do")
    public ResultForm batchDelAeaItemStateFormByIds(String[] ids) {

        if (ids!=null&&ids.length>0) {
            aeaItemStateFormAdminService.batchDelAeaItemStateFormByIds(ids);
            return new ResultForm(true);
        }
        return new ResultForm(false, "操作参数ids为空!");
    }

    @RequestMapping("/listItemNoSelectFormByPage.do")
    public EasyuiPageInfo<AeaItemStateForm> listItemNoSelectFormByPage(AeaItemStateForm form, Page page) {

        PageInfo<AeaItemStateForm> pageInfo = aeaItemStateFormAdminService.listItemNoSelectFormByPage(form, page);
        return PageHelper.toEasyuiPageInfo(pageInfo);
    }

    @RequestMapping("/listItemNoSelectForm.do")
    public List<AeaItemStateForm> listItemNoSelectForm(AeaItemStateForm form) {

        return aeaItemStateFormAdminService.listItemNoSelectForm(form);
    }

    
}
