package com.augurit.aplanmis.common.domain;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;

/**
* -模型
*/
@ApiModel("资质等表级实体")
@Data
public class AeaImQualLevel implements Serializable{

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "等级ID")
    private String qualLevelId; // ()
    @ApiModelProperty(value = "资质等级编码")
    private String qualLevelCode; // (资质等级编码)
    @ApiModelProperty(value = "资质等级名称")
    private String qualLevelName; // (资质等级名称)
    @ApiModelProperty(value = "父ID")
    private String parentQualLevelId; // (父ID)
    @ApiModelProperty(value = "序列")
    private String qualLevelSeq; // (序列)
    @ApiModelProperty(value = "优先级")
    private String priority; // (优先级)
    private String isDelete; // (是否已删除：1 已删除，0 未删除)
    private String creater; // ()
    @DateTimeFormat(pattern="yyyy-MM-dd")
        private java.util.Date createTime; // ()
        private String memo; // (备注)

    private String rootOrgId;//根组织ID

}
