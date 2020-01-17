package com.augurit.aplanmis.front.itemFill.controller;

import com.augurit.agcloud.framework.ui.result.ContentResultForm;
import com.augurit.agcloud.framework.ui.result.ResultForm;
import com.augurit.aplanmis.common.domain.AeaHiItemFill;
import com.augurit.aplanmis.common.domain.AeaHiItemFillDueIninst;
import com.augurit.aplanmis.common.service.itemFill.AeaHiItemFillService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

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

    @ApiOperation(value = "容缺审核 --> 跳转容缺审核详情页", notes = "容缺审核 --> 跳转容缺审核详情页")
    @RequestMapping("/toleranceDetailIndex")
    @ApiImplicitParams(
            value = {@ApiImplicitParam(name = "fillId", value = "事项容缺补齐实例id", dataType = "string", required = true)
    })
    public ModelAndView toleranceDetailIndex(String fillId) {
        ModelAndView modelAndView = new ModelAndView("tolerance/index");
        modelAndView.addObject("fillId",fillId);
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

    @PostMapping("/saveLeranceAproveOpinion")
    @ApiOperation(value = "容缺审核 --> 保存容缺审核结果")
    @ApiImplicitParams(value =   {
            @ApiImplicitParam(name = "fillDueList", value = "容缺补齐材料实例集合", paramType = "body", allowMultiple = true, dataType = "AeaHiItemFillDueIninst")
    })
    public ResultForm saveLeranceAproveOpinion(@RequestBody List<AeaHiItemFillDueIninst> fillDueList) {
        try {
            aeaHiItemFillService.saveLeranceAproveOpinion(fillDueList);
            return new ResultForm(true,"保存成功。");
        }catch (Exception e){
            log.error(e.getMessage(), e);
            return new ContentResultForm<>(false, null, "保存失败："+e.getMessage());
        }
    }
}
