package com.augurit.aplanmis.front.basis.stage.controller;

import com.augurit.agcloud.framework.ui.result.ContentResultForm;
import com.augurit.agcloud.framework.ui.result.ResultForm;
import com.augurit.agcloud.framework.util.StringUtils;
import com.augurit.aplanmis.common.domain.AeaApplyinstForminst;
import com.augurit.aplanmis.common.domain.AeaItemPartform;
import com.augurit.aplanmis.common.domain.AeaParStagePartform;
import com.augurit.aplanmis.front.basis.stage.service.RestStageService;
import com.augurit.aplanmis.front.basis.stage.vo.StageItemFormVo;
import com.augurit.aplanmis.front.basis.stage.vo.StageOneFormVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequestMapping("/rest/stage")
@RestController
@Api(value = "阶段接口", tags = "申报-[主题 阶段 事项]")
public class RestStageController {

    @Autowired
    private RestStageService restStageService;

    @GetMapping("/oneforms")
    @ApiImplicitParam(name = "stageId", value = "阶段id")
    @ApiOperation("根据阶段id查询关联的表单")
    public ContentResultForm<List<StageOneFormVo>> findStageOneFormsByStageId(@RequestParam String stageId) {
        Assert.isTrue(StringUtils.isNotBlank(stageId), "stageId is null");
        return new ContentResultForm<>(true, restStageService.findStageOneFormsByStageId(stageId), "Query stage forms success.");
    }

    @GetMapping("/item/form")
    @ApiImplicitParam(name = "stageId", value = "阶段id")
    @ApiOperation("根据阶段id查询阶段下的事项与表单的关联关系")
    public ContentResultForm<List<StageItemFormVo>> findStageItemFormsByStageId(@RequestParam String stageId) {
        Assert.isTrue(StringUtils.isNotBlank(stageId), "stageId is null");
        return new ContentResultForm<>(true, restStageService.findStageItemFormsByStageId(stageId));
    }

    @GetMapping("/part/forms")
    @ApiImplicitParam(name = "stageId", value = "阶段id")
    @ApiOperation("根据阶段id查询关联的扩展表单")
    public ContentResultForm<List<AeaParStagePartform>> listAeaParStagePartformsByStageId(@RequestParam String stageId) {
        Assert.isTrue(StringUtils.isNotBlank(stageId), "stageId is null");
        return new ContentResultForm<>(true, restStageService.listAeaParStagePartformsByStageId(stageId), "success.");
    }

    @GetMapping("/item/part/forms")
    @ApiImplicitParam(name = "itemVerIds", value = "事项版本id列表")
    @ApiOperation("根据事项本版id查询关联的事项表单")
    public ContentResultForm<List<AeaItemPartform>> listAeaItemPartformsByItemVerIds(@RequestParam List<String> itemVerIds) {
        ContentResultForm<List<AeaItemPartform>> result = new ContentResultForm<>(true);
        if (itemVerIds == null || itemVerIds.size() < 1) {
            return result;
        }
        return new ContentResultForm<>(true, restStageService.listAeaItemPartformsByItemVerIds(itemVerIds), "success.");
    }

    @PostMapping("/bind/forminst")
    @ApiOperation("申报实例与表单实例关联")
    public ResultForm bindForminst(@RequestBody AeaApplyinstForminst aeaApplyinstForminst) {
        try {
            restStageService.bindForminst(aeaApplyinstForminst);
        } catch (Exception e) {
            return new ResultForm(false, e.getMessage());
        }
        return new ResultForm(true, "success");
    }
}
