package com.augurit.aplanmis.admin.par;

import com.augurit.agcloud.framework.ui.result.ContentResultForm;
import com.augurit.agcloud.framework.ui.result.ResultForm;
import com.augurit.agcloud.framework.util.StringUtils;
import com.augurit.aplanmis.common.domain.AeaParStateItem;
import com.augurit.aplanmis.common.service.admin.par.AeaParStateItemAdminService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * 情形与事项关联定义表-Controller 页面控制转发类
 */
@RestController
@RequestMapping("/aea/par/state/item")
public class AeaParStateItemAdminController {

    private static Logger logger = LoggerFactory.getLogger(AeaParStateItemAdminController.class);

    @Autowired
    private AeaParStateItemAdminService aeaParStateItemAdminService;


    @RequestMapping("/indexAeaParStateItem.do")
    public ModelAndView indexAeaParStateItem() {
        return new ModelAndView("aea/par/state/state_item_index");
    }

    @RequestMapping("/listAeaParStateItem.do")
    public PageInfo<AeaParStateItem> listAeaParStateItem(AeaParStateItem aeaParStateItem, Page page) {
        logger.debug("分页查询，过滤条件为{}，查询关键字为{}", aeaParStateItem);
        return aeaParStateItemAdminService.listAeaParStateItem(aeaParStateItem, page);
    }

    @RequestMapping("/listStateItemByStateId.do")
    public List<AeaParStateItem> listStateItemByStateId(String parStateId) {
        return aeaParStateItemAdminService.listStateItemByStateId(parStateId);
    }

    @RequestMapping("/getAeaParStateItem.do")
    public AeaParStateItem getAeaParStateItem(String id) {
        if (StringUtils.isNotBlank(id)) {
            logger.debug("根据ID获取AeaParStateItem对象，ID为：{}", id);
            return aeaParStateItemAdminService.getAeaParStateItemById(id);
        } else {
            logger.debug("构建新的AeaParStateItem对象");
            return new AeaParStateItem();
        }
    }

    @RequestMapping("/updateAeaParStateItem.do")
    public ResultForm updateAeaParStateItem(AeaParStateItem aeaParStateItem) {
        logger.debug("更新客户档案信息Form对象，对象为：{}", aeaParStateItem);
        aeaParStateItemAdminService.updateAeaParStateItem(aeaParStateItem);
        return new ResultForm(true);
    }


    /**
     * 保存或编辑情形与事项关联定义表
     *
     * @param aeaParStateItem 情形与事项关联定义表
     * @return 返回结果对象 包含结果信息
     */
    @RequestMapping("/saveAeaParStateItem.do")
    public ResultForm saveAeaParStateItem(AeaParStateItem aeaParStateItem) {
        if (StringUtils.isNotBlank(aeaParStateItem.getStateItemId())) {
            aeaParStateItemAdminService.updateAeaParStateItem(aeaParStateItem);
        } else {
            aeaParStateItem.setStateItemId(UUID.randomUUID().toString());
            aeaParStateItemAdminService.saveAeaParStateItem(aeaParStateItem);
        }
        return new ContentResultForm<>(true, aeaParStateItem);
    }

    @RequestMapping("/saveStateItemRel.do")
    public ResultForm saveStateItemRel(String parStateId, String stageItemId) {
        if (StringUtils.isNotBlank(parStateId) && StringUtils.isNotBlank(stageItemId)) {
            aeaParStateItemAdminService.saveStateItemRel(parStateId, stageItemId);
            return new ResultForm(true);
        }
        return new ResultForm(false, "传递参数parStateId、stageItemId不正确！");
    }

    @RequestMapping("/deleteAeaParStateItemById.do")
    public ResultForm deleteAeaParStateItemById(String id) {
        if (StringUtils.isNotBlank(id)) {
            logger.debug("删除情形与事项关联定义表Form对象，对象id为：{}", id);
            aeaParStateItemAdminService.deleteAeaParStateItemById(id);
            return new ResultForm(true);
        }
        return new ResultForm(false, "操作参数id为空!");
    }


    @RequestMapping("/saveAeaParStateItemByStateIds.do")
    public ResultForm saveAeaParStateItemByStateIds(String stageItemId, String stateIds) {
        if (StringUtils.isNotBlank(stateIds)) {
            aeaParStateItemAdminService.saveAeaParStateItemByStateIds(stageItemId, stateIds);
            return new ResultForm(true);
        }
        return new ResultForm(false, "操作参数id为空!");
    }


    @RequestMapping("/getParStateItem.do")
    public Map<String, Object> getParState(String stageId, String parentId, String showLevel) {
        try {
            return aeaParStateItemAdminService.getStageMatList(stageId, parentId, showLevel);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @RequestMapping("/getRemoveStateIds.do")
    public Map<String, Object> getRemoveStateIds(String stateId, String stageId) {
        try {
            return aeaParStateItemAdminService.getRemoveStateIds(stateId, stageId);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @RequestMapping("/deleteStateItemByStageItemId.do")
    public ResultForm deleteStateItemByStageItemId(String stageItemId) {
        if (StringUtils.isNotBlank(stageItemId)) {
            logger.debug("删除情形与事项关联定义表Form对象，对象id为：{}", stageItemId);
            aeaParStateItemAdminService.deleteStateItemByStageItemId(stageItemId);
            return new ResultForm(true);
        }
        return new ResultForm(false, "操作参数id为空!");
    }


    @RequestMapping("/saveAeaParStateItemByStageItemIds.do")
    public ResultForm saveAeaParStateItemByStageItemIds(String stageItemIds, String stateId, String stageId) {
        if (StringUtils.isNotBlank(stateId)) {
            aeaParStateItemAdminService.saveAeaParStateItemByStageItemIds(stateId, stageItemIds, stageId);
            return new ResultForm(true);
        }
        return new ResultForm(false, "操作参数id为空!");
    }

    @RequestMapping("/saveAeaParStateItemByStageMatItemIds.do")
    public ResultForm saveAeaParStateItemByStageMatItemIds(String stageId, String stateId,
                                                           String matId, String fileType,
                                                           String stageItemIds) {

        aeaParStateItemAdminService.saveAeaParStateItemByStageMatItemIds(stateId, stageItemIds, matId, stageId, fileType);
        return new ResultForm(true);
    }
}
