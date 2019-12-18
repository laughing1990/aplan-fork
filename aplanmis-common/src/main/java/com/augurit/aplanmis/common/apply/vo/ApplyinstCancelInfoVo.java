package com.augurit.aplanmis.common.apply.vo;

import lombok.Data;

import java.io.Serializable;

@Data
public class ApplyinstCancelInfoVo implements Serializable {

    private String applyinstId;
    private String applyReason;
    private String applyUserId;
    private String attId;
    private String applyUserName;
    private String applyUserIdnumber;
    private String applyUserPhone;

}
