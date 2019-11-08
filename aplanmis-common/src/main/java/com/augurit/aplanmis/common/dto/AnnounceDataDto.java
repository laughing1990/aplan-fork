package com.augurit.aplanmis.common.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.Size;
import java.util.Date;

/**
 *  公示审批数据实体类
 */
@Data
@ApiModel("公示审批数据实体类")
public class AnnounceDataDto {
    @ApiModelProperty("申报流水号/受理号")
    private String applyinstCode;  //申报流水号/受理号
    @ApiModelProperty("项目名称")
    private String projName; //项目名称
    @ApiModelProperty("申报时间")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date startTime;//申报时间
    @ApiModelProperty("办结时间")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date endTime;//办结时间
    @ApiModelProperty("事项实例状态")
    private String iteminstState;//事项实例状态
    @ApiModelProperty("事项名称")
    private String itemName;//事项名称
    @ApiModelProperty("受理部门")
    private String orgName;//受理部门
    @Size(max = 38)
    @ApiModelProperty("办理时限，以工作日为单位")
    private Long dueNum; // (办理时限，以工作日为单位)
}
