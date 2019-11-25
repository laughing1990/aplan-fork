package com.augurit.aplanmis.data.exchange.vo;

import lombok.Data;

import java.io.Serializable;

/**
 * @author yinlf
 * @Date 2019/11/18
 */
@Data
public class EtlJobDetailLogVo implements Serializable {
    private static final long serialVersionUID = 724161545611L;
    private Long detailLogId;
    private Long jobLogId;
    private String tableName;
    private Long readNum;
    private Long writtenNum;
}
