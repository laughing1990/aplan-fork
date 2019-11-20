package com.augurit.aplanmis.admin.item.controller;

/**
 * @author tiantian
 * @date 2019/11/11
 */

import com.augurit.agcloud.framework.exception.InvalidParameterException;
import com.augurit.agcloud.framework.security.SecurityContext;
import com.augurit.agcloud.framework.ui.pager.EasyuiPageInfo;
import com.augurit.agcloud.framework.ui.pager.PageHelper;
import com.augurit.agcloud.framework.ui.result.ContentResultForm;
import com.augurit.agcloud.framework.ui.result.ResultForm;
import com.augurit.agcloud.framework.util.StringUtils;
import com.augurit.aplanmis.common.domain.AeaItemFrontItem;
import com.augurit.aplanmis.common.service.admin.item.AeaItemFrontItemAdminService;
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
 * 事项的前置检查事项-Controller 页面控制转发类
 */
@RestController
@RequestMapping("/aea/item/front/item")
public class AeaItemFrontItemAdminController {
    private static Logger logger = LoggerFactory.getLogger(AeaItemFrontItemAdminController.class);

    @Autowired
    private AeaItemFrontItemAdminService aeaItemFrontItemAdminService;

    @RequestMapping("/listAeaItemFrontItemByPage.do")
    public EasyuiPageInfo<AeaItemFrontItem> listAeaItemFrontItemByPage(AeaItemFrontItem aeaItemFrontItem, Page page){

        PageInfo<AeaItemFrontItem> pageInfo = aeaItemFrontItemAdminService.listAeaItemFrontItemByPage(aeaItemFrontItem, page);
        return PageHelper.toEasyuiPageInfo(pageInfo);
    }

    @RequestMapping("/listAeaItemFrontItemByNoPage.do")
    public List<AeaItemFrontItem> listAeaItemFrontItemByNoPage(AeaItemFrontItem aeaItemFrontItem){

        return aeaItemFrontItemAdminService.listAeaItemFrontItem(aeaItemFrontItem);
    }

    @RequestMapping("/getAeaItemFrontItem.do")
    public ResultForm getAeaItemFrontItem(String frontItemId) {
        try {
            if (StringUtils.isNotBlank(frontItemId)) {
                AeaItemFrontItem frontItem = aeaItemFrontItemAdminService.getAeaItemFrontItemByFrontItemId(frontItemId);
                return new ContentResultForm<>(true, frontItem);
            } else {
                return new ResultForm(false, "frontItemId不能为空");
            }
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return new ResultForm(false, e.getMessage());
        }
    }

    @RequestMapping("/saveOrUpdateAeaItemFrontItem.do")
    public ResultForm saveOrUpdateAeaItemFrontItem(AeaItemFrontItem aeaItemFrontItem) {
        try {
            if (StringUtils.isNotBlank(aeaItemFrontItem.getFrontItemId())) {
                aeaItemFrontItemAdminService.updateAeaItemFrontItem(aeaItemFrontItem);
            } else {
                aeaItemFrontItem.setFrontItemId(UUID.randomUUID().toString());
                aeaItemFrontItemAdminService.saveAeaItemFrontItem(aeaItemFrontItem);
            }
            return new ContentResultForm<>(true, aeaItemFrontItem);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return new ResultForm(false, e.getMessage());

        }
    }

    @RequestMapping("/deleteAeaItemFrontItemById.do")
    public ResultForm deleteAeaItemFrontItemById(String id) {
        try {
            if (StringUtils.isNotBlank(id)) {
                aeaItemFrontItemAdminService.deleteAeaItemFrontItemById(id);
            }
            return new ResultForm(true);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return new ResultForm(false, e.getMessage());

        }
    }

    @RequestMapping("/getMaxSortNo.do")
    public ResultForm getMaxSortNo(AeaItemFrontItem aeaItemFrontItem) {
        try {
            return new ContentResultForm<>(true, aeaItemFrontItemAdminService.getMaxSortNo(aeaItemFrontItem));
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return new ResultForm(false, e.getMessage());
        }
    }

    @RequestMapping("/batchSaveAeaItemFrontItem.do")
    public ResultForm batchSaveAeaItemFrontItem(String itemVerId,String frontCkItemVerIds) {
        try {
            aeaItemFrontItemAdminService.batchSaveAeaItemFrontItem(itemVerId,frontCkItemVerIds);
            return new ResultForm(true);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return new ResultForm(false, e.getMessage());

        }
    }

    @RequestMapping("/changIsActive.do")
    public ResultForm changIsActive(String id) {

        if (StringUtils.isBlank(id)) {
            throw new InvalidParameterException("参数id为空!");
        }
        aeaItemFrontItemAdminService.changIsActive(id, SecurityContext.getCurrentOrgId());
        return new ResultForm(true);
    }
}
