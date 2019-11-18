package com.augurit.aplanmis.data.exchange.domain.aplanmis;

import com.augurit.aplanmis.common.domain.AeaLogItemStateHist;
import lombok.Data;

/**
 * @author yinlf
 * @Date 2019/11/8
 */
@Data
public class ItemStateLog extends AeaLogItemStateHist {

    /**
     * 项目代码
     */
    private String gcdm;
    /**
     * 流程时限ID
     */
    private String procinstId;
}
