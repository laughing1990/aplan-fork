package com.augurit.aplanmis.admin.par;

import com.augurit.agcloud.bpm.common.domain.ActStoForm;
import com.augurit.agcloud.framework.constant.Status;
import com.augurit.agcloud.framework.exception.InvalidParameterException;
import com.augurit.agcloud.framework.ui.pager.EasyuiPageInfo;
import com.augurit.agcloud.framework.ui.pager.PageHelper;
import com.augurit.agcloud.framework.ui.result.ContentResultForm;
import com.augurit.agcloud.framework.ui.result.ResultForm;
import com.augurit.agcloud.framework.util.StringUtils;
import com.augurit.aplanmis.common.domain.AeaParStagePartform;
import com.augurit.aplanmis.common.service.admin.oneform.AeaParStagePartformService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/aea/par/stage/partform")
public class AeaParStagePartformController {

    private static Logger logger = LoggerFactory.getLogger(AeaParStagePartformController.class);

    @Autowired
    private AeaParStagePartformService aeaParStagePartformService;

    @RequestMapping("/indexAeaParStagePartform.do")
    public ModelAndView indexAeaParStagePartform(AeaParStagePartform partform) {

        return new ModelAndView("aea/par/stage/stage_partform_index");
    }

    @RequestMapping("/listStagePartformRelForm.do")
    public EasyuiPageInfo<AeaParStagePartform> listStagePartformRelForm(AeaParStagePartform partform, Page page) throws Exception {

        logger.debug("分页查询，过滤条件为{}，查询关键字为{}", partform);
        PageInfo<AeaParStagePartform> pageInfo = aeaParStagePartformService.listStagePartformRelForm(partform, page);
        return PageHelper.toEasyuiPageInfo(pageInfo);
    }

    @RequestMapping("/listStagePartformRelFormByNoPage.do")
    public List<AeaParStagePartform> listStagePartformRelFormByNoPage(AeaParStagePartform partform) throws Exception {

        logger.debug("分页查询，过滤条件为{}，查询关键字为{}", partform);
        return aeaParStagePartformService.listStagePartformRelForm(partform);
    }

    @RequestMapping("/listPartFormNoSelectFormByPage.do")
    public EasyuiPageInfo<AeaParStagePartform> listPartFormNoSelectFormByPage(AeaParStagePartform partform, Page page) {

        PageInfo<AeaParStagePartform> pageInfo = aeaParStagePartformService.listPartFormNoSelectFormByPage(partform, page);
        return PageHelper.toEasyuiPageInfo(pageInfo);
    }

    @RequestMapping("/listPartFormNoSelectForm.do")
    public List<AeaParStagePartform> listPartFormNoSelectForm(AeaParStagePartform partform) {

        return aeaParStagePartformService.listPartFormNoSelectForm(partform);
    }

    @RequestMapping("/getStagePartform.do")
    public AeaParStagePartform getStagePartform(String id) throws Exception {

        if (StringUtils.isNotBlank(id)) {
            logger.debug("根据ID获取AeaParStagePartform对象，ID为：{}", id);
            return aeaParStagePartformService.getStagePartformById(id);
        } else {
            logger.debug("构建新的AeaParStagePartform对象");
            return new AeaParStagePartform();
        }
    }

    @RequestMapping("/updateStagePartform.do")
    public ResultForm updateStagePartform(AeaParStagePartform aeaParStagePartform) throws Exception {

        logger.debug("更新客户档案信息Form对象，对象为：{}", aeaParStagePartform);
        aeaParStagePartformService.updateStagePartform(aeaParStagePartform);
        return new ResultForm(true);
    }

    @RequestMapping("/updateStagePartformSortNos.do")
    public ResultForm updateStagePartformSortNos(String[] ids, Long[] sortNos) throws Exception {

        if (ids != null && ids.length > 0 && sortNos != null && sortNos.length > 0) {
            for (int i = 0; i < ids.length; i++) {
                AeaParStagePartform partform = new AeaParStagePartform();
                partform.setStagePartformId(ids[i]);
                partform.setSortNo(sortNos[i]);
                aeaParStagePartformService.updateStagePartform(partform);
            }
            return new ResultForm(true);
        }
        return new ResultForm(false, "传递材料证照排序数据有问题,请检查!");
    }


    /**
     * 保存或编辑阶段与事项关联定义表
     *
     * @param partform 阶段与事项关联定义表
     * @return 返回结果对象 包含结果信息
     * @throws Exception
     */
    @RequestMapping("/saveStagePartform.do")
    public ResultForm saveStagePartform(AeaParStagePartform partform) throws Exception {

        if (StringUtils.isNotBlank(partform.getStagePartformId())) {
            aeaParStagePartformService.updateStagePartform(partform);
        } else {
            partform.setStagePartformId(UUID.randomUUID().toString());
            aeaParStagePartformService.saveStagePartform(partform);
        }
        return new ContentResultForm<AeaParStagePartform>(true, partform);
    }

    @RequestMapping("/deleteStagePartformById.do")
    public ResultForm deleteStagePartformById(String id) throws Exception {

        if (StringUtils.isNotBlank(id)) {

            logger.debug("删除阶段与事项关联定义表Form对象，对象id为：{}", id);
            aeaParStagePartformService.deleteStagePartformById(id);
            return new ResultForm(true);
        } else {
            throw new InvalidParameterException("参数id为空!");
        }
    }

