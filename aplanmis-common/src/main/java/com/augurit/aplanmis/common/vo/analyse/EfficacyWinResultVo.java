package com.augurit.aplanmis.common.vo.analyse;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@Data
@ApiModel("窗口各细类统计实体")
public class EfficacyWinResultVo implements Serializable {
    @ApiModelProperty(value = "排名")
    private int sortNo;//排名
    @ApiModelProperty(value = "窗口名称")
    private String windowName;//窗口名称
    @ApiModelProperty(value = "接件数量（不含不予受理）")
    private Long acceptCount;//
    @ApiModelProperty(value = "网厅接件数")
    private Long netAcceptCount;//
    @ApiModelProperty(value = "窗口接件数")
    private Long winAcceptCount;//
    @ApiModelProperty(value = "不予受理数")
    private Long notAcceptCount;//
    @ApiModelProperty(value = "受理率")
    private String acceptRate;//
    @ApiModelProperty(value = "逾期数量")
    private Long overdueCount;//
    @ApiModelProperty(value = "逾期率")
    private String overdueRate;//
}
