package com.augurit.aplanmis.common.domain;

import lombok.Data;

import java.io.Serializable;

@Data
public class AeaItemGuideSpecials implements Serializable {

    private static final long serialVersionUID = 1L;

    private String id; // (主建)
    private String itemVerId; // (事项版本id)
    private String othername; // (其他审查方式)
    private String sx; // (时限)
    private String sxdw; // (时限单位)
    private String sxdwText; // (时限单位文本)
    private String type; // (特别程序类型)
    private String typeText; // (特别程序类型文本)
    private String rootOrgId; // 根组织id
}

