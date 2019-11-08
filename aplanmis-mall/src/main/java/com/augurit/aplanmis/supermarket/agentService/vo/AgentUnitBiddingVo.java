package com.augurit.aplanmis.supermarket.agentService.vo;

import com.augurit.agcloud.framework.ui.pager.EasyuiPageInfo;
import lombok.Data;

@Data
public class AgentUnitBiddingVo {

    /**
     * 中介机构中选记录列表
     */
    private EasyuiPageInfo biddingList;

    /**
     * 中介机构综合评价
     */
    private String compEvaluation;

}
