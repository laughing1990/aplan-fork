package com.augurit.aplanmis.rest.index.service.vo;


import com.augurit.agcloud.bsc.domain.BscDicCodeItem;
import com.augurit.aplanmis.common.domain.AeaParTheme;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.util.Assert;

import java.util.List;

@Data
@ApiModel("主题类别")
public class ThemeTypeVo {

    @ApiModelProperty(value = "主题类别名称")
    private String themeTypeName;
    @ApiModelProperty(value = "主题类别编码")
    private String themeTypeCode;
    @ApiModelProperty(value = "主题列表")
    private List<AeaParTheme> themeList;

    public void buildThemeTypeVo(BscDicCodeItem bscDicCodeItem) {
        Assert.notNull(bscDicCodeItem, "主题类型不能为空");

        this.setThemeTypeName(bscDicCodeItem.getItemName());
        this.setThemeTypeCode(bscDicCodeItem.getItemCode());
    }
}
