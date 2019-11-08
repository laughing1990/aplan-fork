package com.augurit.aplanmis.common.event.vo;

import lombok.Data;

// 事项实例事件vo
@Data
public class IteminstEventVo {

    // 事项实例id
    private String iteminstId;

    // 实行实例状态
    private String iteminstState;

    // 是否抛出报错信息
    private boolean showException = false;

    public IteminstEventVo(String iteminstId, String iteminstState) {
        this.iteminstId = iteminstId;
        this.iteminstState = iteminstState;
    }

    public IteminstEventVo(String iteminstId, String iteminstState,boolean showException) {
        this(iteminstId,iteminstState);
        this.showException = showException;
    }
}
