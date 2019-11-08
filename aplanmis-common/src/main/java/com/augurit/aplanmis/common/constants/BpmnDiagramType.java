package com.augurit.aplanmis.common.constants;

import com.augurit.aplanmis.common.handler.BaseEnum;
import lombok.Getter;

@Getter
public enum BpmnDiagramType implements BaseEnum<BpmnDiagramType, String> {

    REALSTAGE("并联审批真实阶段节点", "bpmn.HPool"),
    VIRTSTAGE("并行推进虚拟阶段节点", "bpmn.Pool"),
    AUXSTAGE("辅线阶段节点", "bpmn.SPool"),
    ITEM("事项节点", "bpmn.Activity");

    private String name;
    private String value;

    BpmnDiagramType(String name, String value) {

        this.name = name;
        this.value = value;
    }
}
