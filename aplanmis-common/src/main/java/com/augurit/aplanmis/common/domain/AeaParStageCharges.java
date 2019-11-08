package com.augurit.aplanmis.common.domain;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;

/**
 * 阶段办事指南收费项目信息
 *
 * @author jjt
 * @date 2016/10/31
 */
@ApiModel(description = "阶段办事指南收费项目")
@Data
public class AeaParStageCharges implements Serializable{

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @ApiModelProperty(value = "主键")
    private String chargeId;

    /**
     * 阶段ID
     */
    @ApiModelProperty(value = "阶段ID")
    private String stageId;

    /**
     * 收费项目名称
     */
    @ApiModelProperty(value = "收费项目名称")
    private String feeName;

    /**
     * 收费主体
     */
    @ApiModelProperty(value = "收费主体")
    private String feeOrg;

    /**
     * 收费性质 1 行政事业性收费 2 经营服务性收费
     */
    @ApiModelProperty(value = "收费性质 1 行政事业性收费 2 经营服务性收费")
    private String feeType;

    /**
     * 收费性质文本
     */
    @ApiModelProperty(value = "收费性质文本")
    private String feeTypeText;

    /**
     * 收费依据
     */
    @ApiModelProperty(value = "收费依据")
    private String bylaw;

    /**
     * 是否允许减免 1是 0否
     */
    @ApiModelProperty(value = "是否允许减免 1是 0否")
    private String isDesc;

    /**
     * 是否允许减免文本
     */
    @ApiModelProperty(value = "是否允许减免文本")
    private String isDescText;

    /**
     * 收费减免情形说明
     */
    @ApiModelProperty(value = "收费减免情形说明")
    private String descExplain;

    /**
     * 减免法律依据
     */
    @ApiModelProperty(value = "减免法律依据")
    private String descLaw;

    /**
     * 收费标准
     */
    @ApiModelProperty(value = "收费标准")
    private String feeStand;

    /**
     * 收费项目备注
     */
    @ApiModelProperty(value = "收费项目备注")
    private String remark;

    /**
     * 收费项目排序
     */
    @ApiModelProperty(value = "收费项目排序")
    private Long sortNo;

    /**
     * 创建人
     */
    @ApiModelProperty(value = "创建人")
    private String creater;

    /**
     * 创建时间
     */
    @ApiModelProperty(value = "创建时间")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    /**
     * 修改人
     */
    @ApiModelProperty(value = "修改人")
    private String modifier;

    /**
     * 修改时间
     */
    @ApiModelProperty(value = "修改时间")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private Date modifyTime;

    /**
     * 根组织ID
     */
    @ApiModelProperty(value = "根组织id")
    private String rootOrgId;

    /**
     * 关键字查询
     */
    @ApiModelProperty(value = "关键字查询")
    private String keyword;
}
