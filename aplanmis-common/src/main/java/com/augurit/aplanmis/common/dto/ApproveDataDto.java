package com.augurit.aplanmis.common.dto;

import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.Size;
import java.util.Date;

/**
 *  审批数据实体类（受理，审批，办结）
 */
@Data
public class ApproveDataDto {
    private String applyinstCode;  //申报流水号/受理号
    private String projName; //项目名称
    private String applySubjectName;//申报主体(单位名称或者个体户名称)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date startTime;//申报时间
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date assigneeTime;//办理时间
    private String applyState;//申报状态
    private String applyinstState;
}
