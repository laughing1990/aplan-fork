package com.augurit.aplanmis.front.approve.controller;

import com.augurit.agcloud.bpm.front.service.BpmProcessFrontService;
import com.augurit.agcloud.bpm.front.service.BpmTaskFrontService;
import com.augurit.agcloud.bpm.front.vo.ExtendBpmHistoryCommentForm;
import com.augurit.agcloud.framework.ui.result.ContentResultForm;
import com.augurit.agcloud.framework.ui.result.ResultForm;
import com.augurit.aplanmis.front.approve.service.RestBpmService;
import com.augurit.aplanmis.front.approve.vo.ActFlowParamVo;
import com.augurit.aplanmis.front.approve.vo.BpmHistoryCommentFormVo;
import com.augurit.aplanmis.front.approve.vo.HistoryCommentsVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/rest/bpm/approve")
@Api(value = "bpm接口", tags = "审批详情页")
@Slf4j
public class RestBpmController {

    @Autowired
    private BpmTaskFrontService bpmTaskFrontService;
    @Autowired
    private RestBpmService restBpmService;
    @Autowired
    private BpmProcessFrontService bpmProcessFrontService;
    SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");

    @GetMapping("/act/param/")
    @ApiOperation(value = "审批详情页 --> 流程类型判断")
    @ApiImplicitParam(name = "appFlowdefId", value = "所属流程设置ID", required = true, dataType = "string", paramType = "query", readOnly = true)
    public ContentResultForm<ActFlowParamVo> getActFlowParam(@RequestParam String appFlowdefId) {
        log.info("获取流程参数, appFlowdefId: {}", appFlowdefId);
        try {
            Map<String, Object> actFlowParam = restBpmService.getActFlowParam(appFlowdefId);
            ActFlowParamVo actFlowParamVo = ActFlowParamVo.toActFlowParamVo(actFlowParam);
            return new ContentResultForm<>(true, actFlowParamVo);
        } catch (Exception e) {
            e.printStackTrace();
            return new ContentResultForm<>(false, null, "获取流程参数失败：" + e.getMessage());
        }
    }

