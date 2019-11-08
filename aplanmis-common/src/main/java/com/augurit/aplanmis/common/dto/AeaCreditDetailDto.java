package com.augurit.aplanmis.common.dto;

import lombok.Data;

@Data
public class AeaCreditDetailDto {

    private String detailId; // (主键)
    private String summaryId; // (所属汇总ID)
    private String cnColumnName; // (字段中文名)
    private String enColumnName; // (字段英文名)
    private String columnValue; // (字段值)
    private String columnDataType; // (数据类型，s表示字符串，n表示数字，d表示日期)
    private String rootOrgId; // (根组织ID)
}
