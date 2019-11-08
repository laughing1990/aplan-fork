package com.augurit.aplanmis.common.domain;

import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

/**
 * @author xiaohutu
 */
@Data
public class AeaImServiceItem {
    /**主键*/
    private String serviceItemId;
    /**(关联aea_item_ver表id)*/
    private String itemVerId;
    /**
     * 服务ID
     */
    private String serviceId;

    /**
     * 是否删除：1 已经删，0 未删除
     */
    private String isDelete;

    private String creater;

    @DateTimeFormat(pattern="yyyy-MM-dd")
    private LocalDateTime createTime;

    /**
     * 单位要求信息ID
     */
    private String unitRequireId;


    //扩展字段

    /** (事项内容ID，主键ID)*/
    private String itemBasicId;
    /** (关联aea_item表id)*/
    private String itemId;
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
    /**服务内容*/
    private String serviceContent;
    /**服务结果*/
    private String serviceResult;
    /**服务时限说明*/
    private String timeLimitExplain;
    /**服务时限要求*/
    private String serviceTimeLimit;
    /**价格管理方式*/
    private String priceManagement;
    /**设立层级*/
    private String slcj;
    /**备注*/
    private String memo;
    /**其他要求说明*/
    private String otherRequireExplain;
    /**是否需要执业/职业人员要求：1 需要，0 不需要*/
    private String isRegisterRequire;
    /**执业/职业人员总数（当IS_REGISTER_REQUIRE =1 时，必填）*/
    private String registerTotal;
    /**执业/职业人员要求（当IS_REGISTER_REQUIRE =1 时，必填）*/
    private String registerRequire;
    /**是否需要备案要求：1 需要，0 不需要*/
    private String isRecordRequire;
    /**备案要求说明（当IS_RECORD_REQUIRE =1 时，必填）*/
    private String recordRequireExplain;
    /**是否需要资质要求：1 需要，0 不需要*/
    private String isQualRequire;
    /**资质要求：1 多个资质子项符合其一即可，0 需同时符合所有选中资质子项*/
    private String qualRequireType;
    /**资质要求说明（当IS_QUAL_REQUIRE =1 时，必填）*/
    private String qualRequireExplain;
    /**资质备案说明*/
    private String qualRecordRequire;
    /**是否仅承诺服务：1 是，0 否*/
    private String promiseService;
    /**是否为财政资金（资金来源）：1 是，0 不是*/
    private String isFinancialFund;
    /**是否为社会资金（资金来源）：1 是，0 不是*/
    private String isSocialFund;

}
