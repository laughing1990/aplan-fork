package com.augurit.aplanmis.common.dto;

import lombok.Data;


@Data
public class ProjStateDto {

    private String applyinstCode;//受理号
    private String stageItemName;//阶段/事项名称

    private String applyinstSource;//申报方式
    private String applyinstState;//申办状态

    private String dueNum;//办理时限
    private String restProcessingTime;//剩余办理时间

    private String projInfoId; //项目id
    private String stageId;  //阶段id
    private String isSeriesApprove;//是单项事项 1 ：是(单项)  0：否(并联)
    private String stageInstId; //阶段申请实例ID

    private String projName; // (项目名称)

}
