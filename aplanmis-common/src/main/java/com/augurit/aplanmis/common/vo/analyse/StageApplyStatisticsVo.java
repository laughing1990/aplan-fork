package com.augurit.aplanmis.common.vo.analyse;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

/**
 * @author Ma Yanhao
 * @date 2019/9/25 11:50
 */
@Data
public class StageApplyStatisticsVo {

    private String code;
    private String name;
    @JsonIgnore
    private Long applyCount;
    private Long count;
    private String precent;

}
