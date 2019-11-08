package com.augurit.aplanmis.front.approve.vo.parState;

import com.augurit.aplanmis.common.domain.AeaParState;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.beans.BeanUtils;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.List;

/***
 * @description 主题情形定义表-模型
 * @author mohaoqi
 * @date 2019/7/26 0026
 ***/
@Data
@ApiModel("主题情形定义表-模型")
public class AeaParStateVo {

    @ApiModelProperty(value = "标准阶段定义ID", required = true, dataType = "string")
    private String parStateId;

    @ApiModelProperty(value = "阶段定义ID", required = true, dataType = "string")
    private String stageId;

    @ApiModelProperty(value = "条件名称", required = true, dataType = "string")
    private String stateName;

    @ApiModelProperty(value = "是否启用EL表达式，0表示不启用，1表示启用。默认为0", required = true, dataType = "string", allowableValues = "0,1")
    private String useEl;

    @ApiModelProperty(value = "条件表达式", required = true, dataType = "string")
    private String stateEl;

    @ApiModelProperty(value = "是否问题，0表示答案，1表示问题，2表示事项情形", required = true, dataType = "string", allowableValues = "0,1,2")
    private String isQuestion;

    @ApiModelProperty(value = "问题的答案类型，y表示是否选择，s表示单选答案，m表示多选答案", required = true,
            dataType = "string", allowableValues = "y,s,m")
    private String answerType;

    @ApiModelProperty(value = "是否必须回答，0表示可选回答，1表示必须回答【IS_QUESTION=1】", required = true,
            dataType = "string", allowableValues = "0,1")
    private String mustAnswer;

    @ApiModelProperty(value = "父ID", required = true, dataType = "string")
    private String parentStateId;

    @ApiModelProperty(value = "序列", required = true, dataType = "string")
    private String stateSeq;

    @ApiModelProperty(value = "排列顺序号", required = true, dataType = "long")
    @Size(max = 38)
    private Long sortNo;

    @ApiModelProperty(value = "是否启用，0表示禁用，1表示启用", required = true, dataType = "string", allowableValues = "0,1")
    private String isActive;

    @ApiModelProperty(value = "备注说明", required = true, dataType = "string")
    private String stateMemo;

    @ApiModelProperty(value = "创建人", required = true, dataType = "string")
    private String creater;

    @ApiModelProperty(value = "创建时间", required = true, dataType = "date")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private java.util.Date createTime;

    @ApiModelProperty(value = "修改人", required = true, dataType = "string")
    private String modifier;

    @ApiModelProperty(value = "修改时间", required = true, dataType = "date")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private java.util.Date modifyTime;

    @ApiModelProperty(value = "是否删除，0表示未删除，1表示已删除", required = true, dataType = "string")
    private String isDeleted;

    @ApiModelProperty(value = "存储原IS_QUESTION数据", required = true, dataType = "string")
    private String isQuestionOri;

    @ApiModelProperty(value = "问题答案列表", required = true, dataType = "java.utl.List")
    List<AeaParStateVo> answerStates;

    List<AeaParStateVo> children;

    private Boolean hasChildren;


    public static List<AeaParStateVo> toListAeaParStateVo(List<AeaParState> stateList) {
        List<AeaParStateVo> list = new ArrayList<>();
        for (AeaParState parState : stateList) {
            AeaParStateVo vo = new AeaParStateVo();
            BeanUtils.copyProperties(parState, vo);
            List<AeaParState> answerStates = parState.getAnswerStates();
            vo.setAnswerStates(toAnsAeaParStateVo(answerStates));
            vo.setChildren(toAnsAeaParStateVo(answerStates));
            vo.setHasChildren(true);
            list.add(vo);
        }
        return list;
    }

    public static List<AeaParStateVo> toAnsAeaParStateVo(List<AeaParState> stateList) {
        List<AeaParStateVo> list = new ArrayList<>();
        for (AeaParState parState : stateList) {
            AeaParStateVo vo = new AeaParStateVo();
            BeanUtils.copyProperties(parState, vo);
            list.add(vo);
        }
        return list;
    }

}
