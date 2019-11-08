package com.augurit.aplanmis.common.domain;

import lombok.Data;

import java.io.Serializable;

/**
 * 收费项目信息-模型
 */
@Data
public class AeaItemGuideCharges implements Serializable {

    private static final long serialVersionUID = 1L;

    private String rowguid; // (主键)
    private String itemVerId; // (事项版本ID)
    private String feeName; // (收费项目名称)
    private String feeOrg; // (收费主体)
    private String feeType; // (收费性质 1 行政事业性收费 2 经营服务性收费)
    private String feeTypeText; // (收费性质文本)
    private String bylaw; // (收费依据)
    private String isDesc; // (是否允许减免 1是 0否)
    private String isDescText; // (是否允许减免文本)
    private String descExplain; // (收费减免情形)
    private String remark; // (收费项目备注)
    private Long ordernum; // (收费项目排序)
    private String feeStand; // ()
    private String descLaw; // ()
    private String rootOrgId;
    /**
     * 扩展字段
     */
    private String keyword;
}
