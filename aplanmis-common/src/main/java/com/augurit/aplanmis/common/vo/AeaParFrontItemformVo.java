package com.augurit.aplanmis.common.vo;

import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.Size;
import java.io.Serializable;

/**
 * @author tiantian
 * @date 2019/11/6
 */
@Data
public class AeaParFrontItemformVo implements Serializable {
    private static final long serialVersionUID = 1L;
    private String frontItemformId; // (ID)
    private String stageId; // (阶段ID)
    private String stageItemId; // (阶段事项ID)
    @Size(max = 10)
    private Long sortNo; // (排序字段)
    private String isActive; // (是否启用，0表示禁用，1表示启用)
    private String frontItemformMemo; // (备注说明)
    private String creater; // (创建人)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private java.util.Date createTime; // (创建时间)
    private String modifier; // (修改人)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private java.util.Date modifyTime; // (修改时间)
    private String rootOrgId; // (根组织id)

    //表单事项名称
    private String itemName;
    //表单名称
    private String formName;
    //表单ID
    private String subFormId;
}
