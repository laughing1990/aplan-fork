package com.augurit.aplanmis.front.approve.vo.item;

import com.augurit.aplanmis.common.domain.AeaItemState;
import com.augurit.aplanmis.common.domain.AeaParState;
import com.augurit.aplanmis.front.approve.vo.parState.AeaParStateVo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.beans.BeanUtils;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.List;

/***
 * @description 事项情形定义表-模型
 * @author mohaoqi
 * @date 2019/7/26 0026
 ***/
@Data
@ApiModel("事项情形定义表-模型")
public class AeaItemStateVo {

    @ApiModelProperty(value = "事项状态ID", required = true, dataType = "string")
    private String itemStateId;

    @ApiModelProperty(value = "主键", required = true, dataType = "string")
    private String itemId;

    @ApiModelProperty(value = "事项定义ID", required = true, dataType = "string")
    private String itemVerId;

    @ApiModelProperty(value = "情形版本ID", required = true, dataType = "string")
    private String stateVerId;

    @ApiModelProperty(value = "情形名称", required = true, dataType = "string")
    private String stateName;

    @ApiModelProperty(value = "条件表达式", required = true, dataType = "string")
    private String stateEl;

    @Size(max = 38)
    @ApiModelProperty(value = "排列顺序号", required = true, dataType = "long")
    private Long sortNo;

    @ApiModelProperty(value = "备注说明", required = true, dataType = "string")
    private String stateMemo;

    @ApiModelProperty(value = "是否启用，0表示禁用，1表示启用", required = true, dataType = "string", allowableValues = "0,1")
    private String isActive;

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

    @ApiModelProperty(value = "是否启用EL表达式，0表示不启用，1表示启用。默认为0", required = true, dataType = "string", allowableValues = "0,1")
    private String useEl;

    @ApiModelProperty(value = "父ID(1表示父节点为事项)", required = true, dataType = "string")
    private String parentStateId;

    @ApiModelProperty(value = "序列", required = true, dataType = "string")
    private String stateSeq;

    @ApiModelProperty(value = "是否问题，0表示答案，1表示问题", required = true, dataType = "string", allowableValues = "0,1")
    private String isQuestion;

    @ApiModelProperty(value = "问题的答案类型，y表示是否选择，s表示单选答案，m表示多选答案", required = true, dataType = "string", allowableValues = "y,s,m")
    private String answerType;

    @ApiModelProperty(value = "是否必须回答，0表示可选回答，1表示必须回答【IS_QUESTION=1】", required = true, dataType = "string", allowableValues = "0,1")
    private String mustAnswer;

    @ApiModelProperty(value = "是否删除，0表示未删除，1表示已删除", required = true, dataType = "string", allowableValues = "0,1")
    private String isDeleted;

    @ApiModelProperty(value = "启动条件", required = true, dataType = "string")
    private String isProcStartCond;

    @ApiModelProperty(value = "情形答案列表", required = true, dataType = "java.utl.List")
    List<AeaItemStateVo> answerStates;


    public static List<AeaItemStateVo> toListAeaParStateVo(List<AeaItemState> stateList) {
        List<AeaItemStateVo> list = new ArrayList<>();
        for (AeaItemState itemState : stateList) {
            AeaItemStateVo vo = new AeaItemStateVo();
            BeanUtils.copyProperties(itemState, vo);
            List<AeaItemState> answerStates = itemState.getAnswerStates();
            vo.setAnswerStates(toAnsAeaParStateVo(answerStates));
            list.add(vo);
        }
        return list;
    }

    public static List<AeaItemStateVo> toAnsAeaParStateVo(List<AeaItemState> stateList) {
        List<AeaItemStateVo> list = new ArrayList<>();
        for (AeaItemState parState : stateList) {
            AeaItemStateVo vo = new AeaItemStateVo();
            BeanUtils.copyProperties(parState, vo);
            list.add(vo);
        }
        return list;
    }
}
