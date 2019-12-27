package com.augurit.aplanmis.mall.log.vo;

import com.augurit.aplanmis.common.constants.DeletedStatus;
import com.augurit.aplanmis.common.domain.AeaHiIteminst;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
@ApiModel("事项确认时事项返回结果VO")
public class IteminstConfirmVo {
    @ApiModelProperty(value = "事项ID")
    private String itemId;
    @ApiModelProperty(value = "事项版本ID")
    private String itemVerId;
    @ApiModelProperty(value = "事项实例ID")
    private String iteminstId;
    @ApiModelProperty(value = "事项名称")
    private String iteminstName;
    @ApiModelProperty(value = "承诺审批时限")
    private Double dueNum;
    @ApiModelProperty(value = "审批部门")
    private String approveOrgName;
    @ApiModelProperty(value = "是否已删除")
    private String isDeleted;
    @ApiModelProperty(value = "情形列表")
    private List<Map<String, String>> stateList;

    @ApiModelProperty(value = "结果物")
    private List<String> matList;
    @ApiModelProperty(value = "部门意见")
    private String deptComments;
    @ApiModelProperty(value = "是否必选")
    private String isMustSelected;
    @ApiModelProperty(value = "是否申请人选择")
    private String isApplySelected;
    @ApiModelProperty(value = "是否部门辅导选择")
    private String isDeptSelected;


    public static IteminstConfirmVo format(AeaHiIteminst aeaHiIteminst){
        IteminstConfirmVo vo=new IteminstConfirmVo();
        vo.setIteminstId(aeaHiIteminst.getIteminstId());
        vo.setItemId(aeaHiIteminst.getItemId());
        vo.setItemVerId(aeaHiIteminst.getItemVerId());
        vo.setIteminstName(aeaHiIteminst.getIteminstName());
        vo.setApproveOrgName(aeaHiIteminst.getApproveOrgName());
        vo.setDueNum(aeaHiIteminst.getDueNum());
        vo.setStateList(aeaHiIteminst.getItemStateinsts());
        vo.setIsDeleted(aeaHiIteminst.getIsDeleted());
        if(DeletedStatus.DELETED.getValue().equals(aeaHiIteminst.getIsDeleted())){
            vo.setIsApplySelected("1");
        }
        return vo;
    }

}
