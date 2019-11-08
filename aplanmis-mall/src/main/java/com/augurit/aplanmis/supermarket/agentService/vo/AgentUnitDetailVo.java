package com.augurit.aplanmis.supermarket.agentService.vo;

import com.augurit.aplanmis.common.diyannotation.FiledNameIs;
import lombok.Data;

/**
 * @author xiaohutu
 * 中介详情信息
 */
@Data
public class AgentUnitDetailVo {

    private String unitInfoId;
    @FiledNameIs(filedValue = "项目（法人）单位名称")
    private String applicant;
    @FiledNameIs(filedValue = "法人类型")
    private String idtype;
    /**
     * 证件类型名称
     */
    private String idTypeName;
    @FiledNameIs(filedValue = "法人证件号码")
    private String idcard;
    @FiledNameIs(filedValue = "联系人")
    private String contact;
    @FiledNameIs(filedValue = "联系人电话")
    private String mobile;
    @FiledNameIs(filedValue = "联系人电子邮件")
    private String email;
    @FiledNameIs(filedValue = "法定代表人")
    private String idrepresentative;
    @FiledNameIs(filedValue = "具体地址")
    private String applicantDetailSite;

    private String registeredCapital;
    private String unitNature;
    private String postalCode;  //邮政编码
    @FiledNameIs(filedValue = "社会信用代码")
    private String unifiedSocialCreditCode;
    private String managementScope; // (经营范围)

}
