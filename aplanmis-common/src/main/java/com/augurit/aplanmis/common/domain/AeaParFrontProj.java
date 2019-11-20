package com.augurit.aplanmis.common.domain;

import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.Size;
import java.io.Serializable;

/**
 * 阶段的项目信息前置检测-模型
 */
@Data
public class AeaParFrontProj implements Serializable {

    private static final long serialVersionUID = 1L;

    private String frontProjId; // (主键)
    private String stageId; // (阶段ID)
    private String ruleName; // (规则名称)
    private String ruleEl; // (条件表达式，XM_YDMJ>1000，启用EL后以表达式为主)
    @Size(max = 38)
    private Long sortNo; // (排列顺序号)
    private String isActive; // (是否启用，0表示禁用，1表示启用)
    private String frontProjMemo; // ()
    private String creater; // (创建人)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private java.util.Date createTime; // (创建时间)
    private String modifier; // (修改人)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private java.util.Date modifyTime; // (修改时间)
    private String rootOrgId; // (根组织ID)

    //非表字段
    private String keyword;//查询关键字
}
