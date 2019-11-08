package com.augurit.aplanmis.common.domain;

import lombok.Data;

import java.io.Serializable;

/**
 * 办理流程-模型
 */
@Data
public class AeaItemServiceFlow implements Serializable {
    private static final long serialVersionUID = 1L;

    private String id; // (办理流程ID)
    private String itemId; // (本级事项目录ID)
    private String ckbllcsm; // (窗口办理流程说明)
    private String ckbllct; // (窗口办理流程图(上传附件))
    private String wsbllcsm; // (网上办理流程说明)
    private String wsbllct; // (网上办理流程图（上传附件）)
    private String nbxulcsm; // (内部办理流程说明)
    private String nbxklct; // (内部办理流程图（上传附件）)
    private String ywtssc; // (有无特殊审查)
    private String dataId; // (省的数据ID)

    //扩展字段
    private String ckbllctDocFile;
    private String wsbllctDocFile;
    private String nbxklctDocFile;
}
