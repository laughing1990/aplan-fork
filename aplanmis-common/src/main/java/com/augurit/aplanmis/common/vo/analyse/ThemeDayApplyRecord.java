package com.augurit.aplanmis.common.vo.analyse;

import io.swagger.annotations.ApiModel;
import lombok.Data;

@Data
@ApiModel("主题-阶段-日记录")
public class ThemeDayApplyRecord {
    private String applyinstId;
    private String newState;
    private String stageId ;
    private String stageName ;
    private String dybzspjdxh ;
    private String isNode ;
    private String  themeId ;
    private String  themeName ;
    private String isParallel;
    private String applyinstSource;
    private String isApprove;

    //统计用
    private long applyCount;//接件数、申报数

    private long count;//数量
}
