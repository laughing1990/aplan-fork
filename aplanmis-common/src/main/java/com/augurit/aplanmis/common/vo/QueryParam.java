package com.augurit.aplanmis.common.vo;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class QueryParam implements Serializable {
    private String ids ;

    private String status;//状态
    private String date;//时间
    private String applyinstSource;//申报来源
}
