package com.augurit.aplanmis.common.domain;

import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

@Data
public class AeaParStageItem implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    private String stageItemId;

    /**
     * 阶段定义ID
     */
    private String stageId;

    /**
     * 事项定义ID
     */
    private String itemId;

    /**
     * 事项版本ID
     */
    private String itemVerId;

    /**
     * 创建人
     */
    private String creater;

    /**
     * 创建时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    /**
     * 排序
     */
    private Long sortNo;

    /**
     * 事项类型： 并联审批、并行推进、前置检查事项
     */
    private String isOptionItem;

    /**
     * 智能表单Id
     */
    private String subFormId;

    /**
     * 扩展字段
     */
    private List<AeaItemInout> aeaItemInouts;

    /**
     * 阶段情形ID
     */
    private String parStateId;

    /**
     * 材料ID
     */
    private String matId;

    /**
     * 事项名称
     */
    private String itemName;

    /**
     * 事项编号
     */
    private String itemCode;

    /**
     * 所属组织
     */
    private String orgName;

    /**
     * 查询关键字
     */
    private String keyword;

    /**
     * 是否勾选
     */
    private Boolean isCheck;
    private String bjType;
    private String dueNum;

    /**
     * 扩展字段： 阶段名称
     */
    private String stageName;

    /**
     * 扩展字段： 阶段编码
     */
    private String stageCode;

    /**
     * 扩展字段： 阶段是否主线、辅线
     */
    private String isNode;

    /**
     * 扩展字段： 事项情形版本id
     */
    private String itemStateVerId;

    /**
     * 智能表单名称
     */
    private String formName;
    private String parInId;
    private String rootOrgId;

    /**
     * 是否标准事项  1标准事项 0 实施事项
     */
    private String isCatalog;

    /**
     * 标准事项指导部门
     */
    private String guideOrgName;
    private String inId;
    /**
     * 在线运行图实施事项itemId*itemVerId
     */
//    private String exeItemVerId;

    private String parentItemId;

    private String itemSeq;

    // 主题id
    private String themeId;

    // 主题名称
    private String themeName;

    // 项目id
    private String projInfoId;
}
