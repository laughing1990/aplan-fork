package com.augurit.aplanmis.common.domain;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 主题导航因子表-模型
 */
@Data
public class AeaParFactor implements Serializable {

    private static final long serialVersionUID = 1L;

    private String factorId; // (主键)
    private String navId; // (阶段定义ID)
    private String factorName; // (条件名称)
    private String useEl; // (是否启用EL表达式，0表示不启用，1表示启用。默认为0)
    private String stateEl; // (条件表达式)
    private String isQuestion; // (是否问题，0表示答案，1表示问题，2表示事项情形)
    private String answerType; // (问题的答案类型，y表示是否选择，s表示单选答案，m表示多选答案)
    private String mustAnswer; // (是否必须回答，0表示可选回答，1表示必须回答【IS_QUESTION=1】)
    private String parentFactorId; // (父ID)
    private String parentQuestionFactorId;// (父问题ID)
    private String factorSeq; // (序列)
    @Size(max = 38)
    private Long sortNo; // (排列顺序号)
    private String isActive; // (是否启用，0表示禁用，1表示启用)
    private String factorMemo; // (备注说明)
    private String creater; // (创建人)
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private java.util.Date createTime; // (创建时间)
    private String modifier; // (修改人)
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private java.util.Date modifyTime; // (修改时间)
    private String isDeleted; // (是否删除，0表示未删除，1表示已删除)
    private String isQuestionOri; // (存储原IS_QUESTION数据)
    private String rootOrgId; // ()

    //==========扩展字段========
    private String themeId;
    private String themeName;
    private String keyword;
    @ApiModelProperty("问题答案列表")
    private List<AeaParFactor> answerFactors = new ArrayList<>();

    // 树结构需要
    private boolean hasChildren = false;
    private List<AeaParFactor> children = new ArrayList<>();
}
