package com.augurit.aplanmis.front.apply.vo.receipt;

import com.augurit.agcloud.framework.util.CollectionUtils;
import com.augurit.agcloud.framework.util.StringUtils;
import com.augurit.aplanmis.front.apply.vo.ApplyinstIdVo;
import com.augurit.aplanmis.front.apply.vo.SeriesApplyDataVo;
import com.augurit.aplanmis.front.apply.vo.StageApplyDataVo;
import com.google.common.collect.Lists;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import org.springframework.util.Assert;

import java.util.HashSet;
import java.util.Set;

@ApiModel("保存回执vo")
@Getter
@Setter
public class SaveReceiptsVo {

    @ApiModelProperty(value = "以前生成的申报实例id", notes = "如： 打印回执、填写表单、暂存")
    private Set<String> alreadExistsApplyinstIds;

    @ApiModelProperty(value = "当前的申报实例", notes = "并联申报存在并行事项，所以有多个")
    private Set<String> currentApplyinstIds;

    @ApiModelProperty(value = "领件人信息对象id")
    private String smsInfoId;

    @ApiModelProperty(value = "办理意见", required = true)
    private String comments;

    private String[] receiptTypes;

    private SaveReceiptsVo() {
        alreadExistsApplyinstIds = new HashSet<>();
        currentApplyinstIds = new HashSet<>();
    }

    public static SaveReceiptsVo fromSeriesApplyDataVo(SeriesApplyDataVo seriesApplyDataVo, String currentApplyinstId, String[] receiptTypes) {
        Assert.isTrue(receiptTypes != null && receiptTypes.length > 0, "回执类型不能为空");

        SaveReceiptsVo saveReceiptsVo = new SaveReceiptsVo();
        if (StringUtils.isNotBlank(seriesApplyDataVo.getApplyinstId())) {
            saveReceiptsVo.getAlreadExistsApplyinstIds().add(seriesApplyDataVo.getApplyinstId());
        }
        saveReceiptsVo.setSmsInfoId(seriesApplyDataVo.getSmsInfoId());
        saveReceiptsVo.setComments(seriesApplyDataVo.getComments());
        saveReceiptsVo.getCurrentApplyinstIds().add(currentApplyinstId);
        saveReceiptsVo.setReceiptTypes(receiptTypes);
        return saveReceiptsVo;
    }

    public static SaveReceiptsVo fromStageApplyDataVo(StageApplyDataVo stageApplyDataVo, ApplyinstIdVo applyinstIdVo, String[] receiptTypes) {
        Assert.isTrue(receiptTypes != null && receiptTypes.length > 0, "回执类型不能为空");
        Assert.notNull(applyinstIdVo, "申报实例不存在");

        SaveReceiptsVo saveReceiptsVo = new SaveReceiptsVo();
        if (stageApplyDataVo.getApplyinstIds() != null && stageApplyDataVo.getApplyinstIds().length > 0) {
            saveReceiptsVo.getAlreadExistsApplyinstIds().addAll(Lists.newArrayList(stageApplyDataVo.getApplyinstIds()));
        }
        if (StringUtils.isNotBlank(stageApplyDataVo.getParallelApplyinstId())) {
            saveReceiptsVo.getAlreadExistsApplyinstIds().add(stageApplyDataVo.getParallelApplyinstId());
        }
        saveReceiptsVo.setSmsInfoId(stageApplyDataVo.getSmsInfoId());
        saveReceiptsVo.setComments(stageApplyDataVo.getComments());
        if (CollectionUtils.isNotEmpty(applyinstIdVo.getApplyinstIds())) {
            saveReceiptsVo.getCurrentApplyinstIds().addAll(Lists.newArrayList(applyinstIdVo.getApplyinstIds()));
        }
        if (StringUtils.isNotBlank(applyinstIdVo.getParallelApplyinstId())) {
            saveReceiptsVo.getCurrentApplyinstIds().add(applyinstIdVo.getParallelApplyinstId());
        }
        saveReceiptsVo.setReceiptTypes(receiptTypes);
        return saveReceiptsVo;
    }
}
