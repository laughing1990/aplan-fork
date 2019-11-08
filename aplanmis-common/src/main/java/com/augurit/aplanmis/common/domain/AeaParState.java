package com.augurit.aplanmis.common.domain;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.List;

/**
 * 主题情形定义表-模型
 */
@Data
@ApiModel("情形实体类")
public class AeaParState implements Serializable {

    private static final long serialVersionUID = 1L;
    private String parStateId;
    /**
     * 阶段定义ID
     */
    private String stageId;
    /**
     * 条件名称
     */
    private String stateName;
    /**
     * 是否启用EL表达式，0表示不启用，1表示启用。默认为0
     */
    @ApiModelProperty("是否启用EL表达式，0表示不启用，1表示启用。默认为0")
    private String useEl;
    /**
     * 条件表达式
     */
    @ApiModelProperty("条件表达式")
    private String stateEl;
    /**
     * 是否问题，0表示答案，1表示问题，2表示事项情形
     */
    @ApiModelProperty("0表示答案，1表示问题，2表示事项情形")
    private String isQuestion;
    /**
     * 问题的答案类型，y表示是否选择，s表示单选答案，m表示多选答案
     */
    @ApiModelProperty("问题的答案类型，y表示是否选择，s表示单选答案，m表示多选答案")
    private String answerType;
    /**
     * 是否必须回答，0表示可选回答，1表示必须回答【IS_QUESTION=1】
     */
    @ApiModelProperty("是否必须回答，0表示可选回答，1表示必须回答")
    private String mustAnswer;
    /**
     * 父ID
     */
    private String parentStateId;
    /**
     * 序列
     */
    private String stateSeq;
    /**
     * 排列顺序号
     */
    @Size(max = 38)
    private Long sortNo;
    /**
     * 是否启用，0表示禁用，1表示启用
     */
    private String isActive;
    /**
     * 备注说明
     */
    @ApiModelProperty("备注说明")
    private String stateMemo;
    /**
     * 创建人
     */
    private String creater;
    /**
     * 创建时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private java.util.Date createTime;
    /**
     * 修改人
     */
    private String modifier;
    /**
     * (修改时间)
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private java.util.Date modifyTime;
    /**
     * (是否删除，0表示未删除，1表示已删除)
     */
    private String isDeleted;
    /**
     * (存储原IS_QUESTION数据)
     */
    private String isQuestionOri;

    /**
     * 根组织ID
     */
    private String rootOrgId;

    /**
     * 是否流程 0否 1是
     */
    private String isProcStartCond;


    /*-------------------------------------拓展字段----------------*/
    /**
     * 问题答案列表--isQuestion==1
     */
    @ApiModelProperty("问题答案列表")
    List<AeaParState> answerStates;
    //选择的子情形ID
    @ApiModelProperty(value = "选择的子情形ID")
    private String selectAnswerId="";

    // 扩展字段
    private String keyword;
    @ApiModelProperty(value = "父问题情形ID")
    private String parentQuestionStateId;
}
