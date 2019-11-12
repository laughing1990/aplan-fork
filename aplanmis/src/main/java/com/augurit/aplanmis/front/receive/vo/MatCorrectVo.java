package com.augurit.aplanmis.front.receive.vo;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class MatCorrectVo extends ReceiveBaseVo {
    private String chargeOrgName;
    private String receiveOrgName;
}
