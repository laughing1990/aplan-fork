package com.augurit.aplanmis.common.domain;

import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.Size;
import java.io.Serializable;

/**
 * 阶段的前置阶段检测表-模型
 */
@Data
public class AeaParFrontStage implements Serializable {

    private static final long serialVersionUID = 1L;

    private String frontStageId; // (ID)
    private String stageId; // (阶段ID)
    private String histStageId; // (前置阶段ID)
    private Long sortNo; // (排序字段)
    private String isActive; // (是否启用，0表示禁用，1表示启用)
    private String frontStageMemo; // ()

    private String creater; // (创建人)
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private java.util.Date createTime; // (创建时间)
    private String modifier; // (修改人)
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private java.util.Date modifyTime; // (修改时间)

    private String rootOrgId; // (根组织id)

    /**
     * 扩展字段: 查询关键字
     */
    private String keyword;

    /**
     * 扩展字段: 阶段名称
     */
    private String histStageName;

    /**
     * 扩展字段: 阶段编号
     */
    private String histStageCode;

    /**
     * 扩展字段: 是否主线
     */
    private String histIsNode;

}
