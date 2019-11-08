package com.augurit.aplanmis.common.vo.diagram;

import lombok.Data;

import java.io.Serializable;

/**
 * BpmDiagramCellVo class
 *
 * @author jjt
 * @date 2019/07/25
 */
@Data
public class BpmnDiagramCell implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 节点Id
     */
    private String id;

    /**
     * 节点名称
     */
    private String auEleName;

    /**
     * 节点类型
     *
     * "type": "bpmn.Pool"  阶段
     *
     * "type": "bpmn.Activity" 事项
     *
     */
    private String type;

    /**
     * 父级节点id
     */
    private String parent;

    /**
     * 扩展给并行推进阶段：父级节点id, 用于事项节点
     */
    private String[] realParent;

    /**
     * 节点属性
     */
    private BpmnDiagramAttrs attrs;

}
