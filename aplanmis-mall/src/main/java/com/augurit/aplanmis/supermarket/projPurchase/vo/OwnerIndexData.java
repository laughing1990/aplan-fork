package com.augurit.aplanmis.supermarket.projPurchase.vo;

import com.augurit.aplanmis.common.constants.AuditFlagStatus;
import com.augurit.aplanmis.common.domain.AeaImProjPurchase;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Arrays;
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

    @ApiModelProperty(value = "服务中项目数量")
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

    //采购需求状态：0：未提交，1：服务中，2：服务完成，3：服务中止，4：审核中，5：退回，6：报名中，7：选取中，8：选取开始，9：已选取，10：无效，11：待选取，12：已过时
    public void changeToSum(List<AeaImProjPurchase> list) {
        if (null == list || list.isEmpty()) return;
        this.allProjectSum = list.size();
        for (AeaImProjPurchase data : list) {
            String auditFlag = data.getAuditFlag();

            switch (auditFlag) {
                case AuditFlagStatus.NO_COMMIT://0 未提交
                    this.waitAuditProjectSum += 1;
                    break;
                case AuditFlagStatus.AUDIT_PROGRESS://4 审核中
                    this.waitAnnounceProjectSum += 1;
                    break;
                case AuditFlagStatus.AUDIT_RETURN://5 退回
                    this.returnProjectSum += 1;
                    break;
                case AuditFlagStatus.INVALID://10 无效
                    this.invalidProjectSum += 1;
                    break;
                case AuditFlagStatus.WAIT_CHOOSE://11
                    this.waitSelectProjectSum += 1;
                    this.alreadyAnnounceProjectSum += 1;
                    break;
                case AuditFlagStatus.REGISTRATION_PROGRESS://6
                case AuditFlagStatus.CHOOSE_PROGRESS://7
                case AuditFlagStatus.TIME_OUT://12
                    this.alreadyAnnounceProjectSum += 1;
                    break;
                case AuditFlagStatus.CHOOSE_START://8
                    this.pendingProjectSum += 1;
                case AuditFlagStatus.CHOOSE_END://9
                    this.pendingProjectSum += 1;
                    this.waitUploadProjectSum += 1;
                    this.alreadySelectionProjectSum += 1;
                    this.alreadyAnnounceProjectSum += 1;
                    break;
                case AuditFlagStatus.SERVICE_PROGRESS://1
                    this.pendingProjectSum += 1;
                    this.waitEndProjectSum += 1;
                    this.inServiceProjectSum += 1;
                    break;
                case AuditFlagStatus.SERVICE_FINISH:
                    this.pendingProjectSum += 1;
                    this.waitEvaluateProjectSum += 1;
                    this.accomplishProjectSum += 1;
                    break;
            }


        }
    }

    //1 2 8 9
    String[] pendingProject = {AuditFlagStatus.SERVICE_PROGRESS, AuditFlagStatus.SERVICE_FINISH, AuditFlagStatus.CHOOSE_START, AuditFlagStatus.CHOOSE_END};
    //6 7 11 12
    String[] alreadyAnnounceProject = {AuditFlagStatus.REGISTRATION_PROGRESS, AuditFlagStatus.CHOOSE_PROGRESS, AuditFlagStatus.WAIT_CHOOSE, AuditFlagStatus.TIME_OUT};

    public void changeToSum2(List<AeaImProjPurchase> list) {
        if (null == list || list.isEmpty()) return;
        this.allProjectSum = list.size();
        for (AeaImProjPurchase data : list) {
            String auditFlag = data.getAuditFlag();

            if (Arrays.binarySearch(pendingProject, auditFlag) > 0) {
                this.pendingProjectSum += 1;
                if ("9".equals(auditFlag)) {
                    this.waitUploadProjectSum += 1;
                    this.alreadySelectionProjectSum += 1;
                    this.alreadyAnnounceProjectSum += 1;
                } else if ("1".equals(auditFlag)) {
                    this.waitEndProjectSum += 1;
                    this.inServiceProjectSum += 1;
                } else if ("2".equals(auditFlag)) {
                    this.waitEvaluateProjectSum += 1;
                    this.accomplishProjectSum += 1;
                } else if ("8".equals(auditFlag)) {
                    this.alreadyAnnounceProjectSum += 1;
                }
            } else if ("0".equals(auditFlag)) {
                this.waitAuditProjectSum += 1;
            } else if ("4".equals(auditFlag)) {
                this.waitAnnounceProjectSum += 1;
            } else if ("5".equals(auditFlag)) {
                this.returnProjectSum += 1;
            } else if (Arrays.binarySearch(alreadyAnnounceProject, auditFlag) > 0) {
                this.alreadyAnnounceProjectSum += 1;
                if ("11".equals(auditFlag)) {
                    this.waitSelectProjectSum += 1;
                }
            } else if ("10".equals(auditFlag)) {
                this.invalidProjectSum += 1;
            }
        }
    }

    public void changeToSum1(List<AeaImProjPurchase> list) {
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
