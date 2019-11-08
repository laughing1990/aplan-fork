package com.augurit.aplanmis.common.domain;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;

@Data
@ApiModel(description = "阶段表单关联表")
public class AeaParStageOneform implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键ID")
    private String stageOneformId;

    @ApiModelProperty(value = "阶段ID")
    private String parStageId;

    @ApiModelProperty(value = "总表id")
    private String oneformId;

    @ApiModelProperty(value = "链接名称")
    private String linkName;

    @ApiModelProperty(value = "排序")
    private Double sortNo;

    @ApiModelProperty(value = "是否启用 1启用 0禁用")
    private String isActive;

    @ApiModelProperty(value = "创建人")
    private String creater;

    @ApiModelProperty(value = "创建时间")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    @ApiModelProperty(value = "修改人")
    private String modifier;

    @ApiModelProperty(value = "修改时间")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date modifyTime;

    @ApiModelProperty(value = "扩展字段：总表名称")
    private String oneformName;
}
