package com.augurit.aplanmis.common.vo;

import com.augurit.agcloud.bpm.common.domain.ActTplAppTrigger;

public class ActTplAppTriggerAdminVo extends ActTplAppTrigger {
    private String NodeName;
    private String procName;
    private String eventName;
    private String itemName;

    public String getNodeName() {
        return NodeName;
    }

    public void setNodeName(String nodeName) {
        NodeName = nodeName;
    }

    public String getProcName() {
        return procName;
    }

    public void setProcName(String procName) {
        this.procName = procName;
    }

    public String getEventName() {
        return eventName;
    }

    public void setEventName(String eventName) {
        this.eventName = eventName;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }
}
