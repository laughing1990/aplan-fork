package com.augurit.aplanmis.province.vo;

import com.augurit.aplanmis.common.domain.AeaMatinst;
import lombok.Data;

import java.util.List;

@Data
public class ParallelApproveDataVo {
    private String itemName; //事项名称
    private String currentState;//当前状态
    private String iteminstId;//事项实例ID
    private String projCode;//项目编码
    private String applyinstId;//申请实例ID
    private String approveOrgName; //审批部门
    private List<AeaMatinst> fileList;//相关文件
}
