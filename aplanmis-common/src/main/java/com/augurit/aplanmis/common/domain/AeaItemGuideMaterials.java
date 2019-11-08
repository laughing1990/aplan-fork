package com.augurit.aplanmis.common.domain;

import lombok.Data;

import java.io.Serializable;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Data
public class AeaItemGuideMaterials implements Serializable {

    private static final long serialVersionUID = 1L;

    private String intermediaryservicescode; // (中介服务事项编码)
    private String itemVerId; // (事项版本id)
    private String materialName; // (材料名称)
    private String materialType; // (材料类型)
    private String materialTypeText; // (材料类型文本)
    private String rowguid; // (材料ID)
    private String sourceExplain; // (来源渠道说明)
    private String sourceType; // (材料来源渠道)
    private String sourceTtypeText; // (材料来源渠道文本)
    private String zzhdzb; // (材料形式)
    private String zzhdzbText; // (材料形式文本)
    private String zzzExampleGuid; // (示例样表)
    private String intermediaryservices; // (中介服务事项名称)
    private String zzzFillExplian; // (填报须知)
    private String rootOrgId;
}

