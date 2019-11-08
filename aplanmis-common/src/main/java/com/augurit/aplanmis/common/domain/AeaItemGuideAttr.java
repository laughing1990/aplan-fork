package com.augurit.aplanmis.common.domain;

import lombok.Data;

import java.io.Serializable;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Data
public class AeaItemGuideAttr implements Serializable {

    private static final long serialVersionUID = 1L;

    private String attachguid; // (住建)
    private String attachname; // ()
    private String filepath; // ()
    private String neiwangPath; // ()
    private String bizTableName; // ()
    private String bizTablePkId; // ()
    private String bizRecordId; // ()
    private String guideAttrId; // (主键)
    private String rootOrgId; // 根组织id
}
