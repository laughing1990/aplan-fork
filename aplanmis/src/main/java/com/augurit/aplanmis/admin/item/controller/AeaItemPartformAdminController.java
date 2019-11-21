package com.augurit.aplanmis.admin.item.controller;

import com.augurit.agcloud.bpm.common.constant.ActStoConstant;
import com.augurit.agcloud.framework.constant.Status;
import com.augurit.agcloud.framework.exception.InvalidParameterException;
import com.augurit.agcloud.framework.ui.pager.EasyuiPageInfo;
import com.augurit.agcloud.framework.ui.pager.PageHelper;
import com.augurit.agcloud.framework.ui.result.ContentResultForm;
import com.augurit.agcloud.framework.ui.result.ResultForm;
import com.augurit.agcloud.framework.util.StringUtils;
import com.augurit.aplanmis.common.domain.AeaItemPartform;
import com.augurit.aplanmis.common.service.admin.item.AeaItemPartformAdminService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

/**
 * 事项与扩展表单关联表-Controller 页面控制转发类
 */
@RestController
@RequestMapping("/aea/item/partform")
public class AeaItemPartformAdminController {

    private static Logger logger = LoggerFactory.getLogger(AeaItemPartformAdminController.class);

    @Autowired
    private AeaItemPartformAdminService aeaItemPartformService;

    @RequestMapping("/listPartFormNoSelectFormByPage.do")
    public EasyuiPageInfo<AeaItemPartform> listPartFormNoSelectFormByPage(AeaItemPartform partform, Page page) {

        PageInfo<AeaItemPartform> pageInfo = aeaItemPartformService.listPartFormNoSelectFormByPage(partform, page);
        return PageHelper.toEasyuiPageInfo(pageInfo);
    }


    @RequestMapping("/getMaxSortNo.do")
    public Long getMaxSortNo(String itemVerId) {

        if (StringUtils.isBlank(itemVerId)) {
            throw new InvalidParameterException("参数stageId为空!");
        }
        return aeaItemPartformService.getMaxSortNo(itemVerId);
    }


    @RequestMapping("/listAeaItemPartform.do")
    public EasyuiPageInfo<AeaItemPartform> listAeaItemPartform(AeaItemPartform aeaItemPartform, Page page) throws Exception {
        logger.debug("分页查询，过滤条件为{}，查询关键字为{}", aeaItemPartform);
        PageInfo<AeaItemPartform> res = aeaItemPartformService.listAeaItemPartform(aeaItemPartform, page);
        return PageHelper.toEasyuiPageInfo(res);
    }

    @RequestMapping("/listAeaItemPartformNoPage.do")
    public List<AeaItemPartform> listAeaItemPartformNoPage(AeaItemPartform aeaItemPartform) throws Exception {
        logger.debug("分页查询，过滤条件为{}，查询关键字为{}", aeaItemPartform);
        List<AeaItemPartform> res = aeaItemPartformService.listAeaItemPartformNoPage(aeaItemPartform);
        return res;
    }

    @RequestMapping("/getAeaItemPartform.do")
    public AeaItemPartform getAeaItemPartform(String id) throws Exception {
        if (id != null) {
            logger.debug("根据ID获取AeaItemPartform对象，ID为：{}", id);
            return aeaItemPartformService.getAeaItemPartformById(id);
        } else {
            logger.debug("构建新的AeaItemPartform对象");
            return new AeaItemPartform();
        }
    }


    /**
     * 保存或编辑事项与扩展表单关联表
     *
     * @param aeaItemPartform 事项与扩展表单关联表
     * @param result          校验对象
     * @return 返回结果对象 包含结果信息
     * @throws Exception
     */
    @RequestMapping("/saveAeaItemPartform.do")
    public ResultForm saveAeaItemPartform(AeaItemPartform aeaItemPartform, BindingResult result) throws Exception {
        if (result.hasErrors()) {
            logger.error("保存事项与扩展表单关联表Form对象出错");
            throw new InvalidParameterException(aeaItemPartform);
        }
        if (StringUtils.isNotBlank(aeaItemPartform.getItemPartformId())) {
            aeaItemPartformService.updateAeaItemPartform(aeaItemPartform);
        } else {
            aeaItemPartform.setItemPartformId(UUID.randomUUID().toString());
            aeaItemPartformService.saveAeaItemPartform(aeaItemPartform);
        }

        return new ContentResultForm<AeaItemPartform>(true, aeaItemPartform);
    }

    @RequestMapping("/updateAeaItemPartformWithFormId.do")
    public ResultForm updateAeaItemPartformWithFormId(String id, String formId, String operation) throws Exception {

        if (StringUtils.isBlank(id)) {
            throw new InvalidParameterException("参数id为空!");
        }
        if (StringUtils.isBlank(formId)) {
            throw new InvalidParameterException("参数formId为空!");
        }
        if (ActStoConstant.SMART_FORM_ENTITY_OPERATION_NEW.equals(operation)) {
            AeaItemPartform partform = new AeaItemPartform();
            partform.setItemPartformId(id);
            partform.setStoFormId(formId);
            aeaItemPartformService.updateAeaItemPartform(partform);
        }
        return new ResultForm(true);
    }

    @RequestMapping("/deleteAeaItemPartformById.do")
    public ResultForm deleteAeaItemPartformById(String id) throws Exception {
        logger.debug("删除事项与扩展表单关联表Form对象，对象id为：{}", id);
        if (id != null) {
            aeaItemPartformService.deleteAeaItemPartformById(id);
        }
        return new ResultForm(true);
    }

    @RequestMapping("/batchDelItemPartform.do")
    public ResultForm batchDelItemPartform(String[] ids) throws Exception {

        if (ids != null && ids.length > 0) {
            logger.debug("删除阶段与事项关联定义表Form对象，对象id为：{}", ids);
            aeaItemPartformService.batchDelItemPartform(ids);
            return new ResultForm(true);
        } else {
            throw new InvalidParameterException("参数ids为空!");
        }
    }

    @RequestMapping("savePartformFormsAndNotDelOld.do")
    public ResultForm savePartformFormsAndNotDelOld(String itemPartformId, String formId) {

        if (StringUtils.isBlank(itemPartformId)) {
            throw new InvalidParameterException("参数itemPartformId为空!");
        }
        if (StringUtils.isBlank(formId)) {
            throw new InvalidParameterException("参数formId为空!");
        }
        AeaItemPartform partform = aeaItemPartformService.getAeaItemPartformById(itemPartformId);
        if (partform != null) {
            partform.setStoFormId(formId);
            aeaItemPartformService.updateAeaItemPartform(partform);
        } else {
            partform.setItemPartformId(itemPartformId);
            partform.setPartformName("未命名扩展表单");
            partform.setStoFormId(formId);
            partform.setUseEl(Status.OFF);
            partform.setIsSmartForm(Status.ON);
            aeaItemPartformService.saveAeaItemPartform(partform);
        }
        return new ResultForm(true);
    }

    @RequestMapping("/delItemPartformRelFormById.do")
    public ResultForm delItemPartformRelFormById(String id) {

        if (StringUtils.isBlank(id)) {
            throw new InvalidParameterException("参数id为空!");
        }
        AeaItemPartform partform = new AeaItemPartform();
        partform.setItemPartformId(id);
        partform.setStoFormId("");
        aeaItemPartformService.updateAeaItemPartform(partform);
        return new ResultForm(true);
    }

    @RequestMapping("/updateItemPartformSortNos.do")
    public ResultForm updateItemPartformSortNos(String[] ids, String[] sortNos) throws Exception {

        if (ids != null && ids.length > 0 && sortNos != null && sortNos.length > 0) {
            for (int i = 0; i < ids.length; i++) {
                AeaItemPartform partform = new AeaItemPartform();
                partform.setItemPartformId(ids[i]);
                partform.setSortNo(sortNos[i]);
                aeaItemPartformService.updateAeaItemPartform(partform);
            }
            return new ResultForm(true);
        }
        return new ResultForm(false, "传递扩展表排序数据有问题,请检查!");
    }

    @RequestMapping("/createAndUpdateDevForm.do")
    public ResultForm createAndUpdateDevForm(String formCode, String formName, String formLoadUrl, String formId, String itemPartformId) {
        try {

            if (StringUtils.isBlank(formCode) || StringUtils.isBlank(formName) || StringUtils.isBlank(formLoadUrl) || StringUtils.isBlank(itemPartformId)) {
                return new ResultForm(false, "缺少参数！");
            }
            aeaItemPartformService.createAndUpdateDevForm(formCode, formName, formLoadUrl, formId, itemPartformId);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResultForm(false, "新增/编辑开发表单失败");
        }
        return new ResultForm(true, "新增/编辑开发表单成功");
    }

}
