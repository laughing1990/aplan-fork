package com.augurit.aplanmis.common.domain;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;

/**
* 用户事项管理-模型
*/
@Data
@ApiModel("用户事项管理")
public class AeaItemUser implements Serializable{

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键")
    private String itemUserId; 

    @ApiModelProperty(value = "事项版本id")
    private String itemVerId; 

    @ApiModelProperty(value = "用户ID")
    private String userId; 

    @ApiModelProperty(value = "是否启用，0表示禁用，1表示启用")
    private String isActive; 

    @ApiModelProperty(value = "创建人")
    private String creater; 

    @ApiModelProperty(value = "创建时间")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private java.util.Date createTime; 

    @ApiModelProperty(value = "根组织ID")
    private String rootOrgId; 
}
