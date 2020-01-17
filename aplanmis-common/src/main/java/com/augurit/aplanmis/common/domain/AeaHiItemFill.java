package com.augurit.aplanmis.common.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.List;

/**
 * 事项容缺补齐实例表-模型
 */
@Data
public class AeaHiItemFill implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("主键ID")
    private String fillId;

    @ApiModelProperty("事项实例ID")
    private String iteminstId;

    @ApiModelProperty("申报实例ID")
    private String applyinstId;

    @ApiModelProperty("项目ID")
    private String projInfoId;

    @ApiModelProperty("申请人补齐意见")
    private String fillDesc; // (描述说明)

    @ApiModelProperty("备注说明")
    private String fillMemo;

    @ApiModelProperty("负责部门ID")
    private String chargeOrgId;

    @ApiModelProperty("负责部门名称")
    private String chargeOrgName;

    @ApiModelProperty("经办人ID")
    private String opsUserId;

    @ApiModelProperty("经办人姓名")
    private String opsUserName;

    @ApiModelProperty("最后审核日期")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private java.util.Date lastOpsTime;

    @ApiModelProperty("最近上传材料附件时间")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private java.util.Date lastUploadTime;

    @ApiModelProperty("提示材料总数（当业主上传2个材料，显示2；当审核后归零）")
    private Long lastTipsCount;

    @ApiModelProperty("补齐结束时间")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private java.util.Date fillEndTime;

    @ApiModelProperty("补齐状态，1表示未开始，2表示补齐中，3表示补齐完毕")
    private String fillState;

    @ApiModelProperty("逻辑删除，0表示未删除，1表示已删除")
    private String isDeleted;

    @ApiModelProperty("创建人")
    private String creater;

    @ApiModelProperty("创建时间")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private java.util.Date createTime;

    @ApiModelProperty("修改人")
    private String modifier;

    @ApiModelProperty("修改时间")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private java.util.Date modifyTime;

    @ApiModelProperty("根组织ID")
    private String rootOrgId;

    //非表字段
    @ApiModelProperty("来源 net表示网厅申报，win表示窗口申报")
    private String applyinstSource;

    @ApiModelProperty("类型 0表示并联，1表示串联")
    private String isSeriesApprove;

    @ApiModelProperty("申报流水号")
    private String applyinstCode;

    @ApiModelProperty("项目代码")
    private String localCode;

    @ApiModelProperty("项目名称")
    private String projName;

    @ApiModelProperty("申报阶段名称")
    private String stageName;

    @ApiModelProperty("对应国家标准审批阶段，多选，1 立项用地规划许可 2 立项用地规划许可 3 施工许可 4 竣工验收 5 并行推进")
    private String dybzspjdxh;

    @ApiModelProperty("审批事项名称")
    private String itemName;

    @ApiModelProperty("申报时间")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm", locale = "zh", timezone = "GMT+8")
    private java.util.Date applyTime;

    @ApiModelProperty("是否可以审核 true展示审核按钮，false展示查看按钮")
    private boolean approveUser;

    @ApiModelProperty("查询关键字")
    private String keyword;

    @ApiModelProperty("业主单位ID")
    private String unitInfoId;

    @ApiModelProperty("业主单位名称")
    private String unitName;

    @ApiModelProperty("容缺补齐材料集合")
    private List<AeaHiItemFillDueIninst> itemFillDueIninstList;

    @ApiModelProperty("部门ID数组。查询使用")
    private String[] orgIds;
}
