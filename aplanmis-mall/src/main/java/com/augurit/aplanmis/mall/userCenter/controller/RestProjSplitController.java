package com.augurit.aplanmis.mall.userCenter.controller;

import com.augurit.agcloud.framework.ui.result.ContentResultForm;
import com.augurit.agcloud.framework.ui.result.ResultForm;
import com.augurit.agcloud.framework.util.StringUtils;
import com.augurit.aplanmis.common.domain.AeaProjApplySplit;
import com.augurit.aplanmis.common.domain.AeaProjInfo;
import com.augurit.aplanmis.common.utils.SessionUtil;
import com.augurit.aplanmis.common.vo.LoginInfoVo;
import com.augurit.aplanmis.mall.userCenter.service.RestAeaProjSplitService;
import com.augurit.aplanmis.mall.userCenter.vo.SplitProjInfoParamVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

@RestController
@RequestMapping("rest/user/proj/split")
@Api(value = "拆分子工程申请接口",tags = "拆分工程申请 --> 拆分工程申请接口")
public class RestProjSplitController {
    Logger logger= LoggerFactory.getLogger(RestProjSplitController.class);

    @Autowired
    RestAeaProjSplitService restAeaProjSplitService;

    @PostMapping("start")
    @ApiOperation(value = "拆分工程申请 --> 拆分工程申请接口")
    public ResultForm saveProjInfoAndInitProjApplySplit(@Valid @RequestBody SplitProjInfoParamVo splitProjInfoParamVo,HttpServletRequest request) {
        LoginInfoVo loginVo = SessionUtil.getLoginInfo(request);
        if(StringUtils.isBlank(loginVo.getUid()))  return new ResultForm(false,"为获取到统一认证登录账号uid");
        splitProjInfoParamVo.setUid(loginVo.getUid());
        try {
            AeaProjApplySplit aeaProjApplySplit=restAeaProjSplitService.saveProjInfoAndInitProjApplySplit(splitProjInfoParamVo);
            return new ContentResultForm(true,aeaProjApplySplit);
        } catch (Exception e) {
            logger.error(e.getMessage(),e);
            return new ContentResultForm(false,"","拆分工程申请接口异常");
        }
    }

    @GetMapping("getFrontStageProjInfo")
    @ApiOperation(value = "拆分工程申请 --> 查询上一阶段的工程信息")
    @ApiImplicitParams({
            @ApiImplicitParam(value = "阶段编号 1 工程建设许可阶段 2 施工许可阶段", name = "stageNo", required = true, dataType = "string"),
            @ApiImplicitParam(value = "主题ID(项目类型ID)", name = "themeId", required = true, dataType = "string"),
            @ApiImplicitParam(value = "项目代码", name = "localCode", required = true, dataType = "string")
    })
    public ContentResultForm getFrontStageProjInfo(String stageNo, String themeId, String localCode, HttpServletRequest request) {
        try {
            AeaProjInfo aeaProjInfo=restAeaProjSplitService.getFrontStageProjInfo(stageNo,themeId,localCode,request);
            return new ContentResultForm(true,aeaProjInfo);
        } catch (Exception e) {
            logger.error(e.getMessage(),e);
            return new ContentResultForm(false,"","查询上一阶段的工程信息接口异常");
        }
    }
}
