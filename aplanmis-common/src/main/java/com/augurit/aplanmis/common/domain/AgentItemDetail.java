package com.augurit.aplanmis.common.domain;

import lombok.Data;

@Data
public class AgentItemDetail {
    /** (事项内容ID，主键ID)*/
    private String itemBasicId;
    /** (关联aea_item表id)*/
    private String itemId;
    /**(关联aea_item_ver表id)*/
    private String itemVerId;
    /** (事项编号)*/
    private String itemCode;
    /** (事项名称)*/
    private String itemName;
    /** (组织ID)*/
    private String orgId;
    /**中介事项部门*/
    private String orgName;
    /**中介事项所属服务*/
    private String serviceName;

    private String qualName;
    private String qualRequireExplain;
    private String isQualRequire;
    private String isFinancialFund;
    private String isSocialFund;
    private String serviceContent;
    private String serviceResult;
    private String timeLimitExplain;
    private String serviceTimeLimit;
    private String priceManagement;
    private String slcj;
    private String memo;

}