    @GetMapping("/authorize/elements")
    @ApiOperation(value = "审批详情页 --> 按钮权限控制接口")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "version", value = "流程版本", required = true, dataType = "int", paramType = "query", readOnly = true)
            , @ApiImplicitParam(name = "viewId", value = "所属视图ID", required = true, dataType = "string", paramType = "query", readOnly = true)
            , @ApiImplicitParam(name = "actId", value = "所属流程节点ID", required = true, dataType = "string", paramType = "query", readOnly = true)
            , @ApiImplicitParam(name = "privMode", value = "权限模式，act表示流程节点控制，view表示视图控制，view-act表示视图及流程节点联合控制", required = true, dataType = "string", paramType = "query", readOnly = true, defaultValue = "act")
            , @ApiImplicitParam(name = "appFlowdefId", value = "所属流程设置ID", required = true, dataType = "string", paramType = "query", readOnly = true)
    })
    public ResultForm getAuthorizeElement(int version, String viewId, String actId, String privMode, String appFlowdefId) {
        Map elementMap;
        if (version > 0 && !StringUtils.isEmpty(privMode) && !StringUtils.isEmpty(appFlowdefId)) {
            elementMap = bpmTaskFrontService.getAuthorizeElement(version, privMode, viewId, actId, appFlowdefId);
            return new ContentResultForm<>(true, elementMap);
        }
        return new ResultForm(true, "无权限配置信息");
    }

    @GetMapping("/comment/tree")
    @ApiOperation(value = "审批详情页 --> 右侧对话视图, 查询审批意见树")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "processInstanceId", value = "流程实例id", required = true, dataType = "string", paramType = "query", readOnly = true)
            , @ApiImplicitParam(name = "applyinstId", value = "申报实例id", required = true, dataType = "string", paramType = "query", readOnly = true)
    })
    public ContentResultForm<List<BpmHistoryCommentFormVo>> listHistoryCommentTree(String processInstanceId, String applyinstId) {
        log.info("查询审批意见树, processInstanceId: {}, applyinstId: {}", processInstanceId, applyinstId);
        try {
            List<BpmHistoryCommentFormVo> bpmHistoryCommentFormVos = restBpmService.listHistoryCommentTree(processInstanceId, true, applyinstId);
            return new ContentResultForm<>(true, bpmHistoryCommentFormVos, "查询成功");
        } catch (Exception e) {
            e.printStackTrace();
            return new ContentResultForm<>(false, null, "查询失败: " + e.getMessage());
        }
    }

    @GetMapping("/comment/list")
    @ApiOperation(value = "审批详情页 --> 查询审批意见列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "processInstanceId", value = "流程实例id", required = true, dataType = "string", paramType = "query", readOnly = true)
            , @ApiImplicitParam(name = "isUnit", value = "没有用到，不用传", dataType = "string", paramType = "query", readOnly = true)
            , @ApiImplicitParam(name = "applyinstId", value = "申报实例id", required = true, dataType = "string", paramType = "query", readOnly = true)
    })
    public ContentResultForm<List<BpmHistoryCommentFormVo>> listHistoryCommentAllByTaskId(@RequestParam String processInstanceId, @RequestParam(required = false) String isUnit, @RequestParam String applyinstId) {

        log.info("查询审批意见列表, processInstanceId: {}, applyinstId: {}", processInstanceId, applyinstId);
        try {
            List<BpmHistoryCommentFormVo> bpmHistoryCommentFormVos = restBpmService.listHistoryCommentAll(processInstanceId, isUnit, applyinstId);
            return new ContentResultForm<>(true, bpmHistoryCommentFormVos, "查询成功");
        } catch (Exception e) {
            e.printStackTrace();
            return new ContentResultForm<>(false, null, "查询审批意见列表失败: " + e.getMessage());
        }
    }

    @GetMapping("/comment/item")
    @ApiOperation(value = "审批详情页 --> 查询单个事项审批意见列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "iteminstId", value = "事项实例id", required = true, dataType = "string", paramType = "query", readOnly = true)
    })
    public ResultForm listHistoryCommentByIteminstId(@RequestParam String iteminstId) {
        if (StringUtils.isBlank(iteminstId)) {
            return new ResultForm(false, "参数事项实例id不能为空！");
        }
        log.info("查询单个事项审批意见列表, iteminstId: {}", iteminstId);
        try {
            List<BpmHistoryCommentFormVo> bpmHistoryCommentFormVos = restBpmService.listHistoryCommentByIteminstId(iteminstId);
            return new ContentResultForm<>(true, bpmHistoryCommentFormVos, "查询成功");
        } catch (Exception e) {
            e.printStackTrace();
            return new ContentResultForm<>(false, null, "查询单个事项审批意见列表失败: " + e.getMessage());
        }
    }


    @GetMapping("/comments/list/all/old")
    @ApiOperation(value = "4.0审批页 --> 查询审批意见列表")
    @ApiImplicitParam(name = "processInstanceId", value = "流程实例id", required = true, dataType = "string", paramType = "query", readOnly = true)
    public ResultForm getCommentList(String processInstanceId) throws Exception {
        //fixme 目前写死支持到4级，待完善
        List<ExtendBpmHistoryCommentForm> historyCommentTree = bpmProcessFrontService.getHistoryCommentTree(processInstanceId, true);
        List<HistoryCommentsVo> voList = new ArrayList<>();
        //最后一级节点
        List<HistoryCommentsVo> lastNodeList = new ArrayList<>();
        for (ExtendBpmHistoryCommentForm commentForm : historyCommentTree) {
            String nodeName = commentForm.getNodeName();//一级节点名称
            List<ExtendBpmHistoryCommentForm> childNode = commentForm.getChildNode();//二级节点列表
            if (null != childNode && childNode.size() > 0) {
                HistoryCommentsVo vo1 = new HistoryCommentsVo();
                List<HistoryCommentsVo.Comment> commentList1 = new ArrayList<>();
                vo1.setNodeName(nodeName);
                boolean flag = false;
                HistoryCommentsVo vo2 = new HistoryCommentsVo();
                List<HistoryCommentsVo> voList2 = new ArrayList<>();
                vo2.setNodeName(nodeName);
                for (ExtendBpmHistoryCommentForm form : childNode) {
                    String nodeName2 = form.getNodeName();
                    List<ExtendBpmHistoryCommentForm> childNode1 = form.getChildNode();//三级节点
                    if (null != childNode1 && childNode1.size() > 0) {//存在三级节点
                        HistoryCommentsVo vo = new HistoryCommentsVo();
                        vo.setNodeName(nodeName2);
                        List<HistoryCommentsVo.Comment> commentList = new ArrayList<>();
                        childNode1 = childNode1.stream().sorted(Comparator.comparing(ExtendBpmHistoryCommentForm::getSigeInDate).reversed()).collect(Collectors.toList());

                        for (int i = 0; i < childNode1.size(); i++) {
                            ExtendBpmHistoryCommentForm form3 = childNode1.get(i);
                            if (childNode1.size() - i > 1 && StringUtils.isBlank(form3.getCommentMessage()) && form3.getEndDate() == null) {
                                continue;
                            }
                            HistoryCommentsVo.Comment comment = new HistoryCommentsVo.Comment();
                            BeanUtils.copyProperties(form3, comment);
                            String isApprover = form3.getIsApprover();
                            Date sigeInDate = form3.getSigeInDate();
                            String format = this.format.format(sigeInDate);
                            comment.setStartDate(format);
                            comment.setTaskAssignee((StringUtils.isBlank(form3.getOrgName()) || StringUtils.isBlank(comment.getTaskAssignee())) ? null : form3.getOrgName() + "-" + comment.getTaskAssignee());
                            if (com.augurit.agcloud.framework.util.StringUtils.isNotBlank(isApprover) && "1".equals(isApprover)) {
                                comment.setIsPass(true);
                            } else {
                                comment.setIsPass(false);
                            }
                            commentList.add(comment);
                        }

                        vo.setCommentList(commentList);
                        voList2.add(vo);
                    } else {//不存在三级节点
                        flag = true;
                        HistoryCommentsVo.Comment comment = new HistoryCommentsVo.Comment();
                        BeanUtils.copyProperties(form, comment);
                        String isApprover = form.getIsApprover();
                        Date sigeInDate = form.getSigeInDate();
                        String format = this.format.format(sigeInDate);
                        comment.setStartDate(format);
                        if (com.augurit.agcloud.framework.util.StringUtils.isNotBlank(isApprover) && "1".equals(isApprover)) {
                            comment.setIsPass(true);
                        } else {
                            comment.setIsPass(false);
                        }
                        commentList1.add(comment);

                    }

                }

                if (voList2.size() > 0) {
                    if (commentList1.size() > 0) {
                        for (HistoryCommentsVo.Comment comment : commentList1) {
                            List<HistoryCommentsVo.Comment> comments = new ArrayList();
                            comments.add(comment);
                            HistoryCommentsVo vo = new HistoryCommentsVo();
                            vo.setNodeName(comment.getNodeName());
                            vo.setCommentList(comments);
                            voList2.add(vo);
                        }
                    }

                    vo2.setHistoryCommentsVos(voList2);
                    voList.add(vo2);
                }

                if (flag && voList2.size() < 1) {
                    vo1.setCommentList(commentList1);
                    voList.add(vo1);
                }


            } else {//不存在二级节点
                changeAddFormToVo(voList, commentForm, nodeName);
            }
        }
        return new ContentResultForm<>(true, voList);
    }

    private void changeAddFormToVo(List<HistoryCommentsVo> voList, ExtendBpmHistoryCommentForm form, String nodeName) {
        HistoryCommentsVo.Comment comment = new HistoryCommentsVo.Comment();
        BeanUtils.copyProperties(form, comment);
        String isApprover = form.getIsApprover();
        Date sigeInDate = form.getSigeInDate();
        String format = this.format.format(sigeInDate);
        comment.setStartDate(format);
        if (com.augurit.agcloud.framework.util.StringUtils.isNotBlank(isApprover) && "1".equals(isApprover)) {
            comment.setIsPass(true);
        } else {
            comment.setIsPass(false);
        }
        List<HistoryCommentsVo.Comment> commentList = new ArrayList<>();
        commentList.add(comment);
        HistoryCommentsVo vo = new HistoryCommentsVo();
        vo.setCommentList(commentList);
        vo.setNodeName(nodeName);
        voList.add(vo);
    }

    @GetMapping("/comments/list/all")
    @ApiOperation(value = "4.0审批页 --> 查询审批意见列表，平铺")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "processInstanceId", value = "流程实例id", required = true, dataType = "string", paramType = "query", readOnly = true),
            @ApiImplicitParam(name = "applyinstId", value = "申请实例id", required = true, dataType = "string", paramType = "query", readOnly = true)
    })
    public ResultForm testCommentList(String processInstanceId, String applyinstId) throws Exception {
        List<HistoryCommentsVo> voList = restBpmService.commentList(processInstanceId, applyinstId);
        return new ContentResultForm<>(true, voList);
    }

    @GetMapping("/item/supplement")
    @ApiOperation(value = "4.0审批页 --> 审批过程表单事项审批节点查看补正记录")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "iteminstId", value = "事项实例id", required = true, dataType = "string", paramType = "query", readOnly = true)
    })
    public ResultForm itemSupplementRecord(String iteminstId) throws Exception {
        if (StringUtils.isBlank(iteminstId)) {
            return new ResultForm(false, "参数事项实例id不能为空！");
        }
        List<HistoryCommentsVo> voList = restBpmService.itemSupplementRecord(iteminstId);
        return new ContentResultForm<>(true, voList);
    }

    @GetMapping("/task/comment")
    @ApiOperation(value = "4.0审批页 --> 获取任务节点的审批意见")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "taskId", value = "任务id", required = true, dataType = "string", paramType = "query", readOnly = true)
    })
    public ResultForm getTaskComment(String taskId) throws Exception {
        if (StringUtils.isBlank(taskId)) {
            return new ResultForm(false, "参数任务id不能为空！");
        }
        List<HistoryCommentsVo> voList = restBpmService.getTaskComment(taskId);
        return new ContentResultForm<>(true, voList);
    }

    @GetMapping("/lastTask/comment")
    @ApiOperation(value = "4.0审批页 --> 获取任务节点的前一个节点的审批意见")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "taskId", value = "任务id", required = true, dataType = "string", paramType = "query", readOnly = true)
    })
    public ResultForm getLastTaskComment(String taskId) throws Exception {
        if (StringUtils.isBlank(taskId)) {
            return new ResultForm(false, "参数任务id不能为空！");
        }
        List<HistoryCommentsVo> voList = restBpmService.getLastTaskComment(taskId);
        return new ContentResultForm<>(true, voList);
    }

    @PostMapping("/informCommitIteminst/withdraw")
    @ApiOperation(value = "撤回告知承诺制办件（如果是并联申报，则一起撤回所有办件）")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "applyinstId", value = "申请实例id", required = true, dataType = "string", paramType = "query", readOnly = true)
    })
    public ResultForm withdrawInformCommitIteminst(String applyinstId) throws Exception {
        if (StringUtils.isBlank(applyinstId)) {
            return new ResultForm(false, "申请实例id不能为空！");
        }
        return restBpmService.withdrawInformCommitIteminst(applyinstId);
    }
}
