package com.augurit.aplanmis.common.domain;

import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.Size;
import java.io.Serializable;

/**
 * 阶段事项表单前置检测表-模型
 */
@Data
public class AeaParFrontItemform implements Serializable {

    private static final long serialVersionUID = 1L;

    private String frontItemformId; // (ID)
    private String stageId; // (阶段ID)
    private String stageItemId; // (阶段事项ID)
    private Long sortNo; // (排序字段)
    private String isActive; // (是否启用，0表示禁用，1表示启用)
    private String frontItemformMemo; // (备注说明)

    private String creater; // (创建人)
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private java.util.Date createTime; // (创建时间)
    private String modifier; // (修改人)
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private java.util.Date modifyTime; // (修改时间)
    private String rootOrgId; // (根组织id)

    //非表字段
    //查询关键字
    private String keyword;

}
