package com.augurit.agcloud.bpm.front.controller;

import com.alibaba.fastjson.JSONObject;
import com.augurit.agcloud.bpm.common.domain.ActUserOpinion;
import com.augurit.agcloud.bpm.common.domain.BpmDestTaskConfig;
import com.augurit.agcloud.bpm.common.domain.BpmHistoryCommentForm;
import com.augurit.agcloud.bpm.common.domain.BpmTaskSendObject;
import com.augurit.agcloud.bpm.common.engine.BpmProcessService;
import com.augurit.agcloud.bpm.common.engine.BpmTaskService;
import com.augurit.agcloud.bpm.common.engine.form.BpmTaskInstance;
import com.augurit.agcloud.bpm.common.service.ActUserOpinionService;
import com.augurit.agcloud.bpm.front.domain.BpmAssigneeOrgEntity;
import com.augurit.agcloud.bpm.front.domain.ZTreeParams;
import com.augurit.agcloud.bpm.front.service.BpmTaskFrontService;
import com.augurit.agcloud.framework.security.SecurityContext;
import com.augurit.agcloud.framework.ui.result.ContentResultForm;
import com.augurit.agcloud.framework.ui.result.ResultForm;
import com.augurit.aplanmis.common.event.AplanmisEventPublisher;
import com.augurit.aplanmis.common.event.def.BpmNodeSendAplanmisEvent;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.flowable.engine.TaskService;
import org.flowable.task.api.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;

/**
 * bpm前端流程任务操作类
 * Created by sam on 2018/1/15.
 */
@Api(value = "bpm接口", tags = "审批详情页相关接口")
@RestController
@RequestMapping("/rest/front/task")
public class RestBpmTaskFrontController {
    @Autowired
    private BpmTaskService bpmTaskService;
    @Autowired
    private TaskService taskService;
    @Autowired
    private BpmTaskFrontService bpmTaskFrontService;
    @Autowired
    private ActUserOpinionService actUserOpinionService;
    @Autowired
    private AplanmisEventPublisher publisher;
    @Autowired
    private BpmProcessService bpmProcessService;

