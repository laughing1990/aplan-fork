package com.augurit.aplanmis.common.vo.analyse;

import io.swagger.annotations.ApiModel;
import lombok.Data;

@Data
@ApiModel("申报时限统计实体")
public class ApplyLimitTimeVo {
    private String applyinstId;
    private String isApprove;
    private String dybzspjdxh;
    private String instState;
    private String instStateName;
    private Double useLimitTime;
    private String windowId;
    private String windowName;
    private String isParallel;

    //
    private long count;//数量

}
