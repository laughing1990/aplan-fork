package com.augurit.aplanmis.common.domain;

import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;

/**
 * 设立依据-模型
 */
@Data
public class AeaItemServiceBasic extends AeaServiceLegalClause implements Serializable {

    private static final long serialVersionUID = 1L;

    private String basicId; // (设立依据ID)
    private String tableName; // (业务表名（大写）)
    private String pkName; // (业务表主键ID（大写）)
    private String recordId; // (业务记录数据)
    private String legalClauseId; // (法律条款ID)
    private String creater; // (创建人)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private java.util.Date createTime; // (创建时间)
    private String modifier; // (修改人)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private java.util.Date modifyTime; // (修改时间)
    private String rootOrgId;//根组织ID

    //非表字段
    private String clauseTitle;//依据题目
    private String clauseContent;//依据内容
    private String legalName;//依据名称
    private Date exeDate;//实施日期

}
