package com.augurit.aplanmis.front.approve.vo;

import com.augurit.agcloud.bsc.domain.BscAttForm;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;
import java.util.List;

@Data
public class AttDirTreeVo {
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
    private String dirNameSeq;
    private String storeType;
    private Integer dirSeqNum;

    private List<BscAttForm> files;

}
