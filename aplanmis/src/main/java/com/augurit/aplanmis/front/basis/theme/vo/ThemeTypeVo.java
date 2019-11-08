package com.augurit.aplanmis.front.basis.theme.vo;

import com.augurit.agcloud.bsc.domain.BscDicCodeItem;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.util.Assert;

import java.util.ArrayList;
import java.util.List;

@Data
@ApiModel("主题类型和主题")
public class ThemeTypeVo {
    @ApiModelProperty(name = "themeTypeId", value = "主题类型id", dataType = "string")
    private String themeTypeId;
    @ApiModelProperty(name = "themeType", value = "主题类型", dataType = "string")
    private String themeType;
    @ApiModelProperty(name = "themeTypeName", value = "主题类型名称", dataType = "string")
    private String themeTypeName;
    @ApiModelProperty(value = "排序")
    private Long sortNo;
    @ApiModelProperty(name = "themes", value = "主题类型下的所有主题", dataType = "java.util.List")
    private List<ThemeVo> themes;

    public ThemeTypeVo() {
        themes = new ArrayList<>();
    }

    public void buildThemeTypeVo(BscDicCodeItem bscDicCodeItem) {
        Assert.notNull(bscDicCodeItem, "主题类型不能为空");

        this.setThemeTypeId(bscDicCodeItem.getTypeId());
        this.setThemeType(bscDicCodeItem.getItemCode());
        this.setThemeTypeName(bscDicCodeItem.getItemName());
        this.setSortNo(bscDicCodeItem.getItemSortNo());
    }

}
