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
            acceptOpinionSummaryVo = projAcceptService.caculateProjAcceptOpinionSummary(applyinstId,null);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResultForm(false,e.getMessage());
        }

        return new ContentResultForm<ProjAcceptOpinionSummaryVo>(true,acceptOpinionSummaryVo);
    }

    /**
     * 生成联合验收终审意见书批文批复 接口
     * @param applyinstId 申请实例id
     * @throws Exception
     */
    @RequestMapping("/checkAcceptOpinionSummaryPwpf")
    @ApiOperation(value = "竣工验收 --> 检查当前申报是否已经生成联合验收终审意见书批文批复")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "applyinstId", value = "申请实例ID", dataType = "string", required = true)
            })
    public ResultForm checkAcceptOpinionSummaryPwpf(String applyinstId) throws Exception{
        boolean isHasPwpf = projAcceptService.checkOpinionSummaryPwpf(applyinstId);
        if(isHasPwpf)
            return new ResultForm(true,"当前审批已经存在联合验收终审意见书批文批复！");
        return new ResultForm(false,"当前审批还未生成联合验收终审意见书批文批复！");
    }
    /**
     * 生成联合验收终审意见书批文批复 接口
     * @param applyinstId 申请实例id
     * @param procinstId 验收节点子流程实例id
     * @throws Exception
     */
    @RequestMapping("/createAcceptOpinionSummaryPwpf")
    @ApiOperation(value = "竣工验收 --> 生成联合验收终审意见书批文批复")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "applyinstId", value = "申请实例ID", dataType = "string", required = true),
            @ApiImplicitParam(name = "procinstId", value = "申请实例ID", dataType = "string", required = true),
            @ApiImplicitParam(name = "override", value = "是否覆盖之前已经生成的批文批复,1是 0否", dataType = "string", required = true)
            })
    public ResultForm createAcceptOpinionSummaryPwpf(String applyinstId,String procinstId,String override) throws Exception{
        if(!"1".equals(override)){
            boolean isHasPwpf = projAcceptService.checkOpinionSummaryPwpf(applyinstId);
            if(isHasPwpf)
                return new ResultForm(false,"当前已存在《联合验收终审意见书》！是否重新生成！");
        }
        projAcceptService.createOpinionSummaryPwpf(applyinstId,procinstId);
        return new ResultForm(true,"操作成功！");
    }
}
