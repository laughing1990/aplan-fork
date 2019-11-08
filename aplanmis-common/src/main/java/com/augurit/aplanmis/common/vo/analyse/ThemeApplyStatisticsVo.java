package com.augurit.aplanmis.common.vo.analyse;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

/**
 * @author Ma Yanhao
 * @date 2019/9/25 11:17
 */
@Data
public class ThemeApplyStatisticsVo {
    private String code;
    private String name;
    @JsonIgnore
    private Long applyCount;
    private Long count;
    private String precent;

    @JsonIgnore
    private String themeId;
    @JsonIgnore
    private String themeName;
    @JsonIgnore
    private String applyinstSource;
    @JsonIgnore
    private Long apply;
    @JsonIgnore
    private Long acceptDeal;
    @JsonIgnore
    private Long overtime;
}
