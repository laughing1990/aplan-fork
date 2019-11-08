package com.augurit.aplanmis.admin.item.controller;

import com.augurit.agcloud.framework.ui.result.ResultForm;
import com.augurit.agcloud.framework.util.StringUtils;
import com.augurit.aplanmis.common.domain.AeaItemFrontItem;
import com.augurit.aplanmis.common.service.admin.item.AeaItemFrontItemAdminService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
* 事项的前置检查事项-Controller 页面控制转发类
*/
@RestController
@RequestMapping("/aea/item/front")
public class AeaItemFrontAdminController {

private static Logger logger = LoggerFactory.getLogger(AeaItemFrontAdminController.class);

    @Autowired
    private AeaItemFrontItemAdminService aeaItemFrontService;

    @RequestMapping("/listItemsByItemVerId.do")
    public List<AeaItemFrontItem> listItemsByItemVerId(String itemVerId) {

        if (StringUtils.isNotBlank(itemVerId)) {
            return aeaItemFrontService.listItemsByItemVerId(itemVerId);
        }
        return null;
    }

    @RequestMapping("/batchSaveFrontItem.do")
    public ResultForm batchSaveFrontItem(String itemVerId, String[] frontItemVerIds, String[] sortNos) {

        if (frontItemVerIds != null && frontItemVerIds.length > 0) {
            if (StringUtils.isBlank(itemVerId)) {
                return new ResultForm(false, "参数itemVerId为空!");
            }
            aeaItemFrontService.batchSaveFrontItem(itemVerId, frontItemVerIds, sortNos);
        } else {
            aeaItemFrontService.batchDelItemByItemVerId(itemVerId);
        }
        return new ResultForm(true);
    }
}
