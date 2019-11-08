package com.augurit.aplanmis.common.dto;

import lombok.Data;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Data
public class AeaCreditSummaryDto {

    private String summaryId; // (主键)
    private String creditType; // (信用类型，bad表示失信，good表示守信，reg表示登记注册备案，cert表示资质认证许可)
    private String summaryType; // (对象类型，u表示单位，l表示联系人)
    private String creditUnitInfoId; // ()
    private String linkmanInfoId; // (联系人ID)
    private String cnTableName; // (表中文名)
    private String enTableName; // (表英文名)
    private String cnDeptName; // (所属单位中文名)
    private String enDeptName; // (所属单位英文名)
    private String isValid; // (是否有效，0表示无效，1表示有效)
    private String invalidReason; // (失效原因)
    private String rootOrgId; // (根组织ID)
    private String creditUnitInfoName;
    private String linkmanInfoName;
    private Date lastUpdateTime;//最后更新时间

    private List<AeaCreditDetailDto> detailDtos = new ArrayList<>();
}
