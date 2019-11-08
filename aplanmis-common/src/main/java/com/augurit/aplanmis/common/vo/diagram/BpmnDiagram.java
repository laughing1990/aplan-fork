package com.augurit.aplanmis.common.vo.diagram;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * BpmDiagramVo class
 *
 * @author jjt
 * @date 2019/07/25
 *
 */
@Data
public class BpmnDiagram implements Serializable {

    private static final long serialVersionUID = 1L;

    private String type;

    private List<BpmnDiagramCell> cells;
}
