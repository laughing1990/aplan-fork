package com.augurit.aplanmis.front.basis.stage.controller;

import com.augurit.agcloud.framework.ui.result.ContentResultForm;
import com.augurit.agcloud.framework.util.StringUtils;
import com.augurit.aplanmis.front.basis.stage.service.RestStageService;
import com.augurit.aplanmis.front.basis.stage.vo.StageItemFormVo;
import com.augurit.aplanmis.front.basis.stage.vo.StageOneFormVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.GetMapping;
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
}
