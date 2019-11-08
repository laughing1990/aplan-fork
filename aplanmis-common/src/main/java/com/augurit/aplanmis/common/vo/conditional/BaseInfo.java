package com.augurit.aplanmis.common.vo.conditional;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;

/**
 * @author tiantian
 * @date 2019/10/22
 */
@Data
@ApiModel(value = "查询返回基本信息")
public class BaseInfo implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "申报实例id")
    protected String applyinstId;

    @ApiModelProperty(value = "申报流水号")
    protected String applyinstCode;

    @ApiModelProperty(value = "项目名称")
    protected String projName;

    @ApiModelProperty(value = "联系人")
    protected String linkmanName;

    @ApiModelProperty(value = "项目代码")
    protected String localCode;

    @ApiModelProperty(value = "工程编码")
    protected String gcbm;

    @ApiModelProperty(value = "申报主题")
    protected String themeName;

    @ApiModelProperty(value = "申报类型")
    protected String applyType;

    @ApiModelProperty(value = "申报来源")
    protected String applySource;

    @ApiModelProperty(value = "阶段名")
    protected String stageName;

    @ApiModelProperty(value = "事项名")
    protected String itemName;

    @ApiModelProperty(value = "流程实例id")
    protected String procInstId;

    @ApiModelProperty(value = "申请时间")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern="yyyy-MM-dd HH:mm",locale = "zh", timezone = "GMT+8")
    protected Date applyTime;

    @ApiModelProperty(value = "剩余时间")
    protected Double remainingTime;

    @ApiModelProperty(value = "时限状态")
    protected String instState;

    @ApiModelProperty(value = "时间单位")
    protected String timeruleUnit;

    @ApiModelProperty(value = "逾期用时")
    protected Double overdueTime;

    @ApiModelProperty(value = "剩余/逾期用时显示文本")
    protected String remainingOrOverTimeText;

    @ApiModelProperty(value = "法定时限")
    protected Double dueNum;

    @ApiModelProperty(value = "法定时限显示")
    protected String dueNumText;

    @ApiModelProperty(value = "是否已办结,1为是,0为否")
    protected String isConcluding;

    @ApiModelProperty(value = "已用时")
    protected Double useLimitTime;

    @ApiModelProperty(value = "已用时显示")
    protected String useLimitTimeText;
}
