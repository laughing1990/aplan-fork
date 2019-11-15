package com.augurit.aplanmis.supermarket.projPurchase.controller;

import com.augurit.agcloud.framework.ui.result.ContentResultForm;
import com.augurit.agcloud.framework.ui.result.ResultForm;
import com.augurit.aplanmis.common.domain.AeaItemMat;
import com.augurit.aplanmis.common.service.mat.AeaItemMatService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@CrossOrigin(origins = "*", maxAge = 3600)
@Api(description = "项目需求采购管理接口-材料相关")
@RequestMapping("/supermarket/purchase/mat")
@RestController
public class MatController {
    @Autowired
    private AeaItemMatService aeaItemMatService;

    @ApiOperation("获取中介事项材料-不分情形")
    @GetMapping("/")
    public ResultForm getAeaItemInoutMatListByItemVerId(String itemVerId) throws Exception {
        String[] itemVerIds = {itemVerId};
        List<AeaItemMat> matList = aeaItemMatService.getMatListByItemVerIds(itemVerIds, "1", null);
        return new ContentResultForm<>(true, matList, "success");
    }

}
