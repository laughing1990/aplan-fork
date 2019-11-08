package com.augurit.aplanmis.common.dto;

import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;
import java.util.List;

/**
 * 网厅材料补全/补正实体
 */
@Data
public class SupplementInfoDto {
    private String isSeriesApprove;//是否串联审批  1 串联  0并联
    private String applyinstId;//申请实例ID
    private String applyinstCode;//申报流水号
    private String projName;//项目名称
    private String opsUserOpinion;//意见
    private String iteminstId;//事项实例ID
    private String stageName;//阶段名称
    private String iteminstName;//事项实例名称
    private String itemStageName;//事项或阶段名称
    private String projInfoId;//项目ID
    private List<String> opsUserOpinionList;//意见汇总（合并不同意见的重复数据）
    private String linkmanName;//联系人姓名
    private String themeName;//主题名称
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;
    private String applyinstCorrectId;//材料补正ID
    private String localCode;//项目代码
    private String gcbm;//工程代码
    private String correctMemo;//补全备注
}
