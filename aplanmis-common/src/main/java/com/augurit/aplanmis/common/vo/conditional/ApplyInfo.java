package com.augurit.aplanmis.common.vo.conditional;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;

/**
 * 申报数据
 * @author tiantian
 * @date 2019/9/4
 */
@EqualsAndHashCode(callSuper = true)
@Data
@ApiModel(value = "申报查询返回信息")
public class ApplyInfo extends BaseInfo implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "申报状态")
    private String applyState;

    @ApiModelProperty(value = "办结时间")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm", locale = "zh", timezone = "GMT+8")
    private Date concludedTime;

    @ApiModelProperty(value = "任务实例id")
    private String taskId;

    @ApiModelProperty(value = "视图id")
    private String viewId;

}
