package com.augurit.aplanmis.front.queryView.vo;

import com.augurit.agcloud.bsc.domain.BscDicCodeItem;
import com.augurit.agcloud.bsc.domain.BscDicRegion;
import com.augurit.aplanmis.common.domain.AeaParTheme;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 查询条件数据
 * @author tiantian
 * @date 2019/9/4
 */
@Data
@ApiModel("多条件查询数据字典")
public class ConditionalQueryDic implements Serializable {

    private static final long serialVersionUID = 1L;

    @Deprecated
    @ApiModelProperty(value = "所在区域")
    private List<BscDicCodeItem> regionList;

    @ApiModelProperty(value = "立项类型")
    private List<BscDicCodeItem> projTypeList;

    @ApiModelProperty(value = "主题")
    private List<AeaParTheme> themeList;

    @ApiModelProperty(value = "投资类别")
    private List<BscDicCodeItem> investCategoryList;

    @ApiModelProperty(value = "建设性质")
    private List<BscDicCodeItem> buildNatureList;

    @ApiModelProperty(value = "申报来源")
    private List<BscDicCodeItem> applySourceList;

    @ApiModelProperty(value = "项目级别")
    private List<BscDicCodeItem> projLevelList;

    @ApiModelProperty(value = "申报类型")
    private List<BscDicCodeItem> applyTypeList;

    @ApiModelProperty(value = "申报状态")
    private List<BscDicCodeItem> applyStateList;

    @ApiModelProperty(value = "单位类型")
    private List<BscDicCodeItem> unitTypeList;

    @ApiModelProperty(value = "事项实例状态")
    private List<BscDicCodeItem> iteminstStateList;

    @ApiModelProperty(value = "业务时限状态")
    private List<BscDicCodeItem> instStateList;

    @ApiModelProperty(value = "行政区划")
    List<BscDicRegion> regionalismList;


}
