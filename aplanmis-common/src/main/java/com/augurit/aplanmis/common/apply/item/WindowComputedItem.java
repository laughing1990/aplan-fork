package com.augurit.aplanmis.common.apply.item;

import com.augurit.aplanmis.common.domain.AeaItemBasic;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * 换算后的事项
 */
@Getter
@Setter
@ApiModel(value = "窗口申报事项")
public class WindowComputedItem extends ComputedItem {

    @ApiModelProperty(value = "阶段情形id")
    private String parStateId;

    @ApiModelProperty(value = "办理范围", dataType = "string", notes = "0表示全市通办，1表示属地办理")
    private String regionRange;

    @ApiModelProperty(value = "实施事项换算方式", dataType = "string", notes = " 0:按照审批行政区划和属地行政区划换算, 1: 仅按照审批行政区划换算")
    private String itemExchangeWay;

    protected void mergeCommonField(AeaItemBasic origin) {
        super.mergeCommonField(origin);
        this.setItemExchangeWay(origin.getItemExchangeWay());
    }

}

