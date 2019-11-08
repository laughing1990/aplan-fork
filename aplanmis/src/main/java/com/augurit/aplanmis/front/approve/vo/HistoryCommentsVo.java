package com.augurit.aplanmis.front.approve.vo;

import com.augurit.agcloud.bpm.front.vo.ExtendBpmHistoryCommentForm;
import com.augurit.agcloud.framework.util.StringUtils;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.beans.BeanUtils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel("审批意见列表")
public class HistoryCommentsVo {
    @ApiModelProperty(value = "节点或事项名称")
    private String nodeName;
    @ApiModelProperty(value = "是否事项；true 事项，false 节点")
    private Boolean item;
    private List<Comment> commentList;
    private List<HistoryCommentsVo> historyCommentsVos;


    @Data
    public static class Comment {
        @ApiModelProperty(value = "任务ID", required = true, dataType = "string")
        private String taskId;

        @ApiModelProperty(value = "节点名称", required = true, dataType = "string")
        private String nodeName;

        @ApiModelProperty(value = "审批意见", required = true, dataType = "string")
        private String commentMessage;

        @ApiModelProperty(value = "任务签收人", required = true, dataType = "string")
        private String taskAssignee;
        @ApiModelProperty(value = "信号日期", required = true, dataType = "date")
        private Date sigeInDate;
        @ApiModelProperty(value = "办理日期", required = true, dataType = "date")
        private String startDate;

        @ApiModelProperty(value = "结束日期", required = true, dataType = "date")
        private Date endDate;

        @ApiModelProperty(value = "任务状态", required = true, dataType = "int")
        private Integer taskState;

        @ApiModelProperty(value = "组织id", required = true, dataType = "string")
        private String orgId;

        @ApiModelProperty(value = "组织名称", required = true, dataType = "string")
        private String orgName;

        @ApiModelProperty(value = "用户手机", required = true, dataType = "string")
        private String userMobile;

        @ApiModelProperty(value = "是否批准", required = true, dataType = "string")
        private String isApprover;

        @ApiModelProperty(value = "是否通过", required = true, dataType = "string")
        private Boolean isPass;

        @ApiModelProperty(value = "任务个数", required = true, dataType = "int")
        private Integer attDetailNum;
    }

    public static Comment changeToComments(ExtendBpmHistoryCommentForm extendForm) {

        Comment comment = new Comment();
        BeanUtils.copyProperties(extendForm, comment);

        //设置通过标识
        String isApprover = extendForm.getIsApprover();
        if (StringUtils.isNotBlank(isApprover) && "1".equals(isApprover)) {
            comment.setIsPass(true);
        } else {
            comment.setIsPass(false);
        }
        //设置签收人拼接部门
        comment.setTaskAssignee((StringUtils.isBlank(extendForm.getOrgName()) || StringUtils.isBlank(comment.getTaskAssignee())) ? null : extendForm.getOrgName() + "-" + comment.getTaskAssignee());

        //格式化签收时间
        Date sigeInDate = comment.getSigeInDate();
        if (null != sigeInDate) {
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");
            String dateStr = format.format(sigeInDate);
            comment.setStartDate(dateStr);
        }
        return comment;
    }

}
