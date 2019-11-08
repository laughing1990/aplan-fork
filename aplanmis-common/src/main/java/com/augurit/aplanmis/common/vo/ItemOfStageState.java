package com.augurit.aplanmis.common.vo;

import lombok.Data;

import java.io.Serializable;

/**
 * @author jjt
 * @date 2019/4/16
 *
 * 情形与事项关联定义表
 */
@Data
public class ItemOfStageState implements Serializable {

    private static final long serialVersionUID = 1L;

    private String stateItemId; // (ID)

    private String parStateId;
    private String stageItemId;
    private String itemVerId;
    private String itemName;
}
