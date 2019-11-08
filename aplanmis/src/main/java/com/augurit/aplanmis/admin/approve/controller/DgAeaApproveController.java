package com.augurit.aplanmis.admin.approve.controller;


import com.augurit.agcloud.framework.ui.result.ContentResultForm;
import com.augurit.agcloud.framework.ui.result.ResultForm;
import com.augurit.aplanmis.common.service.admin.approve.DgAeaApproveService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;

@RequestMapping("/aea/approve")
@RestController
public class DgAeaApproveController {
    private static Logger logger = LoggerFactory.getLogger(DgAeaApproveController.class);
    @Autowired
    private DgAeaApproveService dgAeaApproveService;


    @RequestMapping("/getAeaStateAndItemsByStageId.do")
    public ResultForm getAeaStateAndItemsByStageId(String itemVerId, String currentBusiType) {
        try {
            Map<String, Object> aeaStateAndItems = dgAeaApproveService.getAeaStateAndItemsByStageId(itemVerId, currentBusiType);
            return new ContentResultForm<>(true, aeaStateAndItems);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResultForm(false);
        }
    }

}
