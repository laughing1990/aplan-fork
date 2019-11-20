package com.augurit.aplanmis.common.domain;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.Size;
import java.io.Serializable;

/**
 * 阶段前置检查-模型
 *
 * @author jjt
 * @date 2019/10/15
 *
 */
@ApiModel(description = "阶段前置检查")
@Data
public class AeaParFrontItem implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("主键id")
    private String frontItemId;

    @ApiModelProperty("阶段id")
    private String stageId;

    @ApiModelProperty("前置事项版本id")
    private String itemVerId;

    @ApiModelProperty("排序字段")
    private Long sortNo;

    @ApiModelProperty("是否启用")
    private String isActive;

    @ApiModelProperty("创建人")
    private String creater;

    @ApiModelProperty("创建时间")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private java.util.Date createTime;

    @ApiModelProperty("'修改人")
    private String modifier;

    @ApiModelProperty("'修改时间")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private java.util.Date modifyTime;

    @ApiModelProperty("根组织id")
    private String rootOrgId;

    @ApiModelProperty("备注")
    private String frontItemMemo;

    // ========== 扩展字段 ===========

    @ApiModelProperty("扩展字段：前置事项id")
    private String itemId;

    @ApiModelProperty("扩展字段：前置事项名称")
    private String itemName;

    @ApiModelProperty("扩展字段：前置事项编号")
    private String itemCode;

    @ApiModelProperty("扩展字段：前置事项是否标准事项  1标准事项 0 实施事项")
    private String isCatalog;

    @ApiModelProperty("扩展字段：前置事项所属组织")
    private String orgName;

    @ApiModelProperty("扩展字段：前置事项标准事项指导部门")
    private String guideOrgName;

    @ApiModelProperty("扩展字段：查询关键字")
    private String keyword;
}
