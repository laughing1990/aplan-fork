package com.augurit.aplanmis.mall.main.vo;


import com.augurit.aplanmis.common.domain.AeaItemBasic;
import com.augurit.aplanmis.common.domain.AeaParTheme;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@Data
@ApiModel("事项、主题list")
public class ThemeAndItemListVo {

    @ApiModelProperty("事项列表")
    private List<AeaItemBasic> itemList;
    @ApiModelProperty("主题列表")
    private List<AeaParTheme> themeList;
}
