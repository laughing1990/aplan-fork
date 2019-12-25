package com.augurit.aplanmis.common.domain;

import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.Size;
import java.io.Serializable;

/**
 * 征求意见详情表-模型
 */
@Data
public class AeaHiSolicitDetail implements Serializable {
// ----------------------------------------------------- Properties

    private static final long serialVersionUID = 1L;
    private String solicitDetailId; // (主键)
    private String solicitId; // (征求意见主表ID)
    private String detailOrgId; // (征求意见部门ID)
    private String detailOrgName; // (征求意见部门名称)
    private String itemId; // (事项ID【当SOLICIT_TYPE=i】)
    private String itemVerId; // (事项版本ID【当SOLICIT_TYPE=i】)
    @Size(max = 10)
    private Long detailDueDays; // (意见征求承诺时限，例如：5个工作日，那该字段为5)
    @Size(max = 10)
    private Long detailRealDays; // (意见征求实际时限，例如：5个工作日，那该字段为5)
    private String detailDaysUnit; // (意见征求时限单位，z表示自然日，g表示工作日)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private java.util.Date detailStartTime; // (意见征求开始时间)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private java.util.Date detailEndTime; // (意见征求结束时间)
    private String detailState; // (征求意见状态：0表示未开始，1表示征求中，2表示已完成，3表示已终止)
    private String isDeleted; // (是否删除：0表示未删除；1表示已删除)
    private String creater; // (创建人)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private java.util.Date createTime; // (创建日期)
    private String modifier; // (更新人)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private java.util.Date modifyTime; // (更新时间)
}
