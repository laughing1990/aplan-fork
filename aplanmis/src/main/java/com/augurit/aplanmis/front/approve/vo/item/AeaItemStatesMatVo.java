package com.augurit.aplanmis.front.approve.vo.item;

import com.augurit.aplanmis.common.domain.AeaItemState;
import com.augurit.aplanmis.front.approve.vo.StatesMatVo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/***
 * @description
 * @author mohaoqi
 * @date 2019/7/26 0026
 ***/
@Data
@ApiModel("单项申报已选择情形")
public class AeaItemStatesMatVo {
    @ApiModelProperty(value = "问题", required = true, dataType="aeaItemStateVo")
    private AeaItemStateVo question;
    @ApiModelProperty(value = "答案", required = true, dataType="java.utl.List")
    private List<AeaItemStateVo> answers;
    @ApiModelProperty(value = "已选id", required = true, dataType="java.utl.List")
    private List<String> selectId;

    public static List<AeaItemStatesMatVo> toAeaItemStatesMatVo(List<StatesMatVo> statesMatVos){
        List<AeaItemStatesMatVo> aeaItemStatesMatVos = new ArrayList<>();
        for(StatesMatVo statesMatVo:statesMatVos){
            aeaItemStatesMatVos.add(getAeaItemStatesMatVo(statesMatVo));
        }
        return aeaItemStatesMatVos;
    }

    private static AeaItemStatesMatVo getAeaItemStatesMatVo(StatesMatVo statesMatVo){
        AeaItemStatesMatVo aeaItemStatesMatVo = new AeaItemStatesMatVo();
        aeaItemStatesMatVo.setQuestion(getAeaItemStateVo(statesMatVo.getQuestion()));
        List<AeaItemStateVo> answers =  new ArrayList<AeaItemStateVo>();
        for(AeaItemState aeaItemState:statesMatVo.getAnswers()){
            AeaItemStateVo aeaItemStateVo=getAeaItemStateVo(aeaItemState);
            answers.add(aeaItemStateVo);
        }
        aeaItemStatesMatVo.setAnswers(answers);
        aeaItemStatesMatVo.setSelectId(statesMatVo.getSelectId());
        return aeaItemStatesMatVo;
    }

    private static AeaItemStateVo getAeaItemStateVo(AeaItemState aeaItemState){
        AeaItemStateVo aeaItemStateVo = new AeaItemStateVo();
        aeaItemStateVo.setItemStateId(aeaItemState.getItemStateId());
        aeaItemStateVo.setItemId(aeaItemState.getItemId());
        aeaItemStateVo.setItemVerId(aeaItemState.getItemVerId());
        aeaItemStateVo.setStateVerId(aeaItemState.getStateVerId());
        aeaItemStateVo.setStateName(aeaItemState.getStateName());
        aeaItemStateVo.setStateEl(aeaItemState.getStateEl());
        aeaItemStateVo.setSortNo(aeaItemState.getSortNo());
        aeaItemStateVo.setStateMemo(aeaItemState.getStateMemo());
        aeaItemStateVo.setIsActive(aeaItemState.getIsActive());
        aeaItemStateVo.setCreater(aeaItemState.getCreater());
        aeaItemStateVo.setCreateTime(aeaItemState.getCreateTime());
        aeaItemStateVo.setModifier(aeaItemState.getModifier());
        aeaItemStateVo.setModifyTime(aeaItemState.getModifyTime());
        aeaItemStateVo.setUseEl(aeaItemState.getUseEl());
        aeaItemStateVo.setParentStateId(aeaItemState.getParentStateId());
        aeaItemStateVo.setStateSeq(aeaItemState.getStateSeq());
        aeaItemStateVo.setIsQuestion(aeaItemState.getIsQuestion());
        aeaItemStateVo.setAnswerType(aeaItemState.getAnswerType());
        aeaItemStateVo.setMustAnswer(aeaItemState.getMustAnswer());
        aeaItemStateVo.setIsDeleted(aeaItemState.getIsDeleted());
        aeaItemStateVo.setIsProcStartCond(aeaItemState.getIsProcStartCond());

        List<AeaItemStateVo> aisvs = new ArrayList<AeaItemStateVo>();
        for(AeaItemState ais:aeaItemState.getAnswerStates()){
            aisvs.add(getAeaItemStateVo(ais));
        }
        aeaItemStateVo.setAnswerStates(aisvs);
        return aeaItemStateVo;
    }

}
