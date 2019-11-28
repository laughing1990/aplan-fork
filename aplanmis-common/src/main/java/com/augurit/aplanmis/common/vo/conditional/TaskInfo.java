package com.augurit.aplanmis.common.vo.conditional;

import com.augurit.agcloud.bpm.common.domain.ActStoRemindAndReceiver;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * @author tiantian
 * @date 2019/9/19
 */
@Data
@ApiModel(value = "查询返回信息")
@EqualsAndHashCode(callSuper = true)
public class TaskInfo extends BaseInfo implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "任务实例id")
    private String taskId;

    @ApiModelProperty(value = "任务节点定义id")
    private String taskDefKey;

    @ApiModelProperty(value = "事项实例id")
    private String iteminstId;

    @ApiModelProperty(value = "受理时间")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm", locale = "zh", timezone = "GMT+8")
    private Date acceptTime;

    @ApiModelProperty(value = "当前节点")
    private String taskName;

    @ApiModelProperty(value = "业务ID")
    private String busRecordId;

    @ApiModelProperty(value = "签收状态")
    private Double signState;

    @ApiModelProperty(value = "视图id")
    private String viewId;

    @ApiModelProperty(value = "办理时间")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm", locale = "zh", timezone = "GMT+8")
    private Date processTime;

    @ApiModelProperty(value = "补正id")
    private String correctId;

    @ApiModelProperty(value = "是否已出件")
    private String isSmsSend;

    @ApiModelProperty(value = "办结时间")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm", locale = "zh", timezone = "GMT+8")
    private Date concludedTime;

    @ApiModelProperty(value = "不予受理时间")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm", locale = "zh", timezone = "GMT+8")
    private Date dismissedTime;

    @ApiModelProperty(value = "处理人")
    private String assignee;

    @ApiModelProperty(value = "到达时间")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm", locale = "zh", timezone = "GMT+8")
    private Date arriveTime;

    @ApiModelProperty(value = "实施主体")
    private String organizer;

    @ApiModelProperty(value = "设定补正/补全时间")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm", locale = "zh", timezone = "GMT+8")
    private Date correctDueTime;

    @ApiModelProperty(value = "补正/补全时间")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm", locale = "zh", timezone = "GMT+8")
    private Date correctTime;

    @ApiModelProperty(value = "回执id")
    private String receiveId;

    @ApiModelProperty("特殊程序件办理方式")
    private String specialDueWay;

    @ApiModelProperty(value = "特别程序类型，来自于数据字典，包括现场听证、现场勘察、招投标、专家评审、环评公示等")
    private String specialType;

    @ApiModelProperty(value = "特殊程序办理天数")
    private int specialDueDays;

    @ApiModelProperty(value = "特殊程序办理期限")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd", locale = "zh", timezone = "GMT+8")
    private Date specialDueTime;

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm", locale = "zh", timezone = "GMT+8")
    private Date specialCreateTime;

    @ApiModelProperty(value = "提醒信息列表")
    private List<ActStoRemindAndReceiver> remindList;

    @ApiModelProperty(value = "节点时限列表")
    private List nodeTimelimitList;

    @ApiModelProperty(value = "事项性质大分类：0-行政事项，8-中介服务事项，9-服务协同，6-市政公用服务")
    private String itemNature;


}
