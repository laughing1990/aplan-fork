package com.augurit.aplanmis.common.vo.analyse;

import lombok.Data;

import java.io.Serializable;

@Data
public class ApplyPorjVo implements Serializable {
    private String applyinstId;
    private String projInfoId;
    private String isApprove;
}
