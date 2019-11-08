package com.augurit.aplanmis.rest.userCenter.vo;

import com.augurit.aplanmis.common.domain.AeaHiIteminst;
import com.augurit.aplanmis.common.domain.AeaParStage;
import com.augurit.aplanmis.common.dto.ProjStateDto;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;
import java.util.Set;

/*
  生命周期图VO
 */
@Data
@ApiModel("  生命周期图VO")
public class LifeCycleInfoVo extends ProjStateDto {


    @ApiModelProperty("已办结事项数")
    private int countItem;         //已办结事项数
    @ApiModelProperty("历时（工作日）")
    private double workDay;            //历时（工作日）
    @ApiModelProperty("逾期事项数")
    private int overDueItem;        //逾期事项数
    @ApiModelProperty("正常审批事件数量")
    private int expectItem;         //正常审批事件数量
    @ApiModelProperty("并联事项数")
    private int paraCountItem;  //并联事项数
    @ApiModelProperty("并联历时")
    private double paraWorkDay;   //并联历时
    @ApiModelProperty("可并行事项数")
    private int seriCountItem;  //可并行事项数
    @ApiModelProperty("可并行历时")
    private double seriWorkDay;   //可并行历时
    @ApiModelProperty("阶段")
    private List<AeaParStage> aeaParStages;

    private Set<AeaHiIteminst> iteminstSet;//可并行事项列表

    private List<AeaHiIteminst> iteminstList;//并联事项列表

}
