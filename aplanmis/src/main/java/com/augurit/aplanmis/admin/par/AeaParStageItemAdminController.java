package com.augurit.aplanmis.admin.par;

import com.augurit.agcloud.bpm.common.constant.ActStoConstant;
import com.augurit.agcloud.framework.exception.InvalidParameterException;
import com.augurit.agcloud.framework.ui.pager.EasyuiPageInfo;
import com.augurit.agcloud.framework.ui.result.ContentResultForm;
import com.augurit.agcloud.framework.ui.result.ResultForm;
import com.augurit.agcloud.framework.ui.ztree.ZtreeNode;
import com.augurit.agcloud.framework.util.StringUtils;
import com.augurit.aplanmis.common.domain.AeaParStageItem;
import com.augurit.aplanmis.common.service.admin.par.AeaParStageItemAdminService;
import com.github.pagehelper.Page;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;
import java.util.UUID;

/**
 * -Controller 页面控制转发类
 */
@RestController
@RequestMapping("/aea/par/stage/item")
public class AeaParStageItemAdminController {

    private static Logger logger = LoggerFactory.getLogger(AeaParStageItemAdminController.class);

    @Autowired
    private AeaParStageItemAdminService aeaParStageItemAdminService;


    @RequestMapping("/indexAeaParStageItem.do")
    public ModelAndView indexAeaParStageItem() {
        return new ModelAndView("aea/par/stage/stage_item_index");
    }

    /**
     * 根据阶段定义id查询事项
     */
    @RequestMapping("/listAeaParStageItem.do")
    public EasyuiPageInfo<AeaParStageItem> listAeaParStageItem(AeaParStageItem aeaParStageItem, Page page) {
        logger.debug("分页查询，过滤条件为{}，查询关键字为{}", aeaParStageItem);
        return aeaParStageItemAdminService.listAeaParStageItem(aeaParStageItem, page);
    }

    @RequestMapping("/getAeaParStageItem.do")
    public AeaParStageItem getAeaParStageItem(String id) {
        if (id != null) {
            logger.debug("根据ID获取AeaParStageItem对象，ID为：{}", id);
            return aeaParStageItemAdminService.getAeaParStageItemById(id);
        } else {
            logger.debug("构建新的AeaParStageItem对象");
            return new AeaParStageItem();
        }
    }

    @RequestMapping("/updateAeaParStageItem.do")
    public ResultForm updateAeaParStageItem(AeaParStageItem aeaParStageItem) {
        logger.debug("更新客户档案信息Form对象，对象为：{}", aeaParStageItem);
        aeaParStageItemAdminService.updateAeaParStageItem(aeaParStageItem);
        return new ResultForm(true);
    }


    /**
     * 保存或编辑
     *
     * @return 返回结果对象 包含结果信息
     * @
     */
    @RequestMapping("/saveAeaParStageItem.do")
    public ResultForm saveAeaParStageItem(AeaParStageItem aeaParStageItem) {
        if (aeaParStageItem.getStageItemId() != null && !"".equals(aeaParStageItem.getStageItemId())) {
            aeaParStageItemAdminService.updateAeaParStageItem(aeaParStageItem);
        } else {
            aeaParStageItem.setStageItemId(UUID.randomUUID().toString());
            aeaParStageItemAdminService.saveAeaParStageItem(aeaParStageItem);
        }
        return new ContentResultForm<>(true, aeaParStageItem);
    }

    @RequestMapping("/batchSaveStageItem.do")
    public ResultForm batchSaveStageItem(String stageId, String[] itemIds, String[] sortNos,String isOptionItem) {

        if (itemIds != null && itemIds.length > 0) {
            if (StringUtils.isBlank(stageId)) {
                return new ResultForm(false, "参数阶段id为空!");
            }
            aeaParStageItemAdminService.batchSaveStageItem(stageId, itemIds, sortNos,isOptionItem);
        } else {
            aeaParStageItemAdminService.batchDeleteOneTypeStageItem(stageId, isOptionItem);
        }
        return new ResultForm(true);
    }

    @RequestMapping("/handleStageItemsToOnly.do")
    public ResultForm handleStageItemsToOnly(String stageId, String isOptionItem) {

        if (StringUtils.isBlank(stageId)) {
            throw new InvalidParameterException("参数stageId为空!");
        }
        if(StringUtils.isBlank(isOptionItem)){
            throw new InvalidParameterException("参数isOptionItem为空!");
        }
        aeaParStageItemAdminService.handleStageItemsToOnly(stageId, isOptionItem);
        return new ResultForm(true);
    }

    @RequestMapping("/deleteAeaParStageItemById.do")
    public ResultForm deleteAeaParStageItemById(String id) {
        logger.debug("删除Form对象，对象id为：{}", id);
        if (id != null) {
            aeaParStageItemAdminService.deleteAeaParStageItemById(id);
        }
        return new ResultForm(true);
    }

    @RequestMapping("/listAeaStageItemByStageId.do")
    public List<AeaParStageItem> listAeaStageItemByStageId(String stageId, String isOptionItem) {

        if (StringUtils.isNotBlank(stageId)) {
            return aeaParStageItemAdminService.listAeaStageItemByStageId(stageId,isOptionItem);
        }
        return null;
    }