    @RequestMapping("/deleteStagePartformByIds.do")
    public ResultForm deleteStagePartformById(String[] ids) throws Exception {

        if (ids != null && ids.length > 0) {
            logger.debug("删除阶段与事项关联定义表Form对象，对象id为：{}", ids);
            aeaParStagePartformService.batchDelStagePartformByIds(ids);
            return new ResultForm(true);
        } else {
            throw new InvalidParameterException("参数ids为空!");
        }
    }

    @RequestMapping("/delStagePartformRelFormById.do")
    public ResultForm delStagePartformRelFormById(String id) {

        if (StringUtils.isBlank(id)) {
            throw new InvalidParameterException("参数id为空!");
        }
        AeaParStagePartform stagePartform = aeaParStagePartformService.getStagePartformById(id);
        if(stagePartform!=null){
            stagePartform.setStoFormId("");
            aeaParStagePartformService.updateStagePartform(stagePartform);
        }
        return new ResultForm(true);
    }

    @RequestMapping("/saveStagePartformByPartformId.do")
    public ResultForm saveStagePartformByPartformId(AeaParStagePartform stagePartform, String formId, String operation) {

        String stagePartformId = stagePartform.getStagePartformId();
        if(StringUtils.isBlank(stagePartformId)){
            throw new InvalidParameterException("参数stagePartformId为空!");
        }
        if(StringUtils.isBlank(formId)){
            throw new InvalidParameterException("参数formId为空!");
        }
        AeaParStagePartform parStagePartform = aeaParStagePartformService.getStagePartformById(stagePartformId);
        if(parStagePartform!=null) {
            parStagePartform.setStoFormId(formId);
            aeaParStagePartformService.updateStagePartform(parStagePartform);
        }else{
            if(stagePartform==null){
                return  new ResultForm(false, "传递参数全部丢失!");
            }
            if(StringUtils.isBlank(stagePartform.getStageId())){
                return  new ResultForm(false, "智能表单与阶段事项关联表记录不存在,传递参数stageId为空!");
            }
            if(StringUtils.isBlank(stagePartform.getIsSmartForm())){
                return  new ResultForm(false, "智能表单与阶段事项关联表记录不存在,传递参数isSmartForm为空!");
            }
            stagePartform.setStagePartformId(UUID.randomUUID().toString());
            stagePartform.setPartformName(StringUtils.isBlank(stagePartform.getPartformName())?"未命名":stagePartform.getPartformName());
            stagePartform.setStoFormId(formId);
            stagePartform.setUseEl(Status.OFF);
            aeaParStagePartformService.saveStagePartform(stagePartform);
        }
        return new ResultForm(true,"保存成功!");
    }

    @RequestMapping("/getMaxSortNo.do")
    public Long getMaxSortNo(String stageId) {

        if (StringUtils.isBlank(stageId)) {
            throw new InvalidParameterException("参数stageId为空!");
        }
        return aeaParStagePartformService.getMaxSortNo(stageId);
    }

    @RequestMapping("savePartformFormsAndNotDelOld.do")
    public ResultForm savePartformFormsAndNotDelOld(String stagePartformId, String formId) {

        if (StringUtils.isBlank(stagePartformId)) {
            throw new InvalidParameterException("参数stagePartformId为空!");
        }
        if (StringUtils.isBlank(formId)) {
            throw new InvalidParameterException("参数formId为空!");
        }
        AeaParStagePartform stagePartform = aeaParStagePartformService.getStagePartformById(stagePartformId);
        if (stagePartform != null) {
            stagePartform.setStoFormId(formId);
            aeaParStagePartformService.updateStagePartform(stagePartform);
        } else {
            stagePartform.setStagePartformId(stagePartformId);
            stagePartform.setPartformName("未命名扩展表单");
            stagePartform.setStoFormId(formId);
            stagePartform.setUseEl(Status.OFF);
            stagePartform.setIsSmartForm(Status.ON);
            aeaParStagePartformService.saveStagePartform(stagePartform);
        }
        return new ResultForm(true);
    }

    @RequestMapping("createAndUpdateDevForm.do")
    public ResultForm createAndUpdateDevForm(String formCode, String formName, String formLoadUrl, String formId, String stagePartformId) {
        try {
            if (StringUtils.isBlank(formCode) || StringUtils.isBlank(formName) || StringUtils.isBlank(formLoadUrl) || StringUtils.isBlank(stagePartformId)) {
                return new ResultForm(false, "缺少参数！");
            }
            aeaParStagePartformService.createAndUpdateDevForm(formCode, formName, formLoadUrl, formId, stagePartformId);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResultForm(false, "新增/编辑开发表单失败");
        }
        return new ResultForm(true, "新增/编辑开发表单成功");
    }

    @RequestMapping("/getStageDevform.do")
    public ActStoForm getStageDevform(String formId) throws Exception {
        if (StringUtils.isNotBlank(formId)) {
            return aeaParStagePartformService.getStageDevformByFormId(formId);
        } else {
            return new ActStoForm();
        }
    }

}
