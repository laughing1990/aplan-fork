package com.augurit.aplanmis.common.domain;

import com.augurit.agcloud.bsc.util.UuidUtil;
import com.augurit.agcloud.framework.ui.ztree.ZtreeNode;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.apache.commons.lang3.StringUtils;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.util.Assert;

import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.List;

/**
 * 事项情形定义表-模型
 */
@Data
@ApiModel("事项情形实体类")
@EqualsAndHashCode(callSuper = true)
public class AeaItemState extends ZtreeNode implements Serializable {

    private static final long serialVersionUID = 1L;
    /**
     *   (主键)
     */
    private String itemStateId;

    private String itemId; 

    private String itemVerId; // (事项定义ID)

    private String stateVerId; // (情形版本ID)

    @ApiModelProperty(value = "情形名称")
    private String stateName; // (情形名称)

    private String stateEl; // (条件表达式)

    @Size(max = 38)
    private Long sortNo; // (排列顺序号)

    private String stateMemo; // (备注说明)

    private String isActive; // (是否启用，0表示禁用，1表示启用)

    private String creater; // (创建人)

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private java.util.Date createTime; // (创建时间)

    private String modifier; // (修改人)

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private java.util.Date modifyTime; // (修改时间)

    private String useEl; // (是否启用EL表达式，0表示不启用，1表示启用。默认为0)

    private String parentStateId; // (父ID(1表示父节点为事项))

    private String stateSeq; // (序列)

    @ApiModelProperty(value = "是否问题，0表示答案，1表示问题")
    private String isQuestion; // (是否问题，0表示答案，1表示问题)

    @ApiModelProperty(value = "问题的答案类型，y表示是否选择，s表示单选答案，m表示多选答案")
    private String answerType; // (问题的答案类型，y表示是否选择，s表示单选答案，m表示多选答案)

    @ApiModelProperty(value = "是否必须回答，0表示可选回答，1表示必须回答【IS_QUESTION=1】")
    private String mustAnswer; // (是否必须回答，0表示可选回答，1表示必须回答【IS_QUESTION=1】)

    private String isDeleted; // (是否删除，0表示未删除，1表示已删除)

    @ApiModelProperty(value = "是否流程  0否  1是")
    private String isProcStartCond;

    @ApiModelProperty(value = "是否告知承诺制  0否 1是")
    private String isInformCommit;

    //选择的子情形ID
    @ApiModelProperty(value = "选择的子情形ID")
    private String selectAnswerId="";

    /**
     * 根组织ID
     */
    private String rootOrgId;
    
    @ApiModelProperty(value = "父问题情形id")
    private String parentQuestionStateId;

    /*----------------拓展字段----------*/
    /** 情形答案列表*/
    List<AeaItemState> answerStates;

    private String keyword;
    private String verStatus;

    public void checkBeforeSave() {

        Assert.notNull(this.itemStateId, "事项情形id不能为空");
        Assert.notNull(this.itemId, "事项id不能为空");
        Assert.notNull(this.itemVerId, "事项版本id不能为空");
        Assert.notNull(this.stateVerId, "情形版本id不能为空");
        Assert.notNull(this.stateName, "情形名称不能为空");
        Assert.notNull(this.sortNo, "排序号未设置");
        Assert.notNull(this.isActive, "激活状态未设置");
        Assert.notNull(this.creater, "创建人未设置");
        Assert.notNull(this.createTime, "创建时间未设置");
        Assert.notNull(this.isQuestion, "是否问题未设置");
        Assert.notNull(this.isDeleted, "删除状态未设置");
    }

    public boolean firstLevelState() {

        return StringUtils.isBlank(parentStateId);
    }

    public AeaItemState copyOne() {

        AeaItemState newOne= new AeaItemState();
        newOne.setItemStateId(UuidUtil.generateUuid());
        newOne.setItemId(this.itemId);
        newOne.setItemVerId(this.itemVerId);
        newOne.setStateVerId(this.stateVerId);
        newOne.setStateName(this.stateName);
        newOne.setStateEl(this.stateEl);
        newOne.setSortNo(this.sortNo);
        newOne.setStateMemo(this.stateMemo);
        newOne.setIsActive(this.isActive);
        newOne.setCreater(this.creater);
        newOne.setCreateTime(this.createTime);
        newOne.setModifier(this.modifier);
        newOne.setModifyTime(this.modifyTime);
        newOne.setUseEl(this.useEl);
        newOne.setParentStateId(this.parentStateId);
        newOne.setStateSeq(this.stateSeq);
        newOne.setIsQuestion(this.isQuestion);
        newOne.setAnswerType(this.answerType);
        newOne.setMustAnswer(this.mustAnswer);
        newOne.setIsDeleted(this.isDeleted);
        newOne.setIsProcStartCond(this.isProcStartCond);
        newOne.setIsProcStartCond(this.isInformCommit);
        newOne.setRootOrgId(this.rootOrgId);
        return newOne;
    }
}
