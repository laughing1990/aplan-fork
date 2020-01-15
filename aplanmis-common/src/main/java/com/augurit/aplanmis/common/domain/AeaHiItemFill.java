package com.augurit.aplanmis.common.domain;

import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.Size;
import java.io.Serializable;

/**
 * 事项容缺补齐实例表-模型
 */
@Data
public class AeaHiItemFill implements Serializable {

    private static final long serialVersionUID = 1L;
    private String fillId; // (主键ID)
    private String iteminstId; // (事项实例ID)
    private String applyinstId; // (申报实例ID)
    private String projInfoId; // (项目ID)
    private String fillDesc; // (描述说明)
    private String fillMemo; // (备注说明)
    private String chargeOrgId; // (负责部门ID)
    private String chargeOrgName; // (负责部门名称)
    private String opsUserId; // (经办人ID)
    private String opsUserName; // (经办人姓名)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private java.util.Date lastOpsTime; // (最后审核日期)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private java.util.Date lastUploadTime; // (最近上传材料附件时间)
    @Size(max = 10)
    private Long lastTipsCount; // (提示材料总数（当业主上传2个材料，显示2；当审核后归零）)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private java.util.Date fillEndTime; // (补齐结束时间)
    private String fillState; // (补齐状态，1表示未开始，2表示补齐中，3表示补齐完毕)
    private String isDeleted; // (逻辑删除，0表示未删除，1表示已删除)
    private String creater; // (创建人)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private java.util.Date createTime; // (创建时间)
    private String modifier; // (修改人)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private java.util.Date modifyTime; // (修改时间)
    private String rootOrgId; // (根组织ID)
}
