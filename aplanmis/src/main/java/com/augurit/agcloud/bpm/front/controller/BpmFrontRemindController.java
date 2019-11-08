package com.augurit.agcloud.bpm.front.controller;

import com.alibaba.fastjson.JSONArray;
import com.augurit.agcloud.bpm.common.domain.ActStoRemindReceiver;
import com.augurit.agcloud.bpm.front.domain.ActStoRemindForm;
import com.augurit.agcloud.bpm.front.domain.BpmTaskAndUser;
import com.augurit.agcloud.bpm.front.service.BpmFrontRemindService;
import com.augurit.agcloud.framework.ui.result.ContentResultForm;
import com.augurit.agcloud.framework.ui.result.ResultForm;
import com.augurit.agcloud.opus.common.domain.OpuOmUser;
import com.augurit.agcloud.opus.common.service.om.OpuOmUserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Api(description = "工作流督办相关接口")
@RequestMapping("/bpm/front/remind")
@RestController
public class BpmFrontRemindController {

    @Autowired
    private BpmFrontRemindService bpmFrontRemindService;
    @Autowired
    private OpuOmUserService opuOmUserService;

    @ApiOperation(value = "根据流程实例ID获取当前审批节点的审批人集合", notes = "根据流程实例ID获取当前审批节点的审批人集合。")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "procInstId", value = "流程实例ID", required = true, dataType = "String"),
    })
    @RequestMapping(value = "/process/runtime/users",method = RequestMethod.GET)
    public ResultForm getProcessAllNodeUsers(String procInstId) throws Exception {
        if(StringUtils.isBlank(procInstId))
            return new ResultForm(false,"参数procInstId不能为空！");

        List<BpmTaskAndUser> list = bpmFrontRemindService.getTaskUsersByProcessInstanceId(procInstId);
        return new ContentResultForm<List<BpmTaskAndUser>>(true,list);
    }

    @ApiOperation(value = "保存督办信息", notes = "保存督办信息。")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "actStoRemindForm", value = "督办信息实体；注意userIdJson属性格式为：[{'remindReceiverType':'c','receiverUserId':'11111'}]；remindReceiverType（接收人类型）：s表示接收人、c表示抄送人", required = true, dataType = "ActStoRemindForm"),
    })
    @RequestMapping(value = "/remind/save",method = RequestMethod.POST)
    public ResultForm saveRemind(@RequestBody ActStoRemindForm actStoRemindForm){
        if(actStoRemindForm==null||actStoRemindForm.getUserIdJson()==null)
            return new ResultForm(false,"保存对象为空！");

        List<ActStoRemindReceiver> receivers = JSONArray.parseArray(actStoRemindForm.getUserIdJson(),ActStoRemindReceiver.class);

        try {
            bpmFrontRemindService.saveProcessRemind(actStoRemindForm,receivers);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResultForm(false,e.getMessage());
        }

        return new ResultForm(true);
    }

    @ApiOperation(value = "获取模板名称", notes = "根据流程实例ID，获取模板名称。")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "procInstId", value = "流程实例ID", required = true, dataType = "String"),
    })
    @RequestMapping(value = "/tpl/app/name",method = RequestMethod.GET)
    public ResultForm getTplAppNameByProcessInstanceId(String procInstId) throws Exception {
        if(StringUtils.isBlank(procInstId))
            return new ResultForm(false,"参数procInstId不能为空！");

        String title = bpmFrontRemindService.getTplAppNameByProcessInstanceId(procInstId);
        return new ResultForm(true,title);
    }

    @ApiOperation(value = "获取用户列表", notes = "根据用户名关键字，获取用户列表。")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "keyword", value = "用户名关键字", required = true, dataType = "String"),
    })
    @RequestMapping(value = "/users/{keyword}",method = RequestMethod.GET)
    public ResultForm getAllUserByUserNameKeywork(@PathVariable String keyword) throws Exception{
        if(StringUtils.isBlank(keyword))
            return new ResultForm(false,"参数keyword不能为空");

        List<OpuOmUser> list = opuOmUserService.getActiveUsersByKeyWord(keyword);
        return new ContentResultForm<>(true,list);
    }

    @ApiOperation(value = "保存督办信息并发送通知", notes = "保存督办信息并发送通知。")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "actStoRemindForm", value = "督办信息实体；注意userIdJson属性格式为：[{'remindReceiverType':'c','receiverUserId':'11111'}]；remindReceiverType（接收人类型）：s表示接收人、c表示抄送人", required = true, dataType = "ActStoRemindForm"),
    })
    @RequestMapping(value = "/msg/save",method = RequestMethod.POST)
    public ResultForm saveRemindAndAoa(@RequestBody List<ActStoRemindForm> actStoRemindForms,String projName,String applyinstCode){
        if(actStoRemindForms==null||actStoRemindForms.size()==0)
            return new ResultForm(false,"保存对象为空！");

        try {
            bpmFrontRemindService.saveProcessRemindAndAoaMsgs(actStoRemindForms,projName,applyinstCode);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResultForm(false,e.getMessage());
        }

        return new ResultForm(true);
    }
}
