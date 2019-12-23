package com.augurit.aplanmis.common.domain;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;

@Data
@ApiModel(description = "按组织征求的人员名单表")
public class AeaSolicitOrgUser implements Serializable{

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("主键")
    private String orgUserId;

    @ApiModelProperty("按组织征求配置ID")
    private String solicitOrgId; 

    @ApiModelProperty("用户ID")
    private String userId; 

    @ApiModelProperty("排列顺序号")
    private Long sortNo; 

    @ApiModelProperty("是否启用，0表示禁用，1表示启用")
    private String isActive; 

    @ApiModelProperty("创建人")
    private String creater; 

    @ApiModelProperty("创建时间")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private java.util.Date createTime; 

    @ApiModelProperty("修改人")
    private String modifier; 

    @ApiModelProperty("修改时间")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private java.util.Date modifyTime; 
}
