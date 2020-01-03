package com.augurit.aplanmis.common.domain;

import lombok.Data;

import java.io.Serializable;
import java.util.Map;

/**
 * 竣工验收阶段 辅助对象
 */
@Data
public class ProjectCheckForm implements Serializable {
    //序列化id
    private static final long serialVersionUID = 4125096758372084309L;

    private Map<String,Boolean> itemStates;//事项审查决定结果集合【key：唯一分类标识符，value为：事项审查决定结果（审查通过为true,审查不通过为false）】
    private Map<String,Boolean> matchFacilities;//公共配套设施集合；

    private Boolean allItemState;//所有审查事项的审查通过时为true,否则为false
//    private Boolean allMatchState;//所有的工改配套设施审查通过时为true，否则为false

    /*public Boolean getAllItemState() {
        if(itemStates!=null&&itemStates.size()>0){
            int count = itemStates.size();
            Set<String> keys = itemStates.keySet();
            for(String key:keys){
                Boolean boo = itemStates.get(key);
                if(boo!=null&&boo){
                    count--;
                }
            }

            if(count==0)
                return true;
        }
        return false;
    }

    public void setAllItemState(Boolean allItemState) {
        this.allItemState = allItemState;
    }

    public Boolean getAllMatchState() {
        if(matchFacilities!=null&&matchFacilities.size()>0){
            int count = matchFacilities.size();
            Set<String> keys = matchFacilities.keySet();
            for(String key:keys){
                Boolean boo = matchFacilities.get(key);
                if(boo!=null&&boo){
                    count--;
                }
            }

            if(count==0)
                return true;
        }
        return false;
    }

    public void setAllMatchState(Boolean allMatchState) {
        this.allMatchState = allMatchState;
    }*/
}
