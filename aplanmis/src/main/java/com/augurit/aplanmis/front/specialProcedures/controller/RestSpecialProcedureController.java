package com.augurit.aplanmis.front.specialProcedures.controller;

import com.augurit.agcloud.bsc.domain.BscDicCodeItem;
import com.augurit.agcloud.framework.security.SecurityContext;
import com.augurit.agcloud.framework.ui.result.ContentResultForm;
import com.augurit.agcloud.framework.ui.result.ResultForm;
import com.augurit.agcloud.framework.util.StringUtils;
import com.augurit.agcloud.opus.common.domain.OpuOmUser;
import com.augurit.aplanmis.front.specialProcedures.service.RestSpecialRrocedureService;
import com.augurit.aplanmis.front.specialProcedures.vo.SpecialParamVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;
import java.util.Map;

@RestController
@Slf4j
@RequestMapping("/rest/specialProcedure")
@Api(tags = "特殊程序入口")
public class RestSpecialProcedureController {


    @Autowired
    private RestSpecialRrocedureService specialRrocedureService;

    @ApiOperation("特殊程序页面")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "applyinstId", value = "申请实例id", readOnly = true, dataType = "string", paramType = "query", required = true),
            @ApiImplicitParam(name = "iteminstId", value = "事项实例id", readOnly = true, dataType = "string", paramType = "query", required = true),
            @ApiImplicitParam(name = "isApprover", value = "是否审批人员【0: 窗口人员; 1: 审批人员】", readOnly = true, dataType = "string", paramType = "query", required = true)

    })
    @GetMapping("/index")
    public ModelAndView singleApplyIndex(String applyinstId,String iteminstId,String isApprover,String taskId,String processInstanceId){
        ModelAndView modelAndView=new ModelAndView("specialProcedures/index");
        modelAndView.addObject("applyinstId",applyinstId);
        modelAndView.addObject("iteminstId",iteminstId);
        modelAndView.addObject("isApprover",isApprover);
        modelAndView.addObject("currentUserName",SecurityContext.getCurrentUserName());
        modelAndView.addObject("currentUserId",SecurityContext.getCurrentUserId());
        modelAndView.addObject("taskId",taskId);
        modelAndView.addObject("processInstanceId",processInstanceId);
        return modelAndView;
    }

    @ApiOperation("查询当前是否可进入特殊程序的状态")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "applyinstId", value = "申请实例id", readOnly = true, dataType = "string", paramType = "query", required = true),
            @ApiImplicitParam(name = "iteminstId", value = "申请实例id", readOnly = true, dataType = "string", paramType = "query", required = true)

    })
    @GetMapping("/getCurrentSpecialStatus")
    public ContentResultForm getCurrentSpecialStatus(String applyinstId,String iteminstId){
        try {
            if(StringUtils.isBlank(applyinstId)){
                throw new IllegalArgumentException("applyinstId 不能为空！");
            }
            if(StringUtils.isBlank(iteminstId)){
                throw new IllegalArgumentException("iteminstId 不能为空！");
            }
            Map<String,Object> result = specialRrocedureService.getCurrentSpecialStatus(applyinstId,iteminstId);
            return new ContentResultForm(true,result,"query数据成功");
        } catch (Exception e) {
            e.printStackTrace();
            return new ContentResultForm(false,null,"查询失败");
        }

    }

    @ApiOperation("获取特殊程序所需基本信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "applyinstId", value = "申请实例id", readOnly = true, dataType = "string", paramType = "query", required = true),
            @ApiImplicitParam(name = "iteminstId", value = "申请实例id", readOnly = true, dataType = "string", paramType = "query", required = true)

    })
    @GetMapping("/getBasicInfo")
    public ContentResultForm getBasicInfo(String applyinstId,String iteminstId){


        try {
            if(StringUtils.isBlank(iteminstId)){
                throw new IllegalArgumentException("iteminstId 不能为空！");
            }
            SpecialParamVo paramVo = specialRrocedureService.getBasicInfo(applyinstId,iteminstId);
            return new ContentResultForm(true,paramVo,"获取基本信息成功");
        } catch (Exception e) {
            e.printStackTrace();
            return new ContentResultForm(false,null,"获取特殊程序所需基本信息失败！");
        }
    }
    @ApiOperation("获取特殊程序类型列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "codeType", value = "特殊程序类型的字典codeType", readOnly = true, dataType = "string",  required = true)

    })
    @GetMapping("/getDicItemByType")
    public ContentResultForm getDicItemByType(String codeType){

        try {
            if(StringUtils.isBlank(codeType)){
                throw new IllegalArgumentException("字典类型不能为空！");
            }
            List<BscDicCodeItem> codeItems = specialRrocedureService.getDicItemByType(codeType);
            return new ContentResultForm(true,codeItems,"获取特殊程序类型信息成功");
        } catch (Exception e) {
            e.printStackTrace();
            return new ContentResultForm(false,null,"获取特殊程序类型信息失败！");
        }
    }
    @ApiOperation("保存特殊程序")
    @PostMapping("/saveSpecial")
    public ContentResultForm saveSpecial(SpecialParamVo special){

        try {
            SpecialParamVo result = specialRrocedureService.saveSpecial(special);
            return new ContentResultForm(true, result);
        } catch (Exception e) {
            e.printStackTrace();
            return new ContentResultForm(false,null,"保存特殊程序失败！"+e.getMessage());
        }
    }
    @ApiOperation("停止特殊程序")
    @PostMapping("/stopSpecial")
    public ContentResultForm stopSpecial(SpecialParamVo special){
        try {
            specialRrocedureService.stopSpecial(special);
            return new ContentResultForm(true);
        } catch (Exception e) {
            e.printStackTrace();
            return new ContentResultForm(false,null,"结束特殊任务失败！"+e.getMessage());
        }
    }

    @ApiOperation("结束特殊程序(详审批情页按钮)")
    @GetMapping("/stopSpecialBtn")
    public ContentResultForm stopSpecialBtn(String applyinstId,String  iteminstId){
        try {
            specialRrocedureService.btnStopSpecial(applyinstId,iteminstId,"1");//1流程触发，0非流程
            return new ContentResultForm(true);
        } catch (Exception e) {
            e.printStackTrace();
            return new ContentResultForm(false,null,"结束特殊任务失败！"+e.getMessage());
        }
    }
    @ApiOperation("跳转特殊程序处理页面")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "applyinstId", value = "申请实例id", readOnly = true, dataType = "string", paramType = "query", required = true),
            @ApiImplicitParam(name = "iteminstId", value = "申请实例id", readOnly = true, dataType = "string", paramType = "query", required = true)

    })
    @GetMapping("/show")
    public ModelAndView showSpecial(String applyinstId ,String iteminstId){
        ModelAndView modelAndView=new ModelAndView("specialProcedures/index");
        modelAndView.addObject("applyinstId",applyinstId);
        modelAndView.addObject("iteminstId",iteminstId);
        modelAndView.addObject("currentUserName",SecurityContext.getCurrentUserName());
        modelAndView.addObject("currentUserId",SecurityContext.getCurrentUserId());
        return modelAndView;
    }
    @GetMapping("/getOrgUserList")
    public ContentResultForm getOrgUserList(String iteminstId){
        try {
            List<OpuOmUser> list = specialRrocedureService.getOrgUserList(iteminstId);
            return new ContentResultForm(true,list,"success");
        } catch (Exception e) {
            e.printStackTrace();
            return new ContentResultForm(false,null,"查询失败！"+e.getMessage());
        }
    }

    @RequestMapping("/getItemSpecialList")
    @ApiOperation("获取特殊程序历史记录[iteminstId有值则获取当前事项的特殊程序，applyinstId有值则获取申请实例的所有事项的特殊程序]")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "applyinstId", value = "申请实例id", readOnly = true, dataType = "string", paramType = "query", required = true),
            @ApiImplicitParam(name = "iteminstId", value = "事项实例id", readOnly = true, dataType = "string", paramType = "query", required = true),
    })
    public ContentResultForm getItemSpecialList(String applyinstId,String iteminstId){
        try {
            List<Map<String,Object>> list = specialRrocedureService.getSpecicalList(applyinstId,iteminstId);
            return new ContentResultForm(true,list,"query special list success!");
        } catch (Exception e) {
            e.printStackTrace();
            return new ContentResultForm(false,null,"query faild"+e.getMessage());
        }
    }

    @RequestMapping("/getSpecialEndDate")
    @ApiOperation("获取特殊程序历史记录[iteminstId有值则获取当前事项的特殊程序，applyinstId有值则获取申请实例的所有事项的特殊程序]")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "dueNum", value = "特殊程序申请时限", readOnly = true, dataType = "int", paramType = "query", required = true),
            @ApiImplicitParam(name = "startDate", value = "特殊程序开始日期", readOnly = true, dataType = "string", paramType = "query", required = true),
    })
    public ResultForm getSpecialEndDate(int dueNum, String startDate) {
        if (dueNum <= 0) {
            return new ResultForm(false, "dueNum 必须大于0！");
        }
        if (StringUtils.isBlank(startDate)) {
            return new ResultForm(false, "startDate 不能为空！");
        }
        try {
            String specialEndDate = specialRrocedureService.getSpecialEndDate(dueNum, startDate);
            return new ResultForm(true, specialEndDate);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResultForm(false, "获取特殊程序结束日期失败！" + e.getMessage());
        }
    }
}
