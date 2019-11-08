package com.augurit.aplanmis.common.domain;

import lombok.Data;

import java.io.Serializable;

/**
 * 情形关联材料-模型
 */
@Data
public class AeaItemGuideMatconditions implements Serializable {

    private static final long serialVersionUID = 1L;

    private String conditionGuid; // (情形标识)
    private String itemVerId; // (事项版本id)
    private String materialGuid; // (材料标识)
    private String materialType; // (材料形式)
    private String materialTypeText; // (材料形式文本)
    private String pageCopyNum; // (材料复印件份数)
    private String pageNum; // (材料原件份数)
    private String rowguid; // (记录唯一标识)
    private String rootOrgId;
}
