package com.augurit.aplanmis.common.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author jjt
 * @date 2019/4/19
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class StageMatDto extends InoutMatAddDto {

    /**
     * 阶段id
     */
    private String stageId;

    /**
     * 情形id
     */
    private String parStateId;
}
