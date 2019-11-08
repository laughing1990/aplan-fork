package com.augurit.aplanmis.common.domain;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

/**
 * 事项窗口关联表
 *
 * @author jjt
 * @date 2019/10/14
 *
 */
@Data
@ApiModel(description = "事项窗口关联表")
public class AeaServiceWindowItem extends AeaServiceWindow  {

    private static final long serialVersionUID = 1L;

    /**
     * 主键ID
     */
    @ApiModelProperty(value = "主键ID")
    private String windItemId;

    /**
     * 窗口ID
     */
    @ApiModelProperty(value = "窗口ID")
    private String windId;

    /**
     * 事项版本ID
     */
    @ApiModelProperty(value = "事项版本ID")
    private String itemVerId;

    /**
     * 是否启用，0表示禁用，1表示启用
     */
    @ApiModelProperty(value = "是否启用，0表示禁用，1表示启用")
    private String isActive;

    /**
     * 创建人
     */
    @ApiModelProperty(value = "创建人")
    private String creater;

    /**
     * 创建时间
     */
    @ApiModelProperty(value = "创建时间")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private java.util.Date createTime;

    /**
     * 扩展字段: 事项名称
     */
    @ApiModelProperty(value = "扩展字段: 事项名称")
    private String itemName;

    /**
     * 扩展字段: 事项编码
     */
    @ApiModelProperty(value = "扩展字段: 事项编码")
    private String itemCode;

    /**
     * 扩展字段: 事项编码
     */
    @ApiModelProperty(value = "扩展字段: 事项性质大分类：0-行政事项，8-中介服务事项，9-服务协同，6-市政公用服务")
    private String itemNature;
}
