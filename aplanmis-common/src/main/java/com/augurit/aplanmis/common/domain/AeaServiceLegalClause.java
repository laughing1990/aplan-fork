package com.augurit.aplanmis.common.domain;

import com.augurit.agcloud.bsc.domain.BscAttForm;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 法律法规条款-模型
 *
 * @author jjt
 * @date 2016/10/31
 *
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class AeaServiceLegalClause extends AeaServiceLegal implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 法律法规条款ID
     */
    private String legalClauseId;

    /**
     * 法律法规ID
     */
    private String legalId;

    /**
     * 条款号
     */
    private String clauseTitle;

    /**
     * 条款具体内容
     */
    private String clauseContent;

    /**
     * 条款附件
     */
    private String clauseAtt;

    /**
     * 附件数量
     */
    private Long clauseAttCount;

    /**
     * 排序序号
     */
    private Long sortNo;

    /**
     * 是否有效（0 禁用 1启用）
     */
    private String isActive;

    /**
     * 是否删除（0 不删除 1 删除）
     */
    private String isDeleted;

    /**
     * 查询关键词
     */
    private String keyword;

    /**
     * 查询ID数组
     */
    private String[] legalClauseIdArr;

    /**
     * 是否check
     */
    private Boolean isCheck = false;

    /**
     * 事项id
     */
    private String itemId;

    /**
     * 根组织ID
     */
    private String rootOrgId;

    /**
     * 设立依据ID
     * **/
    private String basicId;

    /**
     * 业务表名
     * **/
    private String tableName;

    /**
     * 业务表主键ID
     * **/
    private String pkName;

    /***
     * 业务记录数据
     * */
    private String recordId;

    /**
     * 法律法规条款附件
     */
    private List<BscAttForm> legalClauseAtts = new ArrayList<>();
}
