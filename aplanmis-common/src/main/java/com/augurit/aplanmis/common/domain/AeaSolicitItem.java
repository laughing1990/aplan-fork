package com.augurit.aplanmis.common.domain;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;

@Data
@ApiModel(description = "按事项征求配置表")
public class AeaSolicitItem implements Serializable{

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("主键")
    private String solicitItemId; 

    @ApiModelProperty("事项ID")
    private String itemId; 

    @ApiModelProperty("事项版本ID")
    private String itemVerId; 

    @ApiModelProperty("征求意见模式：0表示多人征求模式，1表示单人征求模式")
    private String solicitType; 

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

    @ApiModelProperty("根组织ID")
    private String rootOrgId;
}
