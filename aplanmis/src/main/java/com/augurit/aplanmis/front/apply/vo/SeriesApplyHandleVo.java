package com.augurit.aplanmis.front.apply.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
@ApiModel("问题情形和材料")
public class SeriesApplyHandleVo {
    // 问题情形
    private List<SeriesApplyHandleVo.QuestionStateVo> questionStates;
    // 材料
    private List<SeriesApplyHandleVo.MatVo> stateMats;

    public SeriesApplyHandleVo() {
        questionStates = new ArrayList<>();
        stateMats = new ArrayList<>();
    }

    /**
     * 问题情形
     */
    @Data
    @ApiModel("问题情形")
    public static class QuestionStateVo {
        // 事项情形id
        @ApiModelProperty(value = "事项情形id")
        private String itemStateId;
        // 事项版本id
        @ApiModelProperty(value = "事项版本id")
        private String itemVerId;
        // 情形名称
        @ApiModelProperty(value = "情形名称")
        private String stateName;
        // 是否启用EL表达式，0表示不启用，1表示启用。默认为0
        @ApiModelProperty(value = "是否启用EL表达式")
        private String useEl;
        // 条件表达式
        @ApiModelProperty(value = "条件表达式")
        private String stateEl;
        // 问题的答案类型，y表示是否选择，s表示单选答案，m表示多选答案
        @ApiModelProperty(value = "问题的答案类型")
        private String answerType;
        // 是否必须回答，0表示可选回答，1表示必须回答 IS_QUESTION=1
        @ApiModelProperty(value = "是否必须回答")
        private String mustAnswer;
        // 父情形id
        @ApiModelProperty(value = "父情形id")
        private String parentStateId;
        // 排列顺序号
        @ApiModelProperty(value = "排列顺序号")
        private Long sortNo;
        // 备注说明
        @ApiModelProperty(value = "备注说明")
        private String stateMemo;
        // 父问题情形id
        @ApiModelProperty(value = "父问题情形id")
        private String parentQuestionStateId;
        // 材料
        private List<SeriesApplyHandleVo.MatVo> stateMats;
        // 答案情形
        private List<SeriesApplyHandleVo.AnswerStateVo> answerStates;
        //选择的子情形ID
        private String selectAnswerId="";
    }

    /**
     * 答案情形
     */
    @Data
    @ApiModel("答案情形")
    public static class AnswerStateVo {
        // 事项情形id
        @ApiModelProperty(value = "事项情形id")
        private String itemStateId;
        // 事项版本id
        @ApiModelProperty(value = "事项版本id")
        private String itemVerId;
        // 情形名称
        @ApiModelProperty(value = "情形名称")
        private String stateName;
        // 是否启用EL表达式，0表示不启用，1表示启用。默认为0
        @ApiModelProperty(value = "是否启用EL表达式")
        private String useEl;
        // 条件表达式
        @ApiModelProperty(value = "条件表达式")
        private String stateEl;
        // 父情形id
        @ApiModelProperty(value = "父情形id")
        private String parentStateId;
        // 排列顺序号
        @ApiModelProperty(value = "排列顺序号")
        private Long sortNo;
        // 备注说明
        @ApiModelProperty(value = "备注说明")
        private String stateMemo;
        // 材料
        private List<SeriesApplyHandleVo.MatVo> stateMats;

        public AnswerStateVo() {
            stateMats = new ArrayList<>();
        }
    }

    /**
     * 绑定的材料
     */
    @Data
    @ApiModel("绑定的材料")
    public static class MatVo {
        // 关联的输入id
        @ApiModelProperty(value = "关联的输入id")
        private String inId;
        // 关联的情形id
        @ApiModelProperty(value = "关联的情形id")
        private String itemStateId;
        // 材料id
        @ApiModelProperty(value = "材料id")
        private String matId;
        // 材料编号
        @ApiModelProperty(value = "材料编号")
        private String matCode;
        // 材料名称
        @ApiModelProperty(value = "材料名称")
        private String matName;
        // 原件数
        @ApiModelProperty(value = "原件数")
        private Long duePaperCount;
        // 原件验收 0 无 1验 2收
        @ApiModelProperty(value = "原件验收")
        private String duePaperType;
        // 复印件数
        @ApiModelProperty(value = "复印件数")
        private Long dueCopyCount;
        // 复印件验收类型 0 无 1验 2收
        @ApiModelProperty(value = "复印件验收类型")
        private String dueCopyType;
        // 材料来源
        @ApiModelProperty(value = "材料来源")
        private String matFrom;
        // 材料来源部门
        @ApiModelProperty(value = "材料来源部门")
        private String matFromDept;
        // 所属材料类别ID
        @ApiModelProperty(value = "所属材料类别ID")
        private String matTypeId;
        // 样本文档
        @ApiModelProperty(value = "样本文档")
        private String sampleDoc;
        // 模板文档
        @ApiModelProperty(value = "模板文档")
        private String templateDoc;
        // 材料要求
        @ApiModelProperty(value = "材料要求")
        private String matRequire;
        // 材料依据
        @ApiModelProperty(value = "材料依据")
        private String matBasis;
        // 纸质材料是否必需，0表示容缺，1表示必须
        @ApiModelProperty(value = "纸质材料是否必需")
        private String paperIsRequire;
        // 电子材料是否必需，0表示容缺，1表示必须
        @ApiModelProperty(value = "电子材料是否必需")
        private String attIsRequire;
        // 审查要点
        @ApiModelProperty(value = "审查要点")
        private String reviewKeyPoints;
        // 审查样本
        @ApiModelProperty(value = "审查样本")
        private String reviewSampleDoc;
        // 审查依据
        @ApiModelProperty(value = "审查依据")
        private String reviewBasis;
        // 排序号
        @ApiModelProperty(value = "排序号")
        private Long sortNo;
        // 备注说明
        @ApiModelProperty(value = "备注说明")
        private String matMemo;

        @ApiModelProperty(value = "是否支持容缺 0 否 1是")
        private String zcqy;
    }
}
