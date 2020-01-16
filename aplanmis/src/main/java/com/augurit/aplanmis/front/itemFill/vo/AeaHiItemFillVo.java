package com.augurit.aplanmis.front.itemFill.vo;

import com.augurit.aplanmis.common.domain.AeaHiItemFill;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.beans.BeanUtils;

import java.util.List;

@Data
@ApiModel("容缺审核数据模型")
public class AeaHiItemFillVo {

    @ApiModelProperty(value = "主键", required = false, dataType="string")
    private String fillId;

    @ApiModelProperty(value = "事项实例ID)", required = false, dataType="string")
    private String iteminstId;

    @ApiModelProperty(value = "申报实例ID", required = false, dataType="string")
    private String applyinstId;

    @ApiModelProperty(value = "项目ID", required = false, dataType="string")
    private String projInfoId;

    @ApiModelProperty(value = "描述说明，企业补齐容缺材料补齐意见", required = false, dataType="string")
    private String fillDesc;

    @ApiModelProperty(value = "备注说明", required = false, dataType="string")
    private String fillMemo;

    @ApiModelProperty(value = "负责部门ID", required = false, dataType="string")
    private String chargeOrgId;

    @ApiModelProperty(value = "负责部门名称", required = false, dataType="string")
    private String chargeOrgName;

    @ApiModelProperty(value = "经办人ID", required = false, dataType="string")
    private String opsUserId;

    @ApiModelProperty(value = "经办人姓名", required = false, dataType="string")
    private String opsUserName;

    @ApiModelProperty(value = "上一次最后审核日期", required = false, dataType="string")
    private java.util.Date lastOpsTime;

    @ApiModelProperty(value = "企业最近上传材料附件时间", required = false, dataType="string")
    private java.util.Date lastUploadTime;

    @ApiModelProperty(value = "视图列表提示材料总数（当业主上传2个材料，显示2；当审核后归零）", required = false, dataType="string")
    private Long lastTipsCount;

    @ApiModelProperty(value = "补齐结束时间", required = false, dataType="string")
    private java.util.Date fillEndTime;

    @ApiModelProperty(value = "补齐状态，1表示未开始，2表示补齐中，3表示补齐完毕", required = false, dataType="string")
    private String fillState;

    @ApiModelProperty(value = "逻辑删除，0表示未删除，1表示已删除", required = false, dataType="string")
    private String isDeleted;

    @ApiModelProperty(value = "创建人", required = false, dataType="string")
    private String creater;

    @ApiModelProperty(value = "创建时间", required = false, dataType="string")
    private java.util.Date createTime;

    @ApiModelProperty(value = "修改人", required = false, dataType="string")
    private String modifier;

    @ApiModelProperty(value = "修改时间", required = false, dataType="string")
    private java.util.Date modifyTime;

    @ApiModelProperty(value = "根组织ID", required = false, dataType="string")
    private String rootOrgId;

    //扩展字段
    @ApiModelProperty(value = "申报流水号", required = false, dataType="string")
    private String applyCode;

    @ApiModelProperty(value = "项目/工程代码", required = false, dataType="string")
    private String projectCode;

    @ApiModelProperty(value = "项目/工程名称", required = false, dataType="string")
    private String projectName;

    @ApiModelProperty(value = "业主单位", required = false, dataType="string")
    private String unitName;

    @ApiModelProperty(value = "事项名称", required = false, dataType="string")
    private String itemName;

    @ApiModelProperty(value = "事项审批部门名称", required = false, dataType="string")
    private String approveOrgName;

    @ApiModelProperty(value = "容缺待补齐列表", required = false, dataType="string")
    List<AeaHiItemFillDueIninstVo> fillDueIninstVos;

    @ApiModelProperty(value = "容缺已补齐列表", required = false, dataType="string")
    List<AeaHiItemFillRealIninstVo> fillRealIninstVos;

    public AeaHiItemFill convertToAeaHiItemFill(){
        AeaHiItemFill temp = new AeaHiItemFill();
        BeanUtils.copyProperties(this,temp);
        return temp;
    }

    public void createByAeaHiItemFill(AeaHiItemFill aeaHiItemFill){
        BeanUtils.copyProperties(aeaHiItemFill,this);
    }
}
