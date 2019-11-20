package com.augurit.aplanmis.admin.par;

import com.augurit.agcloud.framework.exception.InvalidParameterException;
import com.augurit.agcloud.framework.security.SecurityContext;
import com.augurit.agcloud.framework.ui.pager.EasyuiPageInfo;
import com.augurit.agcloud.framework.ui.pager.PageHelper;
import com.augurit.agcloud.framework.ui.result.ContentResultForm;
import com.augurit.agcloud.framework.ui.result.ResultForm;
import com.augurit.agcloud.framework.util.StringUtils;
import com.augurit.aplanmis.common.domain.AeaParFrontItem;
import com.augurit.aplanmis.common.service.admin.par.AeaParFrontItemService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

/**
 * 阶段事项前置检测表-Controller 页面控制转发类
 */
@RestController
@RequestMapping("/aea/par/front/item")
public class AeaParFrontItemController {

    private static Logger logger = LoggerFactory.getLogger(AeaParFrontItemController.class);

    @Autowired
    private AeaParFrontItemService aeaParFrontItemService;

    @RequestMapping("/listAeaParFrontItemByPage.do")
    public EasyuiPageInfo<AeaParFrontItem> listAeaParFrontItemByPage(AeaParFrontItem aeaParFrontItem, Page page) throws Exception {

        PageInfo<AeaParFrontItem> pageInfo = aeaParFrontItemService.listAeaParFrontItemVoByPage(aeaParFrontItem, page);
        return PageHelper.toEasyuiPageInfo(pageInfo);
    }

    @RequestMapping("/getAeaParFrontItem.do")
    public ResultForm getAeaParFrontItem(String frontItemId) {

        try {
            if (StringUtils.isNotBlank(frontItemId)) {
                return new ContentResultForm<>(true, aeaParFrontItemService.getAeaParFrontItemVoByFrontItemId(frontItemId));
            } else {
                return new ResultForm(false, "frontItemId不能为空");
            }
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return new ResultForm(false, e.getMessage());
        }
    }

    @RequestMapping("/saveOrUpdateAeaParFrontItem.do")
    public ResultForm saveOrUpdateAeaParFrontItem(AeaParFrontItem aeaParFrontItem) {

        try {
            if (StringUtils.isNotBlank(aeaParFrontItem.getFrontItemId())) {
                aeaParFrontItemService.updateAeaParFrontItem(aeaParFrontItem);
            } else {
                aeaParFrontItem.setFrontItemId(UUID.randomUUID().toString());
                aeaParFrontItemService.saveAeaParFrontItem(aeaParFrontItem);
            }
            return new ContentResultForm<>(true, aeaParFrontItem);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return new ResultForm(false, e.getMessage());

        }
    }

    @RequestMapping("/deleteAeaParFrontItemById.do")
    public ResultForm deleteAeaParFrontItemById(String id) {

        try {
            logger.debug("删除阶段事项前置检测表Form对象，对象id为：{}", id);
            if (StringUtils.isNotBlank(id)) {
                aeaParFrontItemService.deleteAeaParFrontItemById(id);
            }
            return new ResultForm(true);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return new ResultForm(false, e.getMessage());

        }
    }

    @RequestMapping("/getMaxSortNo.do")
    public ResultForm getMaxSortNo(AeaParFrontItem aeaParFrontItem) {

        try {
            return new ContentResultForm<>(true, aeaParFrontItemService.getMaxSortNo(aeaParFrontItem));
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return new ResultForm(false, e.getMessage());
        }
    }

    @RequestMapping("/batchSaveAeaParFrontItem.do")
    public ResultForm batchSaveAeaParFrontItem(String stageId,String itemVerIds) {

        try {
            aeaParFrontItemService.batchSaveAeaParFrontItem(stageId,itemVerIds);
            return new ResultForm(true);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return new ResultForm(false, e.getMessage());

        }
    }

    /**
     * 前置检测事项真正使用
     *
     * @param stageId
     * @param itemVerIds
     * @return
     */
    @RequestMapping("/batchSaveFrontItem.do")
    public ResultForm batchSaveFrontItem(String stageId, String[] itemVerIds, Long[] sortNos) {

        if (itemVerIds != null && itemVerIds.length > 0) {
            if (StringUtils.isBlank(stageId)) {
                return new ResultForm(false, "参数stageId为空!");
            }
            aeaParFrontItemService.batchSaveFrontItem(stageId, itemVerIds, sortNos);
        } else {
            aeaParFrontItemService.batchDelItemByStageId(stageId, SecurityContext.getCurrentOrgId());
        }
        return new ResultForm(true);
    }

    @RequestMapping("/updateAeaParFrontItemSortNos.do")
    public ResultForm updateAeaParFrontItemSortNos(String[] ids, Long[] sortNos) throws Exception {

        if (ids != null && ids.length > 0 && sortNos != null && sortNos.length > 0) {
            for (int i = 0; i < ids.length; i++) {
                AeaParFrontItem aeaParFrontItem = new AeaParFrontItem();
                aeaParFrontItem.setFrontItemId(ids[i]);
                aeaParFrontItem.setSortNo(sortNos[i]);
                aeaParFrontItemService.updateAeaParFrontItem(aeaParFrontItem);
            }
            return new ResultForm(true);
        }
        return new ResultForm(false, "传递排序数据有问题,请检查!");
    }

    @RequestMapping("/listAeaParFrontItemByNoPage.do")
    public List<AeaParFrontItem> listAeaParFrontItemByNoPage(AeaParFrontItem aeaParFrontItem) throws Exception {

        List<AeaParFrontItem> list = aeaParFrontItemService.listAeaParFrontItemVoByNoPage(aeaParFrontItem);
        return list;
    }

    @RequestMapping("/changIsActive.do")
    public ResultForm changIsActive(String id) {

        if (StringUtils.isBlank(id)) {
            throw new InvalidParameterException("参数id为空!");
        }
        aeaParFrontItemService.changIsActive(id, SecurityContext.getCurrentOrgId());
        return new ResultForm(true);
    }
}
