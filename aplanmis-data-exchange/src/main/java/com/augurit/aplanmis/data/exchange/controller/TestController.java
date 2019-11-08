package com.augurit.aplanmis.data.exchange.controller;

import com.augurit.aplanmis.common.domain.AeaItem;
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

    @RequestMapping("/findProj")
    public List<SpglDfxmsplcjdsxxxb> findProj(SpglDfxmsplcjdsxxxb spglDfxmsplcjdsxxxb) {
        return itemService.findSpglDfxmsplcjdsxxxb(spglDfxmsplcjdsxxxb);
    }

    @RequestMapping("/select/3")
    public List<SpglDfxmsplcjdsxxxb> select3() {
        SpglDfxmsplcjdsxxxb condi = new SpglDfxmsplcjdsxxxb();
        return spglDfxmsplcjdsxxxbService.listSpglDfxmsplcjdsxxxb(condi);
    }

    @RequestMapping("/import/1")
    public void importItem() {
        importService.importThemeVer(null, null);
    }

    @RequestMapping("/import/2")
    public void importItem2() {
        importService.importStage(null, null);
    }

    @RequestMapping("/import/3")
    public void importItem3() {
        importService.importItem(null, null);
    }

    @RequestMapping("/import/4")
    public void importItem4() {
        importService.importProj(null, null);
    }

    @RequestMapping("/import/5")
    public void importItem5() {
        importService.importUnit(null, null);
    }

    @RequestMapping("/import/6")
    public void importItem6() {
        importService.importIteminst(null, null);
    }

    @RequestMapping("/import/7")
    public void importItem7() {
        importService.importItemOpinion(null, null);
    }

    @RequestMapping("/import/8")
    public void importItem8() {
        importService.importOfficDoc(null, null);
    }

    @RequestMapping("/import/9")
    public void importItem9() {
        importService.importItemMatinst(null, null);
    }

    @RequestMapping("/import/all")
    public void importAllTable() {
        importService.importAllTable(null, null);
    }

    @RequestMapping("/find/item")
    public List<AeaItem> findItem1(String itemId) {
        return itemBasicService.findCarryOutItemByCatalogItemId(itemId);
    }

    @RequestMapping("/find/item/time_limit")
    public Long findItemTimeLimit(String itemId, String stageId) {
        return itemBasicService.findStageItemDueNumByItemIdAndStageId(itemId, stageId);
    }
}
