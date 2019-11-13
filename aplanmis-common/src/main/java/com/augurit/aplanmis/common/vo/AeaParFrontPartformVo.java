package com.augurit.aplanmis.common.vo;

import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.Size;
import java.io.Serializable;

/**
 * @author tiantian
 * @date 2019/11/7
 */
@Data
public class AeaParFrontPartformVo implements Serializable {
    private static final long serialVersionUID = 1L;
    private String frontPartformId; // (ID)
    private String stageId; // (阶段ID)
    private String stagePartformId; // (阶段扩展表单ID)
    @Size(max = 10)
    private Long sortNo; // (排序字段)
    private String isActive; // ()
    private String frontPartformMemo; // ()
    private String creater; // (创建人)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private java.util.Date createTime; // (创建时间)
    private String modifier; // ()
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private java.util.Date modifyTime; // ()
    private String rootOrgId; // (根组织id)

    private String isSmartForm;//是否智能表单
    private String partformName;//阶段扩展表单名称
    private String partformId;//智能表单定义ID
    private String delformId;//开发表单定义ID

}
