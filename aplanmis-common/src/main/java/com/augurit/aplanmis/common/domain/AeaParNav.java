package com.augurit.aplanmis.common.domain;

import java.io.Serializable;
import java.util.Date;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import com.augurit.aplanmis.common.constants.ActiveStatus;
import com.augurit.aplanmis.common.constants.DeletedStatus;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

/**
 * 主题导航定义表-模型
 */
@Data
public class AeaParNav implements Serializable {


    private static final long serialVersionUID = 1L;

    // (主键)
    private String navId;

    // (主题导航名称)
    private String navName;

    // (排列顺序号)
    @Size(max = 38)
    private Long sortNo;

    // (备注说明)
    private String navMemo;

    // (是否启用，0表示禁用，1表示启用)
    private String isActive;

    // (是否删除，0表示未删除，1表示已删除)
    private String isDeleted;

    // (创建人)
    private String creater;

    // (创建时间)
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private java.util.Date createTime;

    // (修改人)
    private String modifier;

    // (修改时间)
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private java.util.Date modifyTime;

    private String rootOrgId;

    //扩展字段
    private String keyword;

}
