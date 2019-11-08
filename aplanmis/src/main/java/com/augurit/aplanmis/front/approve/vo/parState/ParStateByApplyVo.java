package com.augurit.aplanmis.front.approve.vo.parState;

import com.augurit.aplanmis.common.domain.AeaParState;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/***
 * @description 并联申报已选择情形
 * @author mohaoqi
 * @date 2019/7/26 0026
 ***/
@Data
@ApiModel("并联申报已选择情形")
public class ParStateByApplyVo {

    @ApiModelProperty(value = "答案", required = true, dataType = "aeaParStateVo")
    private AeaParStateVo answerstate;

    @ApiModelProperty(value = "问题", required = true, dataType = "aeaParStateVo")
    private AeaParStateVo questionstate;


    public static List<ParStateByApplyVo> toParStateByApplyVo(List<Map<String, Object>> parStateByApplys) {
        List<ParStateByApplyVo> parStateByApplyVos = new ArrayList<ParStateByApplyVo>();
        for (Map<String, Object> map : parStateByApplys) {
            ParStateByApplyVo parStateByApplyVo = new ParStateByApplyVo();
            for (Map.Entry<String, Object> entry : map.entrySet()) {
                AeaParStateVo aeaParStateVo = getAeaParStateVo((AeaParState) entry.getValue());
                if ("answer".equals(entry.getKey())) {
                    parStateByApplyVo.setAnswerstate(aeaParStateVo);
                } else if ("question".equals(entry.getKey())) {
                    parStateByApplyVo.setAnswerstate(aeaParStateVo);
                }
            }
            parStateByApplyVos.add(parStateByApplyVo);
        }
        return parStateByApplyVos;
    }

    public static List<ParStateByApplyVo> toParStateByApplyListVo(List<AeaParState> parStateByApplys) {
        List<ParStateByApplyVo> list = new ArrayList<ParStateByApplyVo>();
        for (AeaParState parState : parStateByApplys) {
            ParStateByApplyVo parStateByApplyVo = new ParStateByApplyVo();
            AeaParStateVo aeaParStateVo = toAeaParStateVo(parState);
            parStateByApplyVo.setQuestionstate(aeaParStateVo);
            parStateByApplyVo.setAnswerstate(aeaParStateVo.getAnswerStates().get(0));
            list.add(parStateByApplyVo);
        }
        return list;
    }

    private static AeaParStateVo toAeaParStateVo(AeaParState aeaParState) {
        AeaParStateVo aeaParStateVo = new AeaParStateVo();
        aeaParStateVo.setParStateId(aeaParState.getParStateId());
        aeaParStateVo.setStageId(aeaParState.getStageId());
        aeaParStateVo.setStateName(aeaParState.getStateName());
        aeaParStateVo.setUseEl(aeaParState.getUseEl());
        aeaParStateVo.setStateEl(aeaParState.getStateEl());
        aeaParStateVo.setIsQuestion(aeaParState.getIsQuestion());
        aeaParStateVo.setAnswerType(aeaParState.getAnswerType());
        aeaParStateVo.setMustAnswer(aeaParState.getMustAnswer());
        aeaParStateVo.setParentStateId(aeaParState.getParentStateId());
        aeaParStateVo.setStateSeq(aeaParState.getStateSeq());
        aeaParStateVo.setSortNo(aeaParState.getSortNo());
        aeaParStateVo.setIsActive(aeaParState.getIsActive());
        aeaParStateVo.setStateMemo(aeaParState.getStateMemo());
        aeaParStateVo.setCreater(aeaParState.getCreater());
        aeaParStateVo.setCreateTime(aeaParState.getCreateTime());
        aeaParStateVo.setModifier(aeaParState.getModifier());
        aeaParStateVo.setModifyTime(aeaParState.getModifyTime());
        aeaParStateVo.setIsDeleted(aeaParState.getIsDeleted());
        aeaParStateVo.setIsQuestionOri(aeaParState.getIsQuestionOri());
        List<AeaParStateVo> apsvs = new ArrayList<AeaParStateVo>();
        for(AeaParState aps:aeaParState.getAnswerStates()){
            apsvs.add(getAeaParStateVo(aps));
        }
        aeaParStateVo.setAnswerStates(apsvs);
        return aeaParStateVo;
    }

    private static AeaParStateVo getAeaParStateVo(AeaParState aeaParState) {
        AeaParStateVo aeaParStateVo = new AeaParStateVo();
        aeaParStateVo.setParStateId(aeaParState.getParStateId());
        aeaParStateVo.setStageId(aeaParState.getStageId());
        aeaParStateVo.setStateName(aeaParState.getStateName());
        aeaParStateVo.setUseEl(aeaParState.getUseEl());
        aeaParStateVo.setStateEl(aeaParState.getStateEl());
        aeaParStateVo.setIsQuestion(aeaParState.getIsQuestion());
        aeaParStateVo.setAnswerType(aeaParState.getAnswerType());
        aeaParStateVo.setMustAnswer(aeaParState.getMustAnswer());
        aeaParStateVo.setParentStateId(aeaParState.getParentStateId());
        aeaParStateVo.setStateSeq(aeaParState.getStateSeq());
        aeaParStateVo.setSortNo(aeaParState.getSortNo());
        aeaParStateVo.setIsActive(aeaParState.getIsActive());
        aeaParStateVo.setStateMemo(aeaParState.getStateMemo());
        aeaParStateVo.setCreater(aeaParState.getCreater());
        aeaParStateVo.setCreateTime(aeaParState.getCreateTime());
        aeaParStateVo.setModifier(aeaParState.getModifier());
        aeaParStateVo.setModifyTime(aeaParState.getModifyTime());
        aeaParStateVo.setIsDeleted(aeaParState.getIsDeleted());
        aeaParStateVo.setIsQuestionOri(aeaParState.getIsQuestionOri());
        List<AeaParStateVo> apsvs = new ArrayList<AeaParStateVo>();
        /*for(AeaParState aps:aeaParState.getAnswerStates()){
            apsvs.add(getAeaParStateVo(aps));
        }
        aeaParStateVo.setAnswerStates(apsvs);*/
        return aeaParStateVo;
    }

}
