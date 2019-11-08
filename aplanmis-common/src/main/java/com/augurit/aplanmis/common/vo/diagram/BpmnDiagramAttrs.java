package com.augurit.aplanmis.common.vo.diagram;

import com.augurit.aplanmis.common.domain.AeaParStage;
import com.augurit.aplanmis.common.domain.AeaParStageItem;
import lombok.Data;

import java.io.Serializable;

/**
 * BpmDiagramAttrs class
 *
 * @author jjt
 * @date 2019/07/25
 */
@Data
public class BpmnDiagramAttrs implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 阶段节点属性
     */
    private AeaParStage stage;

    /**
     * 事项节点属性
     */
    private AeaParStageItem item;
}