    /**
     * 流程任务发送，可以发送到指定节点（可以多个），不传则流程默认发送到下一个节点
     *
     * @param sendObjectStr
     * @return
     */
    @ApiOperation(value = "流程发送，可以发送到指定节点（可以多个），不传则流程默认发送到下一个节点", notes = "流程发送，可以发送到指定节点（可以多个），不传则流程默认发送到下一个节点。")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "sendObjectStr", value = "发送参数实体，taskId必须传。\n" +
                    "示例：{taskId: taskId, //当前节点任务实例id\n" +
                    "      sendConfigs: [{  //发送的目标节点集合\n" +
                    "         isUserTask: true,         //是否用户节点 true是false否\n" +
                    "         isEnableMultiTask: false, //节点是否启用多工作项 true是false否\n" +
                    "         assignees: '161219',      //下一节点审批人（这里是登录用户的登录名），多个用逗号隔开\n" +
                    "         destActId: 'shenpi'       //下一节点定义id（节点编号）\n" +
                    "      }]}", required = true, dataType = "String")
    })
    @RequestMapping(value = "/wfSend", method = RequestMethod.POST)
    public ResultForm wfSend(String sendObjectStr) {
        try {
            if (StringUtils.isBlank(sendObjectStr))
                return new ResultForm(false, "参数不能为空！");
            BpmTaskSendObject sendObject = JSONObject.parseObject(sendObjectStr, BpmTaskSendObject.class);
            if (sendObject == null || StringUtils.isBlank(sendObject.getTaskId()))
                return new ResultForm(false, "参数不能为空！");

            //触发流程发送事件
            Task currTask = bpmTaskService.getRuTaskByTaskId(sendObject.getTaskId());
            publisher.publishEvent(new BpmNodeSendAplanmisEvent(this, sendObject));

            //调用接口完成当前任务节点，如果下个任务节点的审批人还是当前登录用户的话返回下一节点taskId
            String nextTaskId = bpmTaskService.completeTask(sendObject);
            //return new ResultForm(true,nextTaskId);
            return new ContentResultForm<>(true, nextTaskId);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResultForm(false, e.getMessage());
        }
    }


    /**
     * 流程任务发送，直接发送，无需传下一节点信息，由流程定义自动流转
     *
     * @param taskId
     * @return
     */
    @ApiOperation(value = "流程任务发送，直接发送，无需传下一节点信息，由流程定义自动流转", notes = "流程任务发送，直接发送，无需传下一节点信息，由流程定义自动流转。")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "taskId", value = "任务实例id", required = true, dataType = "String")
    })
    @RequestMapping(value = "/completeTask/{taskId}", method = RequestMethod.POST)
    public ResultForm completeTask(@PathVariable(value = "taskId") String taskId) {
        if (StringUtils.isBlank(taskId) || "undefined".equals(taskId))
            return new ResultForm(false, "参数任务实例id不能为空!");
        try {
            taskService.complete(taskId);
            return new ResultForm(true, "任务发送操作成功！");
        } catch (Exception e) {
            e.printStackTrace();
            return new ResultForm(false, "任务发送操作失败！" + e.getMessage());
        }
    }

    /**
     * 指派任务，当前任务节点审批人将审批权交给指定人，由他完成当前节点审批。
     *
     * @param taskId
     * @param loginName
     * @return
     * @throws Exception
     */
    @ApiOperation(value = "指派任务，当前任务节点审批人将审批权交给指定人，由他完成当前节点审批", notes = "指派任务，当前任务节点审批人将审批权交给指定人，由他完成当前节点审批。")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "taskId", value = "任务实例id", required = true, dataType = "String"),
            @ApiImplicitParam(name = "loginName", value = "用户登录名", required = true, dataType = "String")
    })
    @RequestMapping(value = "/assignTask", method = RequestMethod.POST)
    public ResultForm assignTask(String taskId, String loginName) throws Exception {
        if (StringUtils.isBlank(taskId))
            return new ResultForm(false, "参数任务实例id不能为空!");
        if (StringUtils.isBlank(loginName))
            return new ResultForm(false, "参数用户登录名不能为空!");
        try {
            bpmTaskService.assignTask(taskId, loginName);
            return new ResultForm(true, "指派任务操作成功！");
        } catch (Exception e) {
            e.printStackTrace();
            return new ResultForm(false, "指派任务操作失败！");
        }
    }

    /**
     * 认领任务
     *
     * @param taskId
     * @return
     * @throws Exception
     */
    @ApiOperation(value = "认领任务，当前登录人可到待领取任务视图认领流程任务，认领后可在待办视图中办理该任务", notes = "认领任务，当前登录人可到待领取任务视图认领流程任务，认领后可在待办视图中办理该任务。")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "taskId", value = "任务实例id", required = true, dataType = "String")
    })
    @RequestMapping(value = "/claimTask/{taskId}", method = RequestMethod.GET)
    public ResultForm claimTask(@PathVariable(value = "taskId") String taskId) throws Exception {
        if (StringUtils.isBlank(taskId) || "undefined".equals(taskId))
            return new ResultForm(false, "参数任务实例id不能为空!");
        try {
            bpmTaskService.claimTask(taskId);
            return new ResultForm(true, "认领任务操作成功！");
        } catch (Exception e) {
            e.printStackTrace();
            return new ResultForm(false, "认领任务操作失败！");
        }
    }

    /**
     * 被委派人完成任务，任务返回给任务所有者
     *
     * @param taskId
     * @return
     * @throws Exception
     */
    @ApiOperation(value = "被委派人完成任务，任务返回给任务所有者", notes = "被委派人完成任务，任务返回给任务所有者。")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "taskId", value = "任务实例id", required = true, dataType = "String")
    })
    @RequestMapping(value = "/resolveTask/{taskId}", method = RequestMethod.POST)
    public ResultForm resolveTask(@PathVariable(value = "taskId") String taskId) throws Exception {
        if (StringUtils.isBlank(taskId) || "undefined".equals(taskId))
            return new ResultForm(false, "参数任务实例id不能为空!");
        try {
            bpmTaskService.resolveTask(taskId);
            return new ResultForm(true, "完成委派任务操作成功！");
        } catch (Exception e) {
            e.printStackTrace();
            return new ResultForm(false, "完成委派任务操作失败！");
        }
    }

    /**
     * 添加任务节点审批意见
     *
     * @param taskId
     * @param processInstanceId
     * @param message
     * @return
     * @throws Exception
     */
    @ApiOperation(value = "添加任务节点审批意见", notes = "添加任务节点审批意见。")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "taskId", value = "任务实例id", required = true, dataType = "String"),
            @ApiImplicitParam(name = "processInstanceId", value = "流程实例id", required = true, dataType = "String"),
            @ApiImplicitParam(name = "message", value = "审批意见", required = true, dataType = "String")
    })
    @RequestMapping(value = "/addTaskComment", method = RequestMethod.POST)
    public ResultForm addTaskComment(String taskId, String processInstanceId, String message) throws Exception {
        if (StringUtils.isBlank(taskId))
            return new ResultForm(false, "参数任务实例id不能为空!");
        if (StringUtils.isBlank(processInstanceId))
            return new ResultForm(false, "参数流程实例id不能为空!");
        if (StringUtils.isBlank(message))
            return new ResultForm(false, "参数审批意见不能为空!");
        try {
            bpmTaskService.addTaskComment(taskId, processInstanceId, message);
            return new ResultForm(true, "添加任务节点审批意见操作成功！");
        } catch (Exception e) {
            e.printStackTrace();
            return new ResultForm(false, "添加任务节点审批意见操作失败！");
        }
    }

    /**
     * 更新任务节点审批意见
     *
     * @param commentId
     * @param message
     * @return
     * @throws Exception
     */
    @ApiOperation(value = "更新任务节点审批意见", notes = "更新任务节点审批意见。")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "commentId", value = "意见实例id", required = true, dataType = "String"),
            @ApiImplicitParam(name = "message", value = "审批意见", required = true, dataType = "String")
    })
    @RequestMapping(value = "/updateTaskComment", method = RequestMethod.POST)
    public ResultForm updateTaskComment(String commentId, String message) throws Exception {
        if (StringUtils.isBlank(commentId))
            return new ResultForm(false, "参数意见实例id不能为空!");
        try {
            bpmTaskService.updateComment(commentId, message);
            return new ResultForm(true, "更新任务节点审批意见操作成功！");
        } catch (Exception e) {
            e.printStackTrace();
            return new ResultForm(false, "更新任务节点审批意见操作失败！");
        }
    }

    /**
     * 获取任务节点审批意见
     *
     * @param processInstanceId
     * @param taskId
     * @return
     * @throws Exception
     */
    @ApiOperation(value = "获取任务节点审批意见", notes = "获取任务节点审批意见。")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "processInstanceId", value = "流程实例id", required = true, dataType = "String"),
            @ApiImplicitParam(name = "taskId", value = "任务实例id", required = true, dataType = "String")
    })
    @RequestMapping(value = "/getTaskComment", method = RequestMethod.GET)
    public ResultForm getTaskComment(String processInstanceId, String taskId) throws Exception {
        if (StringUtils.isBlank(taskId))
            return new ResultForm(false, "参数任务实例id不能为空!");
        if (StringUtils.isBlank(processInstanceId))
            return new ResultForm(false, "参数流程实例id不能为空!");
        try {
            List<BpmHistoryCommentForm> historyCommentsByTaskId = bpmTaskService.getHistoryCommentsByTaskId(processInstanceId, taskId);
            if (historyCommentsByTaskId != null && historyCommentsByTaskId.size() > 0) {
                return new ContentResultForm<>(true, historyCommentsByTaskId.get(0));
            }
            return new ResultForm(false, "获取不到任务节点审批意见！");
        } catch (Exception e) {
            e.printStackTrace();
            return new ResultForm(false, "获取任务节点审批意见操作失败！");
        }
    }

    /**
     * 设置任务所有者
     *
     * @param taskId    任务ID
     * @param loginName 用户ID
     * @return
     * @throws Exception
     */
    @ApiOperation(value = "设置任务所有者", notes = "设置任务所有者。")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "taskId", value = "任务实例id", required = true, dataType = "String"),
            @ApiImplicitParam(name = "loginName", value = "用户登录名", required = true, dataType = "String")
    })
    @RequestMapping(value = "/setTaskOwner", method = RequestMethod.POST)
    public ResultForm setTaskOwner(String taskId, String loginName) throws Exception {
        if (StringUtils.isBlank(taskId))
            return new ResultForm(false, "参数任务实例id不能为空!");
        if (StringUtils.isBlank(loginName))
            return new ResultForm(false, "参数用户登录名不能为空!");
        try {
            bpmTaskService.setTaskOwner(taskId, loginName);
            return new ResultForm(true, "设置任务所有者操作成功！");
        } catch (Exception e) {
            e.printStackTrace();
            return new ResultForm(false, "设置任务所有者操作失败！");
        }
    }

    /**
     * 签收任务，任务被审批人签收前，上一个节点审批人还可以撤回，签收后则不能撤回。
     *
     * @param taskId 任务ID
     * @return
     * @throws Exception
     */
    @ApiOperation(value = "签收任务，任务被审批人签收前，上一个节点审批人还可以撤回，签收后则不能撤回", notes = "签收任务，任务被审批人签收前，上一个节点审批人还可以撤回，签收后则不能撤回。")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "taskId", value = "任务实例id", required = true, dataType = "String")
    })
    @RequestMapping(value = "/signTask/{taskId}", method = RequestMethod.GET)
    public ResultForm signTask(@PathVariable(value = "taskId") String taskId) throws Exception {
        if (StringUtils.isBlank(taskId) || "undefined".equals(taskId))
            return new ResultForm(false, "参数任务实例id不能为空！");
        try {
            bpmTaskService.signTask(taskId);
            return new ResultForm(true, "签收任务成功！");
        } catch (Exception e) {
            return new ResultForm(false, "签收任务失败！" + e.getMessage());
        }
    }

    /**
     * 设置任务第一次办理的时间
     *
     * @param taskId
     * @return
     * @throws Exception
     */
    @ApiOperation(value = "设置任务第一次办理的时间", notes = "设置任务第一次办理的时间。")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "taskId", value = "任务实例id", required = true, dataType = "String")
    })
    @RequestMapping(value = "/setTaskFirstOpenTime/{taskId}", method = RequestMethod.GET)
    public ResultForm setTaskFirstOpenTime(@PathVariable(value = "taskId") String taskId) throws Exception {
        if (StringUtils.isBlank(taskId) || "undefined".equals(taskId))
            return new ResultForm(false, "参数任务实例id不能为空！");
        try {
            bpmTaskService.setFirstOpenTime(taskId, null);
            return new ResultForm(true, "设置任务第一次办理的时间操作成功！");
        } catch (Exception e) {
            e.printStackTrace();
            return new ResultForm(false, "设置任务第一次办理的时间操作失败！");
        }
    }

    /**
     * 获取流程发送的下一环节信息，包括一个或多个节点的配置信息
     *
     * @param taskId
     * @return
     * @throws Exception
     */
    @ApiOperation(value = "加载流程发送的下一环节信息，包括一个或多个节点的配置信息", notes = "加载流程发送的下一环节信息，包括一个或多个节点的配置信息。")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "taskId", value = "任务实例id", required = true, dataType = "String")
    })
    @RequestMapping(value = "/getTaskSendConfig/{taskId}", method = RequestMethod.GET)
    public ResultForm getTaskSendConfig(@PathVariable(value = "taskId") String taskId) throws Exception {
        if (StringUtils.isBlank(taskId) || "undefined".equals(taskId))
            return new ResultForm(false, "参数任务实例id不能为空！");
        try {
            List<BpmDestTaskConfig> list = bpmTaskService.getBpmDestTaskConfigByCurrTaskId(taskId);
            return new ContentResultForm<>(true, list);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResultForm(false, "获取流程发送的下一环节信息操作失败！");
        }
    }

    /**
     * 获取流程发送的下一节点审批人范围
     *
     * @param taskId
     * @return
     * @throws Exception
     */
    @ApiOperation(value = "加载流程发送的下一环节信息，包括一个或多个节点的配置信息", notes = "加载流程发送的下一环节信息，包括一个或多个节点的配置信息。")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "taskId", value = "任务实例id", required = true, dataType = "String"),
            @ApiImplicitParam(name = "destActId", value = "下一个节点定义id", required = true, dataType = "String")
    })
    @RequestMapping(value = "/getNextTaskAssigneeRange", method = RequestMethod.GET)
    public ResultForm getAssigneeRangeByCurrTaskId(String taskId, String destActId) throws Exception {
        if (StringUtils.isBlank(taskId))
            return new ResultForm(false, "参数任务实例id不能为空!");
        if (StringUtils.isBlank(destActId))
            return new ResultForm(false, "参数下一个节点定义id不能为空!");
        try {
            Map<String, String> assigneeRange = bpmTaskService.getAssigneeRangeByCurrTaskId(taskId, destActId);
            return new ContentResultForm<Map>(true, assigneeRange);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResultForm(false, "获取流程发送的下一节点审批人范围操作失败！");
        }
    }

    /**
     * 获取参与人范围，机构、角色、岗位、用户的集合树
     *
     * @param assigneeRangeKey
     * @param parentId
     * @return
     * @throws Exception
     */
    @ApiOperation(value = "获取参与人范围，机构、角色、岗位、用户的集合树", notes = "获取参与人范围，机构、角色、岗位、用户的集合树。")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "assigneeRangeKey", value = "审批人范围KEY，格式：$USER.登录名,$ORG.组织机构id,$ROLE.角色id,$POS.岗位id", required = true, dataType = "String"),
            @ApiImplicitParam(name = "parentId", value = "当前树节点id，格式：USER_登录名，ORG_组织机构id", required = true, dataType = "String")
    })
    @RequestMapping(value = "/getAssigneeRangeTree", method = RequestMethod.GET)
    public List getAssigneeRangeTree(String assigneeRangeKey, String parentId) throws Exception {
        List<Map> result = new ArrayList<Map>();
        if (StringUtils.isNotBlank(assigneeRangeKey)) {
            result = bpmTaskFrontService.createResultTree(bpmTaskService.getAssigneeRangeTreeByRangeKey(assigneeRangeKey), true);//需要用户列表根节点，传true
        } else if (StringUtils.isNotBlank(parentId)) {
            String[] keys = parentId.split("_");
            if (keys.length == 3)
                result = bpmTaskFrontService.createResultTree(bpmTaskService.getUserAndSubNodeById(keys[1], keys[2]), false);
        }
        return result;
    }

    /**
     * 目前使用这个树接口,ztree
     *
     * @param params
     * @return
     * @throws Exception
     */
    @ApiOperation(value = "获取参与人范围，机构、角色、岗位、用户的集合树，zTree", notes = "获取参与人范围，机构、角色、岗位、用户的集合树，zTree。")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "params", value = "参数实体", required = true, dataType = "ZTreeParams")
    })
    @RequestMapping(value = "/getAssigneeRangeZTree", method = RequestMethod.GET)
    public List getAssigneeRangeZTree(ZTreeParams params) throws Exception {
        List<BpmAssigneeOrgEntity> assigneeOrgTreeData = bpmTaskFrontService.getAssigneeOrgTreeData(params);
        return assigneeOrgTreeData;
    }

    /**
     * 获取流程审批页元素（流程表单字段、工具栏、办理按钮、附件面板等）的配置权限
     * 升级版
     *
     * @param version
     * @param viewId
     * @param actId
     * @param privMode
     * @param appFlowdefId
     * @return
     * @throws Exception
     */
    @ApiOperation(value = "审批详情页 -->获取流程审批页元素（流程表单字段、工具栏、办理按钮、附件面板等）的配置权限")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "version", value = "流程定义版本号，数值大于0", required = true, dataType = "String", paramType = "query", readOnly = true),
            @ApiImplicitParam(name = "privMode", value = "权限模式，act表示流程节点控制，等于act时，actId参数必须传；view表示视图控制，view-act表示视图及流程节点联合控制", required = true, dataType = "String", paramType = "query", readOnly = true),
            @ApiImplicitParam(name = "viewId", value = "业务视图id", required = true, dataType = "String", paramType = "query", readOnly = true),
            @ApiImplicitParam(name = "actId", value = "流程节点定义id，可选，如果privMode=act,则必选", required = true, dataType = "String", paramType = "query", readOnly = true),
            @ApiImplicitParam(name = "appFlowdefId", value = "业务流程模板流程定义关联id,可选，如果有就加载当前流程关联信息，否则查找当前模板的主流程", required = false, dataType = "String", paramType = "query", readOnly = true)
    })
    @RequestMapping(value = "/getAuthorizeElementPlus", method = RequestMethod.GET)
    public ResultForm getAuthorizeElementPlus(int version, String viewId, String actId, String privMode, String appFlowdefId) throws Exception {
        if (StringUtils.isBlank(actId))
            return new ResultForm(false, "参数流程节点定义id不能为空！");
        if (StringUtils.isBlank(privMode))
            return new ResultForm(false, "参数权限模式不能为空！");
        if (StringUtils.isBlank(viewId))
            return new ResultForm(false, "参数业务视图id不能为空！");
        if (version <= 0)
            return new ResultForm(false, "参数流程定义版本号，数值必须大于0！");
        try {
            Map elementMap = bpmTaskFrontService.getAuthorizeElementPlus(version, privMode, viewId, actId, appFlowdefId);
            return new ContentResultForm<>(true, elementMap);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResultForm(false, "获取流程审批界面页面元素权限信息操作失败！");
        }
    }

    /**
     * 获取流程审批界面页面元素权限信息
     *
     * @param version
     * @param viewId
     * @param actId
     * @param privMode
     * @param appFlowdefId
     * @return
     * @throws Exception
     */
    @ApiOperation(value = "获取流程审批界面页面元素权限信息", notes = "获取流程审批界面页面元素权限信息。")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "version", value = "流程定义版本号，数值大于0", required = true, dataType = "String"),
            @ApiImplicitParam(name = "privMode", value = "权限模式，act表示流程节点控制，等于act时，actId参数必须传；view表示视图控制，view-act表示视图及流程节点联合控制", required = true, dataType = "String"),
            @ApiImplicitParam(name = "viewId", value = "业务视图id", required = true, dataType = "String"),
            @ApiImplicitParam(name = "actId", value = "流程节点定义id，可选，如果privMode=act,则必选", required = true, dataType = "String"),
            @ApiImplicitParam(name = "appFlowdefId", value = "业务流程模板流程定义关联id,可选，如果有就加载当前流程关联信息，否则查找当前模板的主流程", required = false, dataType = "String")
    })
    @RequestMapping(value = "/getAuthorizeElement", method = RequestMethod.GET)
    public ResultForm getAuthorizeElement(int version, String viewId, String actId, String privMode, String appFlowdefId) throws Exception {
        if (StringUtils.isBlank(actId))
            return new ResultForm(false, "参数流程节点定义id不能为空！");
        if (StringUtils.isBlank(privMode))
            return new ResultForm(false, "参数权限模式不能为空！");
        if (StringUtils.isBlank(viewId))
            return new ResultForm(false, "参数业务视图id不能为空！");
        if (version <= 0)
            return new ResultForm(false, "参数流程定义版本号，数值必须大于0！");
        try {
            Map elementMap = bpmTaskFrontService.getAuthorizeElement(version, privMode, viewId, actId, appFlowdefId);
            return new ContentResultForm<>(true, elementMap);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResultForm(false, "获取流程审批界面页面元素权限信息操作失败！");
        }
    }

    /**
     * 获取流程审批界面公共页面元素（流程办理按钮）权限信息
     *
     * @param version
     * @param viewId
     * @param actId
     * @param privMode
     * @param appFlowdefId
     * @return
     * @throws Exception
     */
    @ApiOperation(value = "获取流程审批界面公共页面元素（流程办理按钮）权限信息", notes = "获取流程审批界面公共页面元素（流程办理按钮）权限信息。")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "version", value = "流程定义版本号，数值大于0", required = true, dataType = "String"),
            @ApiImplicitParam(name = "privMode", value = "权限模式，act表示流程节点控制，等于act时，actId参数必须传；view表示视图控制，view-act表示视图及流程节点联合控制", required = true, dataType = "String"),
            @ApiImplicitParam(name = "viewId", value = "业务视图id", required = true, dataType = "String"),
            @ApiImplicitParam(name = "actId", value = "流程节点定义id，可选，如果privMode=act,则必选", required = true, dataType = "String"),
            @ApiImplicitParam(name = "appFlowdefId", value = "业务流程模板流程定义关联id,可选，如果有就加载当前流程关联信息，否则查找当前模板的主流程", required = false, dataType = "String")
    })
    @RequestMapping(value = "/getAuthorizePublicElement", method = RequestMethod.GET)
    public ResultForm getAuthorizePublicElement(int version, String viewId, String actId, String privMode, String appFlowdefId) throws Exception {
        if (StringUtils.isBlank(actId))
            return new ResultForm(false, "参数流程节点定义id不能为空！");
        if (StringUtils.isBlank(privMode))
            return new ResultForm(false, "参数权限模式不能为空！");
        if (version <= 0)
            return new ResultForm(false, "参数流程定义版本号，数值必须大于0！");
        try {
            Map elementMap = bpmTaskFrontService.getAuthorizePublicElement(version, privMode, viewId, actId, appFlowdefId);
            return new ContentResultForm<>(true, elementMap);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResultForm(false, "获取流程审批界面页面元素权限信息操作失败！");
        }
    }

    @ApiOperation(value = "回退前获取当前节点的上一环节的集合",notes = "回退前获取当前节点的上一环节的集合。")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "taskId", value = "任务ID", required = true, dataType = "String")
    })
    @RequestMapping(value="/getReturnPrevTasksByTaskId",method = RequestMethod.GET)
    public ResultForm getReturnPrevTasksByTaskId(String taskId) throws Exception{
        if (StringUtils.isBlank(taskId))
            return new ResultForm(false, "任务id参数不能为空!");

        return bpmTaskService.getReturnPrevTasksByTaskId(taskId);
    }

    /**
     * 回退任务到上一环节
     *
     * @param taskId 当前节点taskId
     * @return
     * @throws Exception
     */
    @ApiOperation(value = "回退任务到上一环节",notes = "回退任务到上一环节。")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "taskId", value = "任务实例id", required = true, dataType = "String"),
            @ApiImplicitParam(name = "returnToActId", value = "回退到的任务节点定义id", required = true, dataType = "String"),
            @ApiImplicitParam(name = "comment", value = "回退意见", required = true, dataType = "String")
    })
    @RequestMapping(value="/returnPrevTask/{taskId}",method = RequestMethod.GET)
    public ResultForm returnPrevTask(@PathVariable(value = "taskId") String taskId,String returnToActId,String comment) throws Exception {
        if (StringUtils.isBlank(taskId)|| "undefined".equals(taskId))
            return new ResultForm(false, "参数任务实例id不能为空！");
        if (StringUtils.isBlank(returnToActId))
            return new ResultForm(false, "参数回退到的任务节点定义id不能为空！");
        try {
            return bpmTaskService.returnToTask(taskId,returnToActId,comment);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResultForm(false,e.getMessage());
        }
    }

    /**
     * 转交任务，将当前任务的审批权，转交给其他人
     *
     * @param taskId    任务ID
     * @param loginName 转交到用户ID
     * @return
     */
    @ApiOperation(value = "转交任务，将当前任务的审批权，转交给其他人", notes = "转交任务，将当前任务的审批权，转交给其他人。")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "taskId", value = "任务实例id", required = true, dataType = "String"),
            @ApiImplicitParam(name = "loginName", value = "用户登录名", required = true, dataType = "String")
    })
    @RequestMapping(value = "/sendOnTask", method = RequestMethod.POST)
    public ResultForm sendOnTask(String taskId, String loginName, String comment) {
        if (StringUtils.isBlank(taskId))
            return new ResultForm(false, "参数任务实例id不能为空!");
        if (StringUtils.isBlank(loginName))
            return new ResultForm(false, "参数用户登录名不能为空!");
        if (StringUtils.isBlank(comment))
            return new ResultForm(false, "参数转办意见不能为空!");
        try {
            bpmTaskService.sendOnTask(taskId, loginName, comment);
            return new ResultForm(true, "转交任务操作成功！");
        } catch (Exception e) {
            e.printStackTrace();
            return new ResultForm(false, e.getMessage());
        }
    }

    /**
     * 获取当前环节的审批人范围
     *
     * @param taskId
     * @return
     * @throws Exception
     */
    @ApiOperation(value = "获取当前环节的审批人范围", notes = "获取当前环节的审批人范围。")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "taskId", value = "任务实例id", required = true, dataType = "String")
    })
    @RequestMapping(value = "/getCurrTaskAssigneeOrRange/{taskId}", method = RequestMethod.GET)
    public ResultForm getCurrTaskAssigneeOrRange(@PathVariable(value = "taskId") String taskId) throws Exception {
        if (StringUtils.isBlank(taskId) || "undefined".equals(taskId))
            return new ResultForm(false, "参数任务实例id不能为空！");
        try {
            Map<String, String> assigneeRange = bpmTaskService.getCurrTaskAssigneeOrRange(taskId);
            return new ContentResultForm<Map>(true, assigneeRange);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResultForm(false, "获取当前环节的审批人范围操作失败！");
        }
    }

    /**
     * 获取当前审批人的常用审批意见列表
     *
     * @return
     * @throws Exception
     */
    @ApiOperation(value = "获取当前审批人的常用审批意见列表", notes = "获取当前审批人的常用审批意见列表。")
    @RequestMapping(value = "/getAllActiveUserOpinions", method = RequestMethod.GET)
    public ResultForm getAllActiveUserOpinions() throws Exception {
        try {
            ActUserOpinion userOpinion = new ActUserOpinion();
            userOpinion.setIsActive("1");
            userOpinion.setUserId(SecurityContext.getCurrentUser().getUserId());
            return new ContentResultForm<>(true, actUserOpinionService.listActUserOpinion(userOpinion));
        } catch (Exception e) {
            e.printStackTrace();
            return new ResultForm(false, "获取当前审批人的常用审批意见列表操作失败！");
        }
    }

    /**
     * 获取单个审批人常用审批意见
     *
     * @param id
     * @return
     * @throws Exception
     */
    @ApiOperation(value = "获取单个审批人常用审批意见", notes = "获取单个审批人常用审批意见。")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "意见id", required = true, dataType = "String")
    })
    @RequestMapping(value = "/getUserOpinion/{id}", method = RequestMethod.GET)
    public ResultForm getUserOpinionsById(@PathVariable(value = "id") String id) throws Exception {
        if (StringUtils.isBlank(id) || "undefined".equals(id))
            return new ResultForm(false, "参数意见id不能为空！");
        try {
            ActUserOpinion actUserOpinion = actUserOpinionService.getActUserOpinionById(id);
            if (actUserOpinion == null)
                return new ResultForm(false, "获取不到常用审批意见！");
            return new ContentResultForm<>(true, actUserOpinion);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResultForm(false, "获取常用审批意见操作失败！");
        }
    }

    /**
     * 更新常用审批意见
     *
     * @param userOpinion
     * @return
     * @throws Exception
     */
    @ApiOperation(value = "更新常用审批意见", notes = "更新常用审批意见。")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "userOpinion", value = "意见实体", required = true, dataType = "ActUserOpinion")
    })
    @RequestMapping(value = "/updateUserOpinion", method = RequestMethod.POST)
    public ResultForm updateUserOpinion(ActUserOpinion userOpinion) throws Exception {
        if (userOpinion == null || StringUtils.isBlank(userOpinion.getOpinionId()))
            return new ResultForm(false, "参数意见实体不能为空！");
        try {
            actUserOpinionService.updateActUserOpinion(userOpinion);
            return new ResultForm(true, "更新操作成功！");
        } catch (Exception e) {
            e.printStackTrace();
            return new ResultForm(false, "更新操作失败！");
        }
    }

    /**
     * 删除审批人常用审批意见
     *
     * @param id
     * @return
     * @throws Exception
     */
    @ApiOperation(value = "删除审批人常用审批意见", notes = "删除审批人常用审批意见。")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "意见id", required = true, dataType = "String")
    })
    @RequestMapping(value = "/deleteUserOpinion/{id}", method = RequestMethod.DELETE)
    public ResultForm deleteUserOpinionById(@PathVariable(value = "id") String id) throws Exception {
        if (StringUtils.isBlank(id) || "undefined".equals(id))
            return new ResultForm(false, "参数意见id不能为空！");
        try {
            actUserOpinionService.deleteActUserOpinion(id);
            return new ResultForm(true, "删除操作成功！");
        } catch (Exception e) {
            e.printStackTrace();
            return new ResultForm(false, "删除操作失败！");
        }
    }

    /**
     * 保存常用审批意见
     *
     * @param message
     * @return
     * @throws Exception
     */
    @ApiOperation(value = "保存常用审批意见", notes = "更新常用审批意见。")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "message", value = "意见", required = true, dataType = "String")
    })
    @RequestMapping(value = "/saveUserOpinion", method = RequestMethod.POST)
    public ResultForm saveUserOpinion(String message) throws Exception {
        if (StringUtils.isBlank(message))
            return new ResultForm(false, "参数意见不能为空！");
        ActUserOpinion userOpinion = new ActUserOpinion();
        userOpinion.setOpinionId(UUID.randomUUID().toString());
        userOpinion.setOpinionContent(message);
        userOpinion.setUserId(SecurityContext.getCurrentUser().getUserId());
        userOpinion.setIsActive("1");
        userOpinion.setSortNo(0);
        userOpinion.setCreater(SecurityContext.getCurrentUserName());
        userOpinion.setCreateTime(new Date());
        try {
            actUserOpinionService.insertActUserOpinion(userOpinion);
            return new ResultForm(true, "保存操作成功！");
        } catch (Exception e) {
            e.printStackTrace();
            return new ResultForm(false, "保存操作失败！");
        }
    }

    @ApiOperation(value = "任务撤回", notes = "撤回未签收的任务。")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "taskId", value = "任务ID", required = true, dataType = "String"),
    })
    @RequestMapping(value = "/withdrawTask/{taskId}", method = RequestMethod.GET)
    public ResultForm withdrawTask(@PathVariable(value = "taskId") String taskId) throws Exception {
        if (StringUtils.isBlank(taskId))
            return new ResultForm(false, "参数任务id不能为空!");

        return bpmTaskService.withdrawTask(taskId);
    }

    @ApiOperation(value = "获取可撤回任务列表", notes = "根据当前已办任务ID，获取可撤回任务列表（包含已签收的任务）。")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "taskId", value = "任务ID", required = true, dataType = "String")
    })
    @RequestMapping(value = "/withdrawTask/list/{taskId}", method = RequestMethod.GET)
    public ResultForm withdrawTaskList(@PathVariable(value = "taskId") String taskId) throws Exception {
        if (StringUtils.isBlank(taskId))
            throw new RuntimeException("参数任务id不能为空!");
        List<BpmTaskInstance> list = bpmTaskService.getwithdrawTaskList(taskId);
        return new ContentResultForm(true, list);
    }

    @ApiOperation(value = "高级任务撤回", notes = "高级撤回；撤回多工作项任务（允许撤回【taskId】的未签收待办任务）,需要配合@method:getwithdrawTaskList使用（获取到可撤回任务列表）。")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "taskId", value = "任务ID", required = true, dataType = "String"),
            @ApiImplicitParam(name = "preTaskDefKey", value = "请求撤回的任务定义key(即当前撤回操作的任务定义key)", required = true, dataType = "String")
    })
    @RequestMapping(value = "/withdrawOneForMultiTask", method = RequestMethod.POST)
    public ResultForm withdrawOneForMultiTask(String taskId, String preTaskDefKey) throws Exception {
        if (StringUtils.isBlank(taskId))
            return new ResultForm(false, "参数任务id不能为空!");

        return bpmTaskService.withdrawOneForMultiTask(taskId, preTaskDefKey);
    }

    @ApiOperation(value = "批量高级撤回", notes = "批量高级撤回；批量撤回多工作项任务（允许撤回【taskIds数组】的未签收待办任务）,需要配合@method:getwithdrawTaskList使用（获取到可撤回任务列表）。")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "taskId", value = "任务ID", required = true, dataType = "String"),
            @ApiImplicitParam(name = "preTaskDefKey", value = "请求撤回的任务定义key(即当前撤回操作的任务定义key)", required = true, dataType = "String")
    })
    @RequestMapping(value = "/withdrawBatchForMultiTasks", method = RequestMethod.POST)
    public ResultForm withdrawBatchForMultiTasks(String[] taskIds, String preTaskDefKey) throws Exception {
        if (taskIds == null)
            return new ResultForm(false, "任务id数组参数不能为空!");
        if (StringUtils.isBlank(preTaskDefKey))
            return new ResultForm(false, "请求撤回的任务定义key参数不能为空!");

        return bpmTaskService.withdrawBatchForMultiTasks(taskIds, preTaskDefKey);
    }

    @RequestMapping("/activateProcessInst")
    public ResultForm activateProcessInst(String processinstId) {
        try {
            if (StringUtils.isBlank(processinstId)) throw new Exception("流程实例ID为空!");
            if (bpmProcessService.isProcessSuspended(processinstId))//判断流程是否已挂起
                bpmProcessService.activateProcessInstanceById(processinstId);//激活当前流程
            return new ResultForm(true, "流程激活成功！");
        } catch (Exception e) {

            return new ResultForm(false, "流程激活失败！");
        }
    }
}
