package com.augurit.aplanmis.common.domain;
import lombok.Data;

import java.io.Serializable;

/**
* -模型
*/
@Data
public class AeaImItemExplain implements Serializable{
// ----------------------------------------------------- Properties

private static final long serialVersionUID = 1L;
    private String itemExplainId; // ()
    private String itemVerId; // (事项版本ID)
    private String serviceContent; // (服务内容)
    private String serviceResult; // (服务结果)
    private String timeLimitExplain; // (时限说明)
    private String serviceTimeLimit; // (服务时限要求)
    private String priceManagement; // (价格管理方式)
    private String slcj; // (设立层级)
    private String memo; // (备注)
    private String isFinancialFund; // (是否为财政资金（资金来源）：1 是，0 不是)
    private String isSocialFund; // (是否为社会资金（资金来源）：1 是，0 不是)
    /**
     * 单位要求信息ID
     */
    private String unitRequireId;
    private String rootOrgId;//根组织ID

}
