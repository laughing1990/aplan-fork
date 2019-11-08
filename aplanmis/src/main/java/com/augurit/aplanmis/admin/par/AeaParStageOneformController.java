package com.augurit.aplanmis.admin.par;

import com.augurit.agcloud.framework.exception.InvalidParameterException;
import com.augurit.agcloud.framework.security.SecurityContext;
import com.augurit.agcloud.framework.ui.pager.EasyuiPageInfo;
import com.augurit.agcloud.framework.ui.result.ContentResultForm;
import com.augurit.agcloud.framework.ui.result.ResultForm;
import com.augurit.agcloud.framework.util.StringUtils;
import com.augurit.aplanmis.common.domain.AeaOneform;
import com.augurit.aplanmis.common.domain.AeaParStageItem;
import com.augurit.aplanmis.common.domain.AeaParStageOneform;
import com.augurit.aplanmis.common.service.admin.oneform.AeaParStageOneformService;
import com.augurit.aplanmis.common.service.admin.par.AeaParStageItemAdminService;
import com.augurit.aplanmis.common.vo.ActStoFormVo;
import com.github.pagehelper.Page;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

/**
 * 一张表单-Controller 页面控制转发类
 */
@RestController
@RequestMapping("/aea/par/stage/stageOneform")
public class AeaParStageOneformController {

    private static Logger logger = LoggerFactory.getLogger(AeaParStageQuestionsController.class);

    @Autowired
    private AeaParStageOneformService aeaParStageOneformService;

    @Autowired
    private AeaParStageItemAdminService AeaParStageItemAdminService;

    /**
     * 获取一张表单总表数据列表
     *
     * @param parStageId
     * @param page
     * @return
     */
    @RequestMapping("/listStageOneform.do")
    public EasyuiPageInfo<AeaParStageOneform> listUsedAeaItemBasicTreeByPage(String parStageId, Page page) {

        logger.debug("分页查询，过滤条件为{}，查询关键字为{}", parStageId);
        return aeaParStageOneformService.getAeaParStageOneformList(parStageId, page);
    }


    /**
     * 获取一张表单事项子表列表
     *
     * @param parStageId
     * @param page
     * @return
     */
    @RequestMapping("/listItemForm.do")
    public EasyuiPageInfo<AeaParStageItem> listItemForm(String parStageId, Page page) {

        logger.debug("分页查询，过滤条件为{}，查询关键字为{}", parStageId);
        return aeaParStageOneformService.getItemFormlist(parStageId, page);
    }

    @RequestMapping("/saveParStageOneform.do")
    public ResultForm saveAeaLinkmanInfo(AeaParStageOneform aeaParStageOneform, BindingResult result) throws Exception {

        if (result.hasErrors()) {
            logger.error("保存联系人表Form对象出错");
            throw new InvalidParameterException(aeaParStageOneform);
        }

        if (aeaParStageOneform.getStageOneformId() != null && !"".equals(aeaParStageOneform.getStageOneformId())) {
            aeaParStageOneform.setModifier(SecurityContext.getCurrentUserName());
            aeaParStageOneformService.updateAeaParStageOneform(aeaParStageOneform);
        } else {
            if (aeaParStageOneform.getStageOneformId() == null || "".equals(aeaParStageOneform.getStageOneformId())) {
                aeaParStageOneform.setStageOneformId(UUID.randomUUID().toString());
            }
            aeaParStageOneform.setCreater(SecurityContext.getCurrentUserName());
            aeaParStageOneformService.saveAeaParStageOneform(aeaParStageOneform);
        }

        return new ContentResultForm<>(true, aeaParStageOneform);
    }

    /**
     * 获取一张表单导入总表列表
     *
     * @param aeaOneform
     * @param page
     * @return
     */
    @RequestMapping("/listOneform.do")
    public EasyuiPageInfo<AeaOneform> listUsedAeaItemBasicTreeByPage(AeaOneform aeaOneform, Page page) {

        logger.debug("分页查询，过滤条件为{}，对象为{}", aeaOneform);
        aeaOneform.setRootOrgId(SecurityContext.getCurrentOrgId());
        return aeaParStageOneformService.getAeaParOneformList(aeaOneform, page);
    }


    @RequestMapping("/addParStageOneform.do")
    public ResultForm addParStageOneform(String parStageId, String oneformId) throws Exception {

        if (parStageId != null && !"".equals(parStageId) && oneformId != null && !"".equals(oneformId)) {
            aeaParStageOneformService.addParStageOneform(parStageId, oneformId);
            return new ContentResultForm<>(true);
        } else {
            return new ContentResultForm<>(false, "", "参数有误！");
        }
    }

