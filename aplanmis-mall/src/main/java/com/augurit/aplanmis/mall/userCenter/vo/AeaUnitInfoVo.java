package com.augurit.aplanmis.mall.userCenter.vo;


import com.augurit.aplanmis.common.domain.AeaUnitInfo;
import com.augurit.aplanmis.common.utils.DesensitizedUtil;
import com.augurit.aplanmis.common.vo.LinkmanTypeVo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Value;

import java.util.List;

@Data
@ApiModel("单位信息")
public class AeaUnitInfoVo {
    @Value("${mall.check.authority:false}")
    private static boolean isCheckAuthority;
    private java.lang.String unitInfoId; // (主键)
    @ApiModelProperty("单位名称")
    private java.lang.String applicant; // (项目（法人）单位名称)
    @ApiModelProperty(value = "统一社会信用代码", dataType = "string")
    private String unifiedSocialCreditCode;
    @ApiModelProperty(value = "工商登记号", dataType = "string")
    private String induCommRegNum;
    @ApiModelProperty(value = "组织机构代码", dataType = "string")
    private String organizationalCode;
    @ApiModelProperty(value = "行政区（园区）", dataType = "string")
    private java.lang.String applicantDistrict; // (行政区（园区）)
    @ApiModelProperty(value = "注册资本", dataType = "string")
    private java.lang.String registeredCapital; // (注册资本)
    @ApiModelProperty(value = "注册登记机关", dataType = "string")
    private java.lang.String registerAuthority; // (注册登记机关)
    @ApiModelProperty(value = "邮政编码", dataType = "string")
    private java.lang.String postalCode; // (邮政编码)
    @ApiModelProperty(value = "纳税人识别号", dataType = "string")
    private String taxpayerNum;
    @ApiModelProperty(value = "信用标记码", dataType = "string")
    private String creditFlagNum;
    @ApiModelProperty(value = "法人姓名", dataType = "string")
    private java.lang.String idrepresentative; // (法人姓名)
    @ApiModelProperty(value = "具体地址", dataType = "string")
    private java.lang.String applicantDetailSite; // (具体地址)
    @ApiModelProperty(value = "经营范围", dataType = "string")
    private java.lang.String managementScope; // (经营范围)
    @ApiModelProperty(value = "单位登录名", dataType = "string")
    private java.lang.String loginName; // (单位登录名)
    @ApiModelProperty(value = "法人身份证号码", dataType = "string")
    private java.lang.String idno; // (法人身份证号码)
    @ApiModelProperty(value = "人员设置列表", dataType = "list")
    private List<LinkmanTypeVo> linkmanTypes;

    public static AeaUnitInfoVo build(AeaUnitInfo aeaUnitInfo) {
        AeaUnitInfoVo aeaUnitInfoVo = new AeaUnitInfoVo();
        BeanUtils.copyProperties(aeaUnitInfo, aeaUnitInfoVo);
        if (isCheckAuthority){
            aeaUnitInfoVo.setIdno(DesensitizedUtil.desensitizedIdNumber(aeaUnitInfoVo.getIdno()));
        }
        return aeaUnitInfoVo;
    }
}
