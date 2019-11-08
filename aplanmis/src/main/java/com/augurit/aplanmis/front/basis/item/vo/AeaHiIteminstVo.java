package com.augurit.aplanmis.front.basis.item.vo;

import com.augurit.aplanmis.common.domain.AeaHiIteminst;
import lombok.Data;

/**
 * @author:chendx
 * @date: 2019-10-31
 * @time: 14:09
 */
@Data
public class AeaHiIteminstVo extends AeaHiIteminst {
    /**
     * 实际审批用时
     */
    private Double realTime;

    /**
     * 事项状态对应的原因
     */
    private String reason;
}
