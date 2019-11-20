package com.augurit.aplanmis.mall.userCenter.vo;

import com.augurit.aplanmis.common.domain.AeaHiIteminst;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author:chendx
 * @date: 2019-10-31
 * @time: 14:09
 */
@EqualsAndHashCode(callSuper = true)
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
