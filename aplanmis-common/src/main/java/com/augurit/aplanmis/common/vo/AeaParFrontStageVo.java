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
public class AeaParFrontStageVo implements Serializable {
    private static final long serialVersionUID = 1L;
    private String frontStageId; // (ID)
    private String stageId; // (阶段ID)
    private String histStageId; // (前置阶段ID)
    @Size(max = 10)
    private Long sortNo; // (排序字段)
    private String isActive; // (是否启用，0表示禁用，1表示启用)
    private String frontStageMemo; // ()
    private String creater; // (创建人)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private java.util.Date createTime; // (创建时间)
    private String modifier; // (修改人)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private java.util.Date modifyTime; // (修改时间)
    private String rootOrgId; // (根组织id)

    private String histStageName;//前置阶段名称
}
