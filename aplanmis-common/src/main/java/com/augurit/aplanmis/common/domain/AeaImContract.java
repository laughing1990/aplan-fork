package com.augurit.aplanmis.common.domain;

import com.augurit.agcloud.bsc.domain.BscAttForm;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.List;

/**
 * -服务合同
 * <ul>
 * <li>项目名：奥格erp3.0--第一期建设项目</li>
 * <li>版本信息：v1.0</li>
 * <li>日期：2019-06-03 17:58:23</li>
 * <li>版权所有(C)2016广州奥格智能科技有限公司-版权所有</li>
 * <li>创建人:thinkpad</li>
 * <li>创建时间：2019-06-03 17:58:23</li>
 * <li>修改人1：</li>
 * <li>修改时间1：</li>
 * <li>修改内容1：</li>
 * </ul>
 */
@Data
@ApiModel("服务合同")
public class AeaImContract implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "服务合同ID")
    private String contractId; // ()

    @ApiModelProperty(value = "合同名称", required = true)
    @NotBlank
    private String contractName; // (合同名称)

    @ApiModelProperty(value = "合同编码", required = true)
    @NotBlank
    private String contractCode; // (合同编码)

    @ApiModelProperty(value = "合同金额", required = true)
    @NotBlank
    private String price; // (合同金额)

    @ApiModelProperty(value = "服务开始时间 yyyy-MM-dd", required = true)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @NotNull
    private java.util.Date serviceStartTime; // (服务开始时间)

    @ApiModelProperty(value = "服务结束时间 yyyy-MM-dd", required = true)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @NotNull
    private java.util.Date serviceEndTime; // (服务结束时间)

    @ApiModelProperty(value = "服务内容", required = true)
    @NotBlank
    private String serviceContent; // (服务内容)

    @ApiModelProperty(value = "备注")
    private String memo; // (备注)

    @ApiModelProperty(value = "签订时间")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private java.util.Date signTime; // (签订时间)

    @ApiModelProperty(value = "服务单位ID", required = true)
    @NotBlank
    private String serviceUnitInfoId; // (服务单位ID)

    @ApiModelProperty(value = "业主单位ID", required = true)
    @NotBlank
    private String ownerUnitInfoId; // (业主单位ID)

    private String creater; // ()

    @ApiModelProperty(value = "创建时间")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private java.util.Date createTime; // ()

    private String isDelete; // ()

    private String modifier; // ()

    @ApiModelProperty(value = "修改时间")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private java.util.Date modifyTime; // ()

    @ApiModelProperty(value = "合同状态：0 待确定，1 已确定 ，2 审核失败，3：审核中")
    private String auditFlag; // (审核状态)

    @ApiModelProperty(value = "企业报价信息ID", required = true)
    private String unitBiddingId; // ()

    @ApiModelProperty(value = "是否业主上传： 1 是， 0 否（中介机构上传）", required = true)
    private String isOwnerUpload; // (是否业主上传： 1 是， 0 否（中介机构上传）)

    @ApiModelProperty(value = "是否已确认：1 是， 0 否")
    private String isConfirm; // (是否已确认：1 是， 0 否)

    @ApiModelProperty(value = "采购需求ID", required = true)
    private String projPurchaseId; // (采购需求ID)

    @ApiModelProperty(value = "审核时间")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private java.util.Date auditTime; // (审核时间)

    @ApiModelProperty(value = "审核意见")
    private String auditOpinion; // (审核意见)

    @ApiModelProperty(value = "延期时间")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private java.util.Date postponeServiceEndTime; // (延期时间)


    //扩展字典
//    @ApiModelProperty(value = "文件列表")
//    private List<BscAttDetail> bscAttDetails;// 文件列表

    @ApiModelProperty(value = "文件列表")
    private List<BscAttForm> bscAttForms;// 文件列表

    @ApiModelProperty(value = "提交单位名称")
    private String createrUnitName;// 提交单位名称

    private String keyword;

    private String serviceUnitInfoName; // (服务单位名称)

    private String ownerUnitInfoName; // (业主单位名称)

    private String projName;//项目名称

    private AeaImPurchaseinst aeaImPurchaseinst;

    private String rootOrgId;//根组织ID

    public AeaImContract() {

    }

    public AeaImContract(String auditFlag, String isConfirm, String rootOrgId) {
        this.auditFlag = auditFlag;
        this.rootOrgId = rootOrgId;
        this.isConfirm = "1";
    }

    public enum ContractAuditFlag {
        待确定("0"),
        已确定("1"),
        审核失败("2"),
        审核中("3");

        private String auditFlag;

        ContractAuditFlag(String auditFlag) {
            this.auditFlag = auditFlag;
        }

        public String getAuditFlag() {
            return auditFlag;
        }
    }
}
