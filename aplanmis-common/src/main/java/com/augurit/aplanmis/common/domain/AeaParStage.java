package com.augurit.aplanmis.common.domain;

import com.augurit.aplanmis.common.constants.NeedStateStatus;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.List;

/**
 * 阶段/环节定义表-模型
 */
@Data
@ApiModel("阶段")
public class AeaParStage implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "阶段ID")
    private String stageId;

    @ApiModelProperty(value = "阶段名称")
    private String stageName;

    @ApiModelProperty(value = "主题版本定义ID")
    private String themeVerId;

    @ApiModelProperty(value = "业务流程模板定义ID")
    private String appId;

    @ApiModelProperty(value = "承诺办结时限")
    private Double dueNum;

    @ApiModelProperty(value = "承诺办结时限单位")
    private String dueUnit;

    @ApiModelProperty(value = "排序号，从1， 2， 3， 4...")
    @Size(max = 10)
    private Long sortNo;

    @ApiModelProperty(value = "图标CSS样式")
    private String iconCss;

    @ApiModelProperty(value = "背景CSS样式")
    private String bgCss;

    @ApiModelProperty(value = "备注说明")
    private String stageMemo;

    @ApiModelProperty(value = "创建人")
    private String creater;

    @ApiModelProperty(value = "创建时间")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private java.util.Date createTime;

    @ApiModelProperty(value = "修改人")
    private String modifier;

    @ApiModelProperty(value = "修改时间")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private java.util.Date modifyTime;

    @ApiModelProperty(value = "是否删除，0表示未删除，1表示已删除")
    private String isDeleted;

    @ApiModelProperty(value = "1表示主线，2表示辅线 3技术审查")
    private String isNode;

    @ApiModelProperty(value = "阶段/环节状态检测URL")
    private String statusCheckUrl;

    @ApiModelProperty(value = "阶段编号")
    private String stageCode;

    @ApiModelProperty(value = "是否重建情形，0表示直接合并关联事项情形，1表示重建阶段情形不使用关联事项情形")
    private String isNeedState;

    @ApiModelProperty(value = "菜单图标小图片路径(16*16)")
    private String smallImgPath;

    @ApiModelProperty(value = "菜单图标中图片路径(24*24)")
    private String middleImgPath;

    @ApiModelProperty(value = "菜单图标大图片路径(32*32)")
    private String bigImgPath;

    @ApiModelProperty(value = "菜单图标巨大图片路径(64*64)")
    private String hugeImgPath;

    @ApiModelProperty(value = "是否使用图片图标")
    private String isImgIcon;

    @ApiModelProperty(value = "是否允许可选事项")
    private String isOptionItem;

    @ApiModelProperty(value = "申报时是否允许窗口人员选择必选事项")
    private String isSelItem;

    @ApiModelProperty(value = "关联主线ID")
    private String parentId;

    @ApiModelProperty(value = "申报时必选事项显示数量")
    @Size(max = 10)
    private Long noptItemShowNum;

    @ApiModelProperty(value = "申报时可选事项显示数量")
    @Size(max = 10)
    private Long optItemShowNum;

    @ApiModelProperty(value = "是否启用EL表达式")
    private String useEl;

    @ApiModelProperty(value = "条件表达式")
    private String stageEl;

    @ApiModelProperty(value = "是否允许设置前置检查事项")
    private String isCheckItem;

    @ApiModelProperty(value = "1:属于该审批阶段的所有里程碑事项办结，该审批阶段才算办结;2:属于该审批阶段的任一项里程碑事项办结，该审批阶段就算办结")
    private String lcbsxlx;

    @ApiModelProperty(value = "1 立项用地规划许可 2 立项用地规划许可 3 施工许可 4 竣工验收 5 并行推进")
    private String dybzspjdxh;

    @ApiModelProperty(value = "处理方式")
    private String handWay;

    @ApiModelProperty(value = "是否启用一张表单 0 禁用 1允许'")
    private String useOneForm;

    @ApiModelProperty(value = "根组织ID'")
    private String rootOrgId;

    @ApiModelProperty(value = "控制在线运行图是否展示事项,1 展示 0 隐藏,主线展示,辅线默认不展示但可控制'")
    private String isShowItem;

    @ApiModelProperty(value = "对应国家标准辅线服务,单选, 多评合一（51）、方案联审（52）、联合审图（53）、联合测绘（54C）、联合验收（54Y）'")
    private String dygjbzfxfw;

    @ApiModelProperty(value = "是否前置检测阶段事项表单  0 禁止  1允许'")
    private java.lang.String isCheckItemform;

    @ApiModelProperty(value = "是否前置检测阶段事项扩展表单  0 禁止  1允许'")
    private java.lang.String isCheckPartform;

    @ApiModelProperty(value = "是否前置检测项目信息  0 禁止  1允许'")
    private java.lang.String isCheckProj;

    @ApiModelProperty(value = "是否前置检测阶段信息  0 禁止  1允许'")
    private java.lang.String isCheckStage;

    @ApiModelProperty(value = "法定办结时限")
    private Double anticipateDay;

    @ApiModelProperty(value = "法定办结时限单位")
    private String anticipateType;


    /**
     * 扩展字段: 关键字查询
     */
    private String keyword;

    /**
     * 主题定义ID
     */
    private String themeId;

    /**
     * 扩展字段: 主题名称
     */
    private String themeName;

    /**
     * 扩展字段: 主题编码
     */
    private String themeCode;

    /**
     * 阶段是否已经进行 0否1是
     */
    private String isDoing;

    /**
     * 已进行的阶段实例id
     */
    private String stageinstId;

    private List<AeaItem> aeaItems;

    public boolean needState() {
        return NeedStateStatus.NEED_STATE.getValue().equals(this.isNeedState);
    }

    /**
     * 申请实例状态
     */
    private String applyinstStatus;

    /**
     * 申请实例状态码
     */
    private String applyinstStatusCode;

    /**
     * 并联事项定义列表
     */
    @ApiModelProperty("并联事项定义列表")
    private List<AeaItemBasic> parallelAeaItemBasicList;

    /**
     * 并行事项定义列表
     */
    @ApiModelProperty("并行事项定义列表")
    private List<AeaItemBasic> seriAeaItemBasicList;

}
