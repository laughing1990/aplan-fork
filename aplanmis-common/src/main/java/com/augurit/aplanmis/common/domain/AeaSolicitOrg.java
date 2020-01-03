package com.augurit.aplanmis.common.domain;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;

@Data
@ApiModel(description = "按组织征求配置表")
public class AeaSolicitOrg implements Serializable{

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("主键")
    private String solicitOrgId; 

    @ApiModelProperty("征求部门ID")
    private String orgId;

    @ApiModelProperty("是否按业务类型征求，0表示按阶段征求部门意见（当前部门为阶段牵头部门），1表示按业务类型征求部门意见")
    private String isBusSolicit;

    @ApiModelProperty("阶段ID【当IS_BUS_SOLICIT=0】")
    private String stageId;

    @ApiModelProperty("征求业务类型，来自于数据字典")
    private String busType;

    @ApiModelProperty("征求意见模式：0表示多人征求模式，1表示单人征求模式)")
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

    @ApiModelProperty("扩展字段：组织名称")
    private String orgName;

    @ApiModelProperty("扩展字段：组织编号")
    private String orgCode;

    @ApiModelProperty("组织属性。u表示单位，d表示部门，g表示工作组")
    private String orgProperty;

    @ApiModelProperty("扩展字段：关键字查询")
    private String keyword;
}
