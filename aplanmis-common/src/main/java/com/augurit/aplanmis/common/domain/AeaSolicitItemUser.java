package com.augurit.aplanmis.common.domain;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;

@Data
@ApiModel(description = "按事项征求的人员名单表")
public class AeaSolicitItemUser implements Serializable{

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("主键")
    private String itemUserId;

    @ApiModelProperty("按组织征求配置ID")
    private String solicitItemId; 

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

    @ApiModelProperty("扩展字段：姓名")
    private String userName;

    @ApiModelProperty("扩展字段：登录名")
    private String loginName;

    @ApiModelProperty("扩展字段：性别")
    private String userSex;

    @ApiModelProperty("扩展字段：手机号")
    private String userMobile;

    @ApiModelProperty("扩展字段：关键字查询")
    private String keyword;
}
