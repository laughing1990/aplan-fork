package com.augurit.aplanmis.mall.check.controller;

import com.augurit.agcloud.framework.ui.result.ResultForm;
import com.augurit.agcloud.framework.util.StringUtils;
import com.augurit.aplanmis.mall.check.service.RestAeaCheckerService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/rest/check")
@Api(value = "单项、阶段前置条件检测", tags = "单项前置条件检测")
public class AeaCheckerController {

    @Autowired
    private RestAeaCheckerService restAeaCheckerService;

    @PostMapping("/itemFrontCheck")
    @ApiOperation(value = "单项前置条件检测", httpMethod = "POST")
    public ResultForm itemFrontCheck(String itemVerIds, String projInfoId) {
        try {

            if (StringUtils.isBlank(itemVerIds)) return new ResultForm(false, "缺少参数：itemVerIds");
            if (StringUtils.isBlank(projInfoId)) return new ResultForm(false, "缺少参数：projInfoId！");
            return restAeaCheckerService.itemFrontCheck(itemVerIds, projInfoId);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResultForm(false, "系统内部发生异常！");
        }
    }

    @PostMapping("/stageFrontCheck")
    @ApiOperation(value = "阶段前置条件检测", httpMethod = "POST")
    public ResultForm stageFrontCheck(String stageId, String projInfoId) {
        try {

            if (StringUtils.isBlank(stageId)) return new ResultForm(false, "缺少参数：stageId");
            if (StringUtils.isBlank(projInfoId)) return new ResultForm(false, "缺少参数：projInfoId！");
            return restAeaCheckerService.stageFrontCheck(stageId, projInfoId);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResultForm(false, "系统内部发生异常！");
        }
    }
}
