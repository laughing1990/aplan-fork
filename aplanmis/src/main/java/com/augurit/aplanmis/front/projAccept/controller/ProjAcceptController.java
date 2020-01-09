package com.augurit.aplanmis.front.projAccept.controller;

import com.augurit.agcloud.framework.ui.result.ContentResultForm;
import com.augurit.agcloud.framework.ui.result.ResultForm;
import com.augurit.agcloud.framework.util.StringUtils;
import com.augurit.aplanmis.common.service.projAccept.service.ProjAcceptService;
import com.augurit.aplanmis.common.service.projAccept.vo.ProjAcceptOpinionSummaryVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 竣工验收阶段相关逻辑
 */
@RestController
@RequestMapping("/projAccept")
@Api(value = "竣工验收阶段联合验收", tags = "竣工验收阶段-联合验收")
public class ProjAcceptController {
    @Autowired
    private ProjAcceptService projAcceptService;

    /**
     * 根据申报实例ID，获取竣工验收阶段汇总意见信息（只适合于竣工验收阶段）
     * @param applyinstId
     * @return
     * @throws Exception
     */
    @RequestMapping("/projAcceptOpinionSummary")
    @ApiOperation(value = "竣工验收 --> 获取验收意见汇总信息")
    @ApiImplicitParams(
            value = {@ApiImplicitParam(name = "applyinstId", value = "申请实例ID", dataType = "string", required = true)
    })
    public ResultForm projAcceptOpinionSummary(String applyinstId){
        if(StringUtils.isBlank(applyinstId))
            return new ResultForm(false,"申报实例ID参数不能为空！");

        ProjAcceptOpinionSummaryVo acceptOpinionSummaryVo = null;
        try {
            acceptOpinionSummaryVo = projAcceptService.caculateProjAcceptOpinionSummary(applyinstId);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResultForm(false,e.getMessage());
        }

        return new ContentResultForm<ProjAcceptOpinionSummaryVo>(true,acceptOpinionSummaryVo);
    }
}
