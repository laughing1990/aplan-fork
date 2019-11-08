package com.augurit.aplanmis.front.third.multidiscipline.controller;

import com.augurit.agcloud.framework.ui.result.ContentResultForm;
import com.augurit.agcloud.framework.ui.result.ResultForm;
import com.augurit.aplanmis.front.third.multidiscipline.service.ThirdApproveInfoService;
import com.augurit.aplanmis.front.third.multidiscipline.vo.ThirdApproveInfo;
import io.jsonwebtoken.lang.Assert;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Api(tags = "第三方对接")
@RestController
@RequestMapping("/rest")
@Slf4j
@CrossOrigin(value = "*", allowedHeaders = "*")
public class ThirdBusinessCooperationController {

    @Autowired
    private ThirdApproveInfoService approveInfoService;

    @GetMapping("/approve/info")
    @ApiOperation("业务协同对接 --> 获取审批信息")
    public ResultForm approveInfo(@ApiParam("项目编码") @RequestParam String projCode) {
        Assert.notNull(projCode, "项目编码不能为空");

        ThirdApproveInfo approveInfo = approveInfoService.getApproveInfo(projCode);
        return new ContentResultForm<>(true, approveInfo);
    }
}

