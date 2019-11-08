package com.augurit.aplanmis.common.domain;

import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;

/**
 * -中介服务和资质关联表
 */
@Data
public class AeaImServiceQual implements Serializable{
    private static final long serialVersionUID = 1L;
    private String serviceQualId; // ()
    private String serviceId; // (服务ID)
    private String qualId; // (资质ID)
    private String isDelete; // ()
    private String creater; // ()
    @DateTimeFormat(pattern="yyyy-MM-dd")
    private java.util.Date createTime; //

    //非表字段
    private String[] serviceIds;
    private String qualName;
}