    @RequestMapping("/addMultiplyStageOneform.do")
    public ResultForm addMultiplyStageOneform(String parStageId, String oneformIds) throws Exception {

        if (parStageId != null && !"".equals(parStageId) && oneformIds != null && !"".equals(oneformIds)) {
            String[] oneformIdstr = oneformIds.split(",");
            for (int i = 0; i < oneformIdstr.length; i++) {
                String oneformId = oneformIdstr[i];
                aeaParStageOneformService.addParStageOneform(parStageId, oneformId);
            }

            return new ContentResultForm<>(true, oneformIds);
        } else {
            return new ContentResultForm<>(false, "", "参数有误！");
        }
    }

    @RequestMapping("/deleteStageOneformById.do")
    public ResultForm deleteStageOneformById(String id) throws Exception {

        if (StringUtils.isBlank(id)) {
            throw new InvalidParameterException("参数id为空!");
        }
        logger.debug("删除总表Form对象，对象id为：{}", id);
        aeaParStageOneformService.deleteAeaParStageOneformById(id);
        return new ResultForm(true);
    }

    @RequestMapping("/getAeaParStageOneform.do")
    public AeaParStageOneform getAeaParStageOneform(String id) throws Exception {
        if (id != null) {
            logger.debug("根据ID获取AeaParStageOneform对象，ID为：{}", id);
            return aeaParStageOneformService.getAeaParStageOneformById(id);
        } else {
            logger.debug("构建新的AeaParStageOneform对象");
            return new AeaParStageOneform();
        }
    }



    /**
     * 获取智能表单列表
     *
     * @param actStoForm
     * @param page
     * @return
     */
    @RequestMapping("/listStoForm.do")
    public EasyuiPageInfo<ActStoFormVo> listStoForm(ActStoFormVo actStoForm, Page page) {

        logger.debug("分页查询，过滤条件为{}，对象为{}", actStoForm);
        return aeaParStageOneformService.getActStoFormList(actStoForm, page);
    }


    /**
     * 导入智能表单
     *
     * @param aeaParStageItem
     * @return
     */
    @RequestMapping("/saveParStageItem.do")
    public ResultForm saveParStageItem(AeaParStageItem aeaParStageItem, BindingResult result) throws Exception {
        if (result.hasErrors()) {
            logger.error("保存智能表单关联出错");
            throw new InvalidParameterException(aeaParStageItem);
        }

        try{
            if (aeaParStageItem.getStageItemId() != null && !"".equals(aeaParStageItem.getStageItemId())) {

                    AeaParStageItemAdminService.updateAeaParStageItem(aeaParStageItem);
                    return new ContentResultForm<>(true, aeaParStageItem);

            }
        }catch(Exception e){
            return new ContentResultForm<>(false, "", "导入智能表单失败！");
        }

        return new ContentResultForm<>(false, "", "导入智能表单失败！");

    }

    @RequestMapping("/getAeaParStageItemListNoPage.do")
    public List<AeaParStageItem> getAeaParStageItemListNoPage(AeaParStageItem aeaParStageItem) {

        List<AeaParStageItem> list = aeaParStageOneformService.getAeaParStageItemListNoPage(aeaParStageItem);
        return list;
    }


    /**
     * 事项子表排序
     *
     * @param stageItemIds,sortNos
     * @return
     */
    @RequestMapping("/updateStageItemSortNos.do")
    public ResultForm updateStageItemSortNos(String[] stageItemIds, Long[] sortNos) {

        if(stageItemIds!=null&&stageItemIds.length>0
                &&sortNos!=null&&sortNos.length>0){
            for(int i=0; i<stageItemIds.length; i++){

                AeaParStageItem aeaParStageItem = new AeaParStageItem();
                aeaParStageItem.setStageItemId(stageItemIds[i]);
                aeaParStageItem.setSortNo(sortNos[i]);
                AeaParStageItemAdminService.updateAeaParStageItem(aeaParStageItem);

            }
            return new ResultForm(true);
        }
        return new ResultForm(false, "事项子表表单排序数据有问题,请检查!");
    }


    @RequestMapping("/deleteStoformByStageItemId.do")
    public ResultForm deleteStoformByStageItemId(AeaParStageItem aeaParStageItem) throws Exception {

        if (StringUtils.isBlank(aeaParStageItem.getStageItemId())) {
            throw new InvalidParameterException("参数id为空!");
        }
        logger.debug("删除事项子表与智能表单关联关系，对象id为：{}", aeaParStageItem.getStageItemId());
        aeaParStageItem.setSubFormId("");
        AeaParStageItemAdminService.updateAeaParStageItem(aeaParStageItem);
        return new ResultForm(true);
    }

}