    @RequestMapping("/listStageItemTreeByStageId.do")
    public List<ZtreeNode> listStageItemTreeByStageId(String stageId) {

        if (StringUtils.isNotBlank(stageId)) {
            return aeaParStageItemAdminService.listStageItemTreeByStageId(stageId);
        }
        return null;
    }

    /**
     * 根据阶段id获取绑定的事项树，报错标准事项和实施事项
     * @param stageId
     * @return
     */
    @RequestMapping("/getStageItemTreeByStageId.do")
    public List<ZtreeNode> getStageItemTreeByStageId(String stageId) {

        if (StringUtils.isNotBlank(stageId)) {
            return aeaParStageItemAdminService.getStageItemTreeByStageId(stageId);
        }
        return null;
    }

    @RequestMapping("/listAeaParStageItemWhichBindState.do")
    public EasyuiPageInfo<AeaParStageItem> listAeaParStageItemWhichBindState(AeaParStageItem aeaParStageItem, int pageNum, int pageSize) {
        Page page = new Page();
        page.setPageSize(pageSize);
        page.setPageNum(pageNum);
        return aeaParStageItemAdminService.listAeaParStageItemWhichBindState(aeaParStageItem, page);
    }

    @RequestMapping("/listAeaStageItemInfoByStageId.do")
    public List<AeaParStageItem> listAeaStageItemInfoByStageId(String stageId) {

        if (StringUtils.isNotBlank(stageId)) {
            return aeaParStageItemAdminService.listAeaStageItemInfoByStageId(stageId);
        }
        return null;
    }

    @RequestMapping("/listAeaParStageItemWithoutBindState.do")
    public List<AeaParStageItem> listAeaStageItemWithoutBindState(String stageId) {

        if (StringUtils.isNotBlank(stageId)) {
            return aeaParStageItemAdminService.listAeaParStageItemWithoutBindState(stageId);
        }
        return null;
    }


    @RequestMapping("/listAeaParStageItemEasyuiPageInfo.do")
    public EasyuiPageInfo<AeaParStageItem> listAeaParStageItemEasyuiPageInfo(AeaParStageItem aeaParStageItem, int pageNum, int pageSize) {

        Page page = new Page();
        page.setPageSize(pageSize);
        page.setPageNum(pageNum);
        return aeaParStageItemAdminService.listAeaParStageItem(aeaParStageItem, page);
    }

    @RequestMapping("/listStageAllItemByPage.do")
    public EasyuiPageInfo<AeaParStageItem> listStageAllItemByPage(AeaParStageItem aeaParStageItem, int pageNum, int pageSize) {

        Page page = new Page();
        page.setPageSize(pageSize);
        page.setPageNum(pageNum);
        return aeaParStageItemAdminService.listStageAllItem(aeaParStageItem, page);
    }

    @RequestMapping("/listAeaParStageItemByNoPage.do")
    public List<AeaParStageItem> listAeaParStageItemByNoPage(AeaParStageItem aeaParStageItem) {

        return aeaParStageItemAdminService.listAeaStageItemCondition(aeaParStageItem);
    }

    @RequestMapping("/listAeaParStageMatItemEasyuiPageInfo.do")
    public EasyuiPageInfo<AeaParStageItem> listAeaParStageMatItemEasyuiPageInfo(AeaParStageItem aeaParStageItem, int pageNum, int pageSize) {

        Page page = new Page();
        page.setPageSize(pageSize);
        page.setPageNum(pageNum);
        return aeaParStageItemAdminService.listAeaParStageMatItem(aeaParStageItem, page);
    }

    /**
     * 选择与该材料相关的事项
     * 
     * @param stageId
     * @param isOptionItem
     * @param inId
     * @return
     */
    @RequestMapping("/listStageItemByInId.do")
    public List<AeaParStageItem> listStageItemByInId(String stageId, String isOptionItem, String inId) {

        if (StringUtils.isNotBlank(stageId) && StringUtils.isNotBlank(inId)) {
            return aeaParStageItemAdminService.listStageItemByInId(stageId, isOptionItem, inId);
        }
        return null;
    }

    /**
     * 保存或编辑智能表单与事项关联
     *
     * @return 返回结果对象 包含结果信息
     * @
     */
    @RequestMapping("/saveAeaParStageItemBySubformId.do")
    public ResultForm saveAeaParStageItemBySubformId(AeaParStageItem aeaParStageItem, String formId, String operation) {

        ResultForm result = new ContentResultForm<>(true, aeaParStageItem);
        aeaParStageItem.setSubFormId(formId);
        if(ActStoConstant.SMART_FORM_ENTITY_OPERATION_NEW.equals(operation)){
            if (StringUtils.isNotBlank(aeaParStageItem.getStageItemId())) {
                aeaParStageItemAdminService.updateAeaParStageItem(aeaParStageItem);
                result = new ContentResultForm<>(true, aeaParStageItem);
            } else {
                result = new ResultForm(false, "智能表单与事项关联表Id为空!");
            }
        }
        else if(ActStoConstant.SMART_FORM_ENTITY_OPERATION_UPDATE.equals(operation)){
            result = new ContentResultForm<>(true, aeaParStageItem);
        }
        return result;
    }
}