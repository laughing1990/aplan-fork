package com.augurit.aplanmis.common.domain;

import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;

/**
 * @author jjt
 * @date 2019/4/16
 *
 * 情形与事项关联定义表
 */
@Data
public class AeaParStateItem implements Serializable {

    private static final long serialVersionUID = 1L;

    private String stateItemId; // (ID)
    private String parStateId; // (情形定义ID)
    private String stageItemId; // (阶段与事项关联ID)

    private String creater; // (创建人)
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime; // (创建时间)
    private Long sortNo; // (排序字段)
    private String rootOrgId;

    // 扩展字段
    private String itemVerId;
    private String itemName;
    private String itemCode;
    private String orgName;

    /**
     * 是否标准事项  1标准事项 0 实施事项
     */
    private String isCatalog;
}

