package com.augurit.aplanmis.supermarket.projPurchase.vo;

import com.augurit.aplanmis.common.domain.AeaImProjPurchase;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@Data
@ApiModel
public class OwnerIndexData {
    @ApiModelProperty(value = "所有项目数量")
    private int allProjectSum = 0;
    @ApiModelProperty(value = "所有项目数量")
    private int pendingProjectSum = 0;
    @ApiModelProperty(value = "所有项目数量")
    private int waitAuditProjectSum = 0;
    @ApiModelProperty(value = "所有项目数量")
    private int waitAnnounceProjectSum = 0;
    @ApiModelProperty(value = "所有项目数量")
    private int returnProjectSum = 0;
    @ApiModelProperty(value = "所有项目数量")
    private int alreadyAnnounceProjectSum = 0;
    @ApiModelProperty(value = "所有项目数量")
    private int alreadySelectionProjectSum = 0;
    @ApiModelProperty(value = "所有项目数量")
    private int invalidProjectSum = 0;
    @ApiModelProperty(value = "所有项目数量")
    private int inServiceProjectSum = 0;
    @ApiModelProperty(value = "所有项目数量")
    private int accomplishProjectSum = 0;
    @ApiModelProperty(value = "所有项目数量")
    private int waitSelectProjectSum = 0;
    @ApiModelProperty(value = "所有项目数量")
    private int waitUploadProjectSum = 0;
    @ApiModelProperty(value = "所有项目数量")
    private int waitEndProjectSum = 0;
    @ApiModelProperty(value = "待评价数量")
    private int waitEvaluateProjectSum = 0;

    public void changeToSum(List<AeaImProjPurchase> list) {
        if (null == list || list.isEmpty()) return;
        this.allProjectSum = list.size();
        for (AeaImProjPurchase data : list) {
            String auditFlag = data.getAuditFlag();

            if ("1".equals(auditFlag) || "2".equals(auditFlag) || "8".equals(auditFlag) || "9".equals(auditFlag)) {
                this.pendingProjectSum += 1;
                if ("9".equals(auditFlag)) {
                    this.waitUploadProjectSum += 1;
                } else if ("1".equals(auditFlag)) {
                    this.waitEndProjectSum += 1;
                } else if ("2".equals(auditFlag)) {
                    this.waitEvaluateProjectSum += 1;
                }
            } else if ("0".equals(auditFlag)) {
                this.waitAuditProjectSum += 1;
            } else if ("4".equals(auditFlag)) {
                this.waitAnnounceProjectSum += 1;
            } else if ("5".equals(auditFlag)) {
                this.returnProjectSum += 1;
            } else if ("6".equals(auditFlag) || "7".equals(auditFlag) || "8".equals(auditFlag)
                    || "9".equals(auditFlag) || "11".equals(auditFlag) || "12".equals(auditFlag)) {
                this.alreadyAnnounceProjectSum += 1;
                if ("11".equals(auditFlag)) {
                    this.waitSelectProjectSum += 1;
                }
            } else if ("9".equals(auditFlag)) {
                this.alreadySelectionProjectSum += 1;
            } else if ("10".equals(auditFlag)) {
                this.invalidProjectSum += 1;
            } else if ("1".equals(auditFlag)) {
                this.inServiceProjectSum += 1;
            } else if ("2".equals(auditFlag)) {
                this.accomplishProjectSum += 1;
            }
        }
    }
}
