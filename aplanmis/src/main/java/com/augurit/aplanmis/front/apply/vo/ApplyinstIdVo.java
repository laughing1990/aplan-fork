package com.augurit.aplanmis.front.apply.vo;

import com.augurit.agcloud.framework.util.StringUtils;
import com.augurit.aplanmis.common.constants.ApplyType;
import com.augurit.aplanmis.common.domain.AeaHiApplyinst;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.util.Assert;

import java.util.ArrayList;
import java.util.List;

@Data
@ApiModel("申报实例ID结果集")
public class ApplyinstIdVo {

    @ApiModelProperty(value = "并行申请实例ID列表")
    private List<String> applyinstIds;

    @ApiModelProperty(value = "并联申请实例ID")
    private String parallelApplyinstId;

    public ApplyinstIdVo() {
        applyinstIds = new ArrayList<>();
    }

    public void addApplyinstId(AeaHiApplyinst aeaHiApplyinst) {
        if (StringUtils.isNotBlank(aeaHiApplyinst.getApplyinstId())) {
            if (ApplyType.SERIES.getValue().equals(aeaHiApplyinst.getIsSeriesApprove())) {
                this.applyinstIds.add(aeaHiApplyinst.getApplyinstId());
            } else if (ApplyType.UNIT.getValue().equals(aeaHiApplyinst.getIsSeriesApprove())) {
                Assert.isTrue(StringUtils.isBlank(this.parallelApplyinstId), "同时出现多个并联申报实例");
                this.parallelApplyinstId = aeaHiApplyinst.getApplyinstId();
            }
        }
    }
}