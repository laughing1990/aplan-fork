package com.augurit.aplanmis.common.apply.vo;

import lombok.Data;

import java.io.Serializable;

@Data
public class AeaHiItemCancelVo implements Serializable {

    private String applyinstId;
    private String iteminstCancelId;
    private String approvalOpinion;
    private String cancelState;
    private String attId;
}
