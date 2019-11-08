package com.augurit.aplanmis.admin.item.vo;

import com.augurit.aplanmis.common.domain.AeaItemBasic;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel("实施事项vo")
public class CarryOutItemVo {

    @ApiModelProperty(value = "事项版本id", dataType = "string")
    private String itemVerId;

    @ApiModelProperty(value = "事项id", dataType = "string")
    private String itemId;

    @ApiModelProperty(value = "事项名称", dataType = "string")
    private String itemName;

    @ApiModelProperty(value = "事项编码", dataType = "string")
    private String itemCode;

    @ApiModelProperty(value = "租户id", dataType = "string")
    private String rootOrgId;

    public static CarryOutItemVo build(AeaItemBasic aeaItemBasic) {
        CarryOutItemVo carryOutItemVo = new CarryOutItemVo();
        carryOutItemVo.setItemVerId(aeaItemBasic.getItemVerId());
        carryOutItemVo.setItemId(aeaItemBasic.getItemId());
        carryOutItemVo.setItemName(aeaItemBasic.getItemName());
        carryOutItemVo.setItemCode(aeaItemBasic.getItemCode());
        carryOutItemVo.setRootOrgId(aeaItemBasic.getRootOrgId());
        return carryOutItemVo;
    }
}
