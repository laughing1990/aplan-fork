package com.augurit.aplanmis.data.exchange.controller;

import com.augurit.aplanmis.common.domain.AeaItem;
import com.augurit.aplanmis.common.service.project.AeaProjStageService;
import com.augurit.aplanmis.data.exchange.domain.spgl.SpglDfxmsplcjdsxxxb;
import com.augurit.aplanmis.data.exchange.service.ImportService;
import com.augurit.aplanmis.data.exchange.service.aplanmis.ItemBasicService;
import com.augurit.aplanmis.data.exchange.service.aplanmis.StageItemService;
import com.augurit.aplanmis.data.exchange.service.spgl.SpglDfxmsplcjdsxxxbService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author yinlf
 * @Date 2019/9/21
 */
@RestController
@RequestMapping("/test")
public class TestController {

    @Autowired
    StageItemService itemService;

    @Autowired
    SpglDfxmsplcjdsxxxbService spglDfxmsplcjdsxxxbService;

    @Autowired
    ImportService importService;

    @Autowired
    ItemBasicService itemBasicService;

    @Autowired
    AeaProjStageService aeaProjStageService;

    @RequestMapping("/findProj")
    public List<SpglDfxmsplcjdsxxxb> findProj(SpglDfxmsplcjdsxxxb spglDfxmsplcjdsxxxb) {
        return itemService.findSpglDfxmsplcjdsxxxb(spglDfxmsplcjdsxxxb);
    }

    @RequestMapping("/select/3")
    public List<SpglDfxmsplcjdsxxxb> select3() {
        SpglDfxmsplcjdsxxxb condi = new SpglDfxmsplcjdsxxxb();
        return spglDfxmsplcjdsxxxbService.listSpglDfxmsplcjdsxxxb(condi);
    }

    @RequestMapping("/find/item")
    public List<AeaItem> findItem1(String itemId) {
        return itemBasicService.findCarryOutItemByCatalogItemId(itemId);
    }

    @RequestMapping("/find/item/time_limit")
    public Long findItemTimeLimit(String itemId, String stageId) {
        return itemBasicService.findStageItemDueNumByItemIdAndStageId(itemId, stageId);
    }

    @RequestMapping("/calculate")
    public void calculate() {
        aeaProjStageService.calculateAllProjStageState();
    }
}
