package com.augurit.aplanmis.mall.check.controller;

import com.augurit.agcloud.framework.ui.result.ContentResultForm;
import com.augurit.agcloud.framework.util.CollectionUtils;
import com.augurit.agcloud.framework.util.StringUtils;
import com.augurit.aplanmis.common.check.CheckItemResultInfo;
import com.augurit.aplanmis.common.check.CheckStageResultInfo;
import com.augurit.aplanmis.mall.check.service.RestAeaCheckerService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/rest/check")
@Api(value = "单项、阶段前置条件检测", tags = "单项前置条件检测")
public class AeaCheckerController {

    @Autowired
    private RestAeaCheckerService restAeaCheckerService;

    @GetMapping("/itemFrontCheck")
    @ApiOperation(value = "单项前置条件检测", httpMethod = "GET")
    public ContentResultForm<CheckItemResultInfo> itemFrontCheck(@RequestParam List<String> itemVerIds, @RequestParam String projInfoId) {
        try {

            if (CollectionUtils.isEmpty(itemVerIds)) return new ContentResultForm<>(false, null, "缺少参数：itemVerId");
            if (StringUtils.isBlank(projInfoId)) return new ContentResultForm<>(false, null, "缺少参数：projInfoId！");
            return restAeaCheckerService.itemFrontCheck(itemVerIds, projInfoId);
        } catch (Exception e) {
            e.printStackTrace();
            return new ContentResultForm<>(false, null, "系统内部发生异常！");
        }
    }

    @GetMapping("/stageFrontCheck")
    @ApiOperation(value = "阶段前置条件检测", httpMethod = "GET")
    public ContentResultForm<CheckStageResultInfo> stageFrontCheck(String stageId, String projInfoId) {
        try {

            if (StringUtils.isBlank(stageId)) return new ContentResultForm<>(false, null, "缺少参数：stageId");
            if (StringUtils.isBlank(projInfoId)) return new ContentResultForm<>(false, null, "缺少参数：projInfoId！");
            return restAeaCheckerService.stageFrontCheck(stageId, projInfoId);
        } catch (Exception e) {
            e.printStackTrace();
            return new ContentResultForm<>(false, null, "系统内部发生异常！");
        }
    }
}
