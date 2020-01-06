package com.augurit.aplanmis.mall.userCenter.controller;

import com.augurit.agcloud.framework.ui.result.ContentResultForm;
import com.augurit.aplanmis.common.domain.AeaProjApplySplit;
import com.augurit.aplanmis.mall.userCenter.service.RestAeaProjAgentService;
import com.augurit.aplanmis.mall.userCenter.vo.SplitProjInfoParamVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("rest/user/proj/split")
@Api(value = "拆分子工程申请接口",tags = "拆分工程申请 --> 拆分工程申请接口")
public class RestProjSplitController {
    Logger logger= LoggerFactory.getLogger(RestProjSplitController.class);

    @Autowired
    RestAeaProjAgentService restAeaProjAgentService;

    @PostMapping("start")
    @ApiOperation(value = "拆分工程申请 --> 拆分工程申请接口")
    public ContentResultForm saveProjInfoAndInitProjApplySplit(@Valid @RequestBody SplitProjInfoParamVo splitProjInfoParamVo) {
        try {
            AeaProjApplySplit aeaProjApplySplit=restAeaProjAgentService.saveProjInfoAndInitProjApplySplit(splitProjInfoParamVo);
            return new ContentResultForm(true,aeaProjApplySplit);
        } catch (Exception e) {
            logger.error(e.getMessage(),e);
            return new ContentResultForm(false,"","拆分工程申请接口异常");
        }
    }

}
