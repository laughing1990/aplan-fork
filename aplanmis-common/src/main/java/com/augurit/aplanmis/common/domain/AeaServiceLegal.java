package com.augurit.aplanmis.common.domain;

import com.augurit.agcloud.bsc.domain.BscAttForm;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * 法律法规-模型
 * @author jjt
 * @date 2016/10/31
 *
 */
@Data
public class AeaServiceLegal implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 法律法规ID
     */
    private String legalId;

    /**
     * 法律法规名称
     */
    private String legalName;

    /**
     * 法律法规层级
     */
    private String legalLevel;

    /**
     * 依据文号
     */
    private String basicNo;

    /**
     * 颁发机构
     */
    private String issueOrg;

    /**
     * 法律法规开始实施的日期
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private java.util.Date exeDate;

    /**
     * 修订法律法规的日期
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private java.util.Date updateDate;

    /**
     * 法律法规附件
     */
    private String serviceLegalAtt;

    /**
     * 附件数量
     */
    private Long serviceLegalAttCount;

    /**
     * 备注说明
     */
    private String serviceLegalMemo;

    /**
     * 父ID
     */
    private String parentLegalId;

    /**
     * 序列
     */
    private String legalSeq;

    /**
     * 创建人
     */
    private String creater;

    /**
     * 创建时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private java.util.Date createTime;

    /**
     * 修改人
     */
    private String modifier;

    /**
     * 修改时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private java.util.Date modifyTime;

    /**
     * 根组织ID
     */
    private String rootOrgId;

    /**
     * 法律法规附件
     */
    private List<BscAttForm> legalAtts = new ArrayList<>();

    /**
     * 子集条款
     */
    private List<AeaServiceLegalClause> legalClauses = new ArrayList<>();
}
