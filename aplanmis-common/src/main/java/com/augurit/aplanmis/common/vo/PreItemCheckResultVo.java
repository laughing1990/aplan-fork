package com.augurit.aplanmis.common.vo;

import com.augurit.aplanmis.common.constants.ItemStatus;
import com.augurit.aplanmis.common.domain.AeaHiIteminst;
import com.augurit.aplanmis.common.domain.AeaItemFrontItem;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Data
@ApiModel("前置事项检查结果")
public class PreItemCheckResultVo {

    @ApiModelProperty(value = "前置事项")
    private List<AeaItemFrontItem> preItems;

    @ApiModelProperty(value = "各前置事项状态", notes = "itemVerId 对应 事项实例 itemState")
    private Map<String, String> preItemStates;

    public boolean isPassed() {
        if (preItems == null) {
            return true;
        } else if (preItemStates == null) {
            return false;
        }
        boolean result = true;
        // 所有前置事项审批通过才允许单项申报
        for (Map.Entry<String, String> state : preItemStates.entrySet()) {
            if (!ItemStatus.isEnd(state.getValue())) {
                result = false;
            }
        }
        return result;
    }

    /**
     * 构建事项实例的审批结果信息
     *
     * @param aeaHiIteminstList 事项实例列表
     */
    public void buildPreItemStates(List<AeaHiIteminst> aeaHiIteminstList) {
        if (aeaHiIteminstList != null && aeaHiIteminstList.size() > 0) {
            if (preItemStates == null) {
                preItemStates = new HashMap<>(aeaHiIteminstList.size());
            }
            aeaHiIteminstList.forEach(iteminst -> preItemStates.put(iteminst.getItemVerId(), iteminst.getIteminstState()));
        }
    }
}

