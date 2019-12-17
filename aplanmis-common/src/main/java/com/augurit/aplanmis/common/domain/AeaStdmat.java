package com.augurit.aplanmis.common.domain;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;

/**
* 标准材料定义表-模型
*/
@Data
@ApiModel(description = "标准材料定义表")
public class AeaStdmat implements Serializable{

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("主键")
    private String stdmatId; 

    @ApiModelProperty("标准材料编号")
    private String stdmatCode; 

    @ApiModelProperty("标准材料名称")
    private String stdmatName; 

    @ApiModelProperty("标准材料类型ID")
    private String stdmatTypeId; 

    @ApiModelProperty("是否自动检测，0表示无自动检测，1表示新增材料定义时自动检测是否匹配关键字")
    private String isAutoCheck; 

    @ApiModelProperty("匹配关键字，以逗号分隔")
    private String checkKeywords; 

    @ApiModelProperty("排列顺序号")
    private Long sortNo; 

    @ApiModelProperty("备注说明")
    private String stdmatMemo; 

    @ApiModelProperty("是否启用，0表示禁用，1表示启用")
    private String isActive;

    @ApiModelProperty("是否逻辑删除")
    private String isDeleted;

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

    @ApiModelProperty("根组织id")
    private String rootOrgId;


    @ApiModelProperty("扩展字段：关键字查询")
    private String keyWord;
}
