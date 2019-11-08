package com.augurit.aplanmis.common.domain;

import com.augurit.agcloud.framework.ui.ztree.ZtreeNode;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.annotation.Transient;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * 事项前提条件表-模型
 */
@Data
@ApiModel("前置条件实体")
public class AeaItemCond extends ZtreeNode implements Serializable {

    private static final long serialVersionUID = 1L;
    @ApiModelProperty(value = "前置条件ID")
    private String itemCondId; // (主键)
    @ApiModelProperty(value = "事项定义ID")
    private String itemId; // (事项定义ID)
    @ApiModelProperty(value = "事项版本ID")
    private String itemVerId;//事项版本
    @ApiModelProperty(value = "条件名称")
    private String condName; // (条件名称)
    private String condEl; // (条件表达式，XM_YDMJ>1000)
    @ApiModelProperty(value = "排列顺序号")
    private Long sortNo; // (排列顺序号)
    private String condMemo; // (备注说明)
    private String isActive; // (是否启用，0表示禁用，1表示启用)

    private String creater; // (创建人)
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime; // (创建时间)
    private String modifier; // (修改人)
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date modifyTime; // (修改时间)

    private String useEl; // (使用EL表达式  0启用  1禁用)
    @ApiModelProperty(value = "父ID")
    private String parentCondId; // (父ID)
    private String condSeq; // (序列)
    @ApiModelProperty(value = "是否问题，0表示答案，1表示问题")
    private String isQuestion; // (是否问题，0表示答案，1表示问题)
    @ApiModelProperty(value = "是否终止  1:是；0：否")
    private String sfzz; // (是否终止  1:是；0：否)
    @ApiModelProperty(value = "判断是否多选，为空时符合1个则通过，不为空需要符合对应的数量才能通过")
    private Long muiltSelect; // (为空时符合1个则通过，不为空需要符合对应的数量才能通过)
    private String rootOrgId; //根组织ID

    // 扩展字段
    private String keyword;

    @Transient
    @Lazy
    private List<AeaItemCond> children;

    List<AeaItemCond> childCondList;
}
