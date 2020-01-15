package com.augurit.aplanmis.front.itemFill.controller;

import com.augurit.agcloud.framework.ui.result.ResultForm;
import com.augurit.aplanmis.common.service.itemFill.AeaHiItemFillService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/rest/item/fill")
@Api(value = "容缺审核", tags = "网厅申报，容缺材料补齐，审批系统容缺审核接口")
public class AeaHiItemFillController {

    @Autowired
    private AeaHiItemFillService aeaHiItemFillService;

    @RequestMapping("/detail")
    @ApiOperation(value = "容缺审核 --> 视图列表点击进入审核页面，获取容缺审核详细信息，包括申报基本信息，材料不齐信息等")
    @ApiImplicitParams(
            value = {@ApiImplicitParam(name = "fillId", value = "事项容缺补齐实例id", dataType = "string", required = true)
            })
    public ResultForm getAeaHiItemFillDetail(String fillId) throws Exception{

        return new ResultForm(true);
    }

    @RequestMapping("/test")
    public ResultForm test(String applyinstId) throws Exception{
        aeaHiItemFillService.createAeaHiItemFill(applyinstId);
        return new ResultForm(true);
    }

}
