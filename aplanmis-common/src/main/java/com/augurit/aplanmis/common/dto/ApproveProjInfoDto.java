package com.augurit.aplanmis.common.dto;

import com.augurit.aplanmis.common.domain.AeaHiIteminst;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Data
public class ApproveProjInfoDto {
    private String  applyinstId;//申请实例ID
    private String applyinstCode;//申报流水号
    private String  stageIteminstName;//阶段/事项实例名称
    private String  iteminstState;//事项实例状态
    //private String  businessState;//业务状态
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date startTime;//受理时间/申报时间
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date  endTime;//办结时间
    private String  projName;//项目名称
    private String localCode;//项目代码
    private String gcbm;//工程代码
    private String  isSeriesApprove;//是否串联审批  1 串联  0并联
    private Long dueNum;//办理时限
    private String iteminstId;//事项实例ID
    private String orgName;//部门名称
    private String promiseTime;//承诺时间
    private String projInfoId;//项目ID
    private String applyinstState;//申请状态
    private String stageName;//阶段名称，目前仅用于并联数据
    private String themeName;//主题名称
    private String approveComments;//办理意见
    private String iteminstCode;//办件编号
    private String itemVerId;
    private String stageinstId;
    private String stageId;
    List<AeaHiIteminstStateDto> iteminst;//事项实例及其状态

    public static List<AeaHiIteminstStateDto> formatList(List<AeaHiIteminst> aeaHiIteminsts) {
        List<AeaHiIteminstStateDto> dtoList=new ArrayList<>();
        if(aeaHiIteminsts.size()>0){
            for (AeaHiIteminst iteminst:aeaHiIteminsts){
                dtoList.add(format(iteminst));
            }
        }
        return dtoList;
    }

    public static AeaHiIteminstStateDto format(AeaHiIteminst iteminst){
        AeaHiIteminstStateDto dto=new AeaHiIteminstStateDto();
        dto.setIteminstName(iteminst.getIteminstName());
        dto.setIteminstState(iteminst.getIteminstState());
        return dto;
    }
}
