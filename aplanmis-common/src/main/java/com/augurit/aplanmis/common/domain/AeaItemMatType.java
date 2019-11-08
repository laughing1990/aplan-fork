package com.augurit.aplanmis.common.domain;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;

/**
 * 材料类型表-模型
 *
 * @author jjt
 * @date 2019/08/19
 *
 */
@ApiModel(description = "材料类型实体类")
@Data
public class AeaItemMatType implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("主键")
    private String matTypeId;

    @ApiModelProperty("材料类型编号")
    private String typeCode;

    @ApiModelProperty("材料类型名称")
    private String typeName;

    @ApiModelProperty("排列顺序号")
    private Long sortNo;

    @ApiModelProperty("父ID")
    private String parentTypeId;

    @ApiModelProperty("序列")
    private String typeSeq;

    @ApiModelProperty("子节点数")
    private Long subCount;

    @ApiModelProperty("备注说明")
    private String typeMemo;

    @ApiModelProperty("是否启用，0表示禁用，1表示启用")
    private String isActive;

    @ApiModelProperty("是否逻辑删除 0表示正常记录，1表示已删除记录")
    private String isDeleted;

    @ApiModelProperty("创建人")
    private String creater;
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")

    @ApiModelProperty("创建时间")
    private Date createTime;

    @ApiModelProperty("修改人")
    private String modifier;

    @ApiModelProperty("修改时间")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date modifyTime;

    @ApiModelProperty("根组织ID")
    private String rootOrgId;

    /**
     * 扩展字段：关键字查询
     */
    @ApiModelProperty("扩展字段：关键字查询")
    private String keyWord;
}
