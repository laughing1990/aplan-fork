package com.augurit.aplanmis.common.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AeaProjDrawingVo {
    private String drawingId; // (主键)
    private String projInfoId; // (项目ID)
    private String drawingQuabookCode; // (施工图审查合格书编号)
    private String inverstmentMoeny; // (投资额，单位：万元)
    private String approveDrawingArea; // (图审面积，单位：平方米)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private java.util.Date approveStartTime; // (开始审查日期)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private java.util.Date approveEndTime; // (审查完成日期)
    private String isOncePass; // (一次审查是否通过，0表示否，1表示是)
    private String oncePassAgainstCount; // (一次审查时违反强条数)
    private String oncePassAgainstItem; // (一次审查时违反的强条条目)
    private String approveOpinion; // (施工图审查意见（终审）)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private java.util.Date approveConfirmTime; // (审图确认时间)
    private String govOrgCode; // (审图确认的行政单位机构代码)
    private String govOrgName; // (审图确认的行政单位名称)
    private String govOrgAreaCode; // (审图确认的行政单位区域码)

    List<AeaProjDrawing> aeaProjDrawing;//单位信息

    private String formId;//扩展字段

}
