package com.augurit.aplanmis.common.domain;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * 主题定义表-模型
 */
@Data
@ApiModel("主题实体")
public class AeaParTheme implements Serializable {

    private static final long serialVersionUID = 1L;

    private String themeId; // (ID)
    @ApiModelProperty("主题名称")
    private String themeName; // (主题名称)
    @ApiModelProperty("主题编码")
    private String themeCode;//主题编码
    @ApiModelProperty("主题类型")
    private String themeType; // 主题类型
    private Long dueNum; // (承诺时限数字)
    private String dueUnit; // (承诺时限单位)
    private String complainPhone; // (投诉电话)
    private String hotlinePhone; // (咨询电话)
    private String handleAddress; // (办理地点)
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date handleTime; // (办理时间)
    private Long sortNo; // (排序号)
    private String isActive; // (是否启用，0表示禁用，1表示启用)
    @ApiModelProperty("备注说明")
    private String themeMemo; // (备注说明)
    private String isDeleted;

    private String creater; // (创建人)
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime; // (创建时间)
    private String modifier; // (修改人)
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date modifyTime; // (修改时间)
    private String isImgIcon; // (是否使用图片图标，0表示使用CSS图标，1表示使用图片图标)
    private String themeIconCss; // (图标CSS样式【不要跟SMALL_IMG_PATH等图标图片字段混合使用，适用于对H5支持较好的浏览器场景】)
    private String smallImgPath; // (菜单图标小图片路径【16*16，使用图片作为图标，适用于对H5支持不好的浏览器场景】)
    private String middleImgPath; // (菜单图标中图片路径【24*24，使用图片作为图标，适用于对H5支持不好的浏览器场景】)
    private String bigImgPath; // (菜单图标大图片路径【32*32，使用图片作为图标，适用于对H5支持不好的浏览器场景】)
    private String hugeImgPath; // (菜单图标巨大图片路径【64*64，使用图片作为图标，适用于对H5支持不好的浏览器场景】)

    private String isMainline; // 是否启用主线 0 不启用 1启用 ，默认启用
    private String mainlineAlias; // 主线别名
    private String isAuxiline; // 是否启用辅线  0 禁用  1 启用，默认启用
    private String auxilineAlias; // 辅线别名
    private String isTechspectline; // 是否启用技术审查线 0 禁用， 1启用，默认禁用
    private String techspectlineAlias; // 技术审查线别名
    private String gjbzsplclx;  // 国家审批流程类型
    private String isOnlineSb; // 是否允许网上申报，0表示不允许，1表示允许，默认是允许
    private String rootOrgId;//根组织ID

    // 扩展字段
    private String verNum;
    private String keyword;
    private String themeVerId;
    private String themeTypeName; // 主题类型名称
    private String themeVerName; // 主题版本定义最新版本
    @ApiModelProperty("主题下的阶段")
    private List<AeaParStage> stageList;//主题下的阶段列表
}
