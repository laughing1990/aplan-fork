package com.augurit.aplanmis.supermarket.agentService.vo;

import com.augurit.aplanmis.common.domain.AgentItemDetail;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

@Data
@EqualsAndHashCode(callSuper = true)
public class AgentServiceItemDetailVo extends AgentItemDetail {
    /**
     * 中介事项设立依据列表
     */
    private List<AeaItemServiceBasicVo> aeaItemServiceBasicList;
    /**
     * 中介事项对应的行政事项列表
     */
    private List<ItemDetailVo> aeaItemBasicList;

}
