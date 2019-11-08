package com.augurit.aplanmis.mall.cloud.vo;

import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

@Data
public class CloudAttDir {
    private static final long serialVersionUID = 1L;
    private String dirId;
    private String orgId;
    private String isRoot;
    private String dirCode;
    private String dirName;
    private String parentId;
    private String dirSeq;
    private String dirMemo;
    private String dirLevel;
    private String creater;
    @DateTimeFormat(
            pattern = "yyyy-MM-dd"
    )
    private Date createTime;
    private String modifier;
    @DateTimeFormat(
            pattern = "yyyy-MM-dd"
    )
    private Date modifyTime;
    private String dirPrivilege;
    private String dirNameSeq;
    private String storeType;
    private Integer dirSeqNum;
    private AttAndDirWithChildVo childAttAndDir;
}
