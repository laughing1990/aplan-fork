package com.augurit.aplanmis.front.itemFill.controller;

import com.augurit.agcloud.framework.ui.result.ContentResultForm;
import com.augurit.agcloud.framework.ui.result.ResultForm;
import com.augurit.aplanmis.common.domain.AeaHiItemFill;
import com.augurit.aplanmis.common.service.itemFill.AeaHiItemFillService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

@RestController
@RequestMapping("/rest/item/fill")
@Api(value = "容缺审核", tags = "网厅申报，容缺材料补齐，审批系统容缺审核接口")
@Slf4j
public class AeaHiItemFillController {

    @Autowired
    private AeaHiItemFillService aeaHiItemFillService;

    @ApiOperation(value = "容缺审核 --> 跳转容缺审核列表", notes = "容缺审核 --> 跳转容缺审核列表")
    @RequestMapping("/toleranceAuditIndex")
    public ModelAndView toleranceAuditIndex() {
        ModelAndView modelAndView = new ModelAndView("view/queryToleranceAuditIndex");
        return modelAndView;
    }

    @RequestMapping("/detail")
    @ApiOperation(value = "容缺审核 --> 视图列表点击进入审核页面，获取容缺审核详细信息，包括申报基本信息，材料不齐信息等")
    @ApiImplicitParams(
            value = {@ApiImplicitParam(name = "fillId", value = "事项容缺补齐实例id", dataType = "string", required = true)
            })
    public ResultForm getAeaHiItemFillDetail(String fillId) {
        try {
            Assert.hasText(fillId,"事项容缺补齐实例id不能为空。");
            AeaHiItemFill fillDetail = aeaHiItemFillService.getAeaHiItemFillDetail(fillId);
            return new ContentResultForm(true,fillDetail,"查询成功。");
        }catch (Exception e){
            log.error(e.getMessage(), e);
            return new ContentResultForm<>(false, null, e.getMessage());
        }
    }
}
