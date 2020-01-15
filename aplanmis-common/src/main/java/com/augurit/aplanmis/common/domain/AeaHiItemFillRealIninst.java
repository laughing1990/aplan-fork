package com.augurit.aplanmis.common.domain;

import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.Size;
import java.io.Serializable;

/**
 * 事项容缺实际补齐材料实例表-模型
 */
@Data
public class AeaHiItemFillRealIninst implements Serializable {

    private static final long serialVersionUID = 1L;
    private String realIninstId; // (ID)
    private String fillId; // (补齐ID)
    private String dueIninstId; // (要求补齐输入ID)
    private String inoutinstId; // (输入输出实例ID)
    private String isPass; // (是否通过，0表示未通过，1表示通过)
    @Size(max = 10)
    private Long attCount; // (附件总数)
    @Size(max = 10)
    private Long realPaperCount; // (实际纸质数)
    @Size(max = 10)
    private Long realCopyCount; // (实际复印件数)
    private String creater; // (创建人)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private java.util.Date createTime; // (创建时间)
    private String modifier; // (修改人)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private java.util.Date modifyTime; // (修改时间)
    private String rootOrgId; // (根组织ID)
}
