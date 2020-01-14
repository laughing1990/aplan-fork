package com.augurit.aplanmis.common.vo.agency;

import com.augurit.agcloud.framework.util.StringUtils;
import com.augurit.aplanmis.common.domain.AeaUnitInfo;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class SplitProjFromVo {

    //项目信息
    @ApiModelProperty(value = "项目ID")
    private String parentProjInfoId;

    @ApiModelProperty("项目代码")
    private String localCode;

    @ApiModelProperty("项目名称")
    private String projName;

    @ApiModelProperty("项目总投资（万元）")
    private Double investSum;

    //单项工程信息
    @ApiModelProperty("所处阶段（1 工程建设许可阶段  2 施工许可阶段）")
    private String stageNo;

    @ApiModelProperty("前阶段关联工程代码")
    private String lastGcbm;

    @ApiModelProperty("单项工程名称")
    private String childProjName;

    @ApiModelProperty("工程范围")
    private String childForeignManagement;

    @ApiModelProperty("单项工程投资额（万元）")
    private Double childProjInvestSum;

    @ApiModelProperty("单项工程建设规模")
    private String childScaleContent;

    @ApiModelProperty("用地面积")
    private Double childXmYdmj;

    @ApiModelProperty("建筑面积(m2)")
    private Double childBuildAreaSum;

    //申报单位信息
    @ApiModelProperty("单位ID")
    private String unitInfoId;

    @ApiModelProperty("单位类型。以项目单位关系表为准")
    private String unitType;

    @ApiModelProperty("单位名称")
    private String unitName;

    @ApiModelProperty("证件类型（1组织机构代码、2统一社会信用代码、3纳税人识别号）")
    private String certType;

    @ApiModelProperty("证件编号")
    private String certCode;

    @ApiModelProperty("法人姓名")
    private String idrepresentative;

    @ApiModelProperty("法人手机号码")
    private String idmobile;

    public static SplitProjFromVo setUnitInfo(SplitProjFromVo vo, AeaUnitInfo unitInfo){
        if(vo != null && unitInfo != null){
            vo.setUnitInfoId(unitInfo.getUnitInfoId());
            vo.setUnitType(unitInfo.getUnitProjUnitType());
            vo.setUnitName(unitInfo.getApplicant());
            vo.setIdrepresentative(unitInfo.getIdrepresentative());
            vo.setIdmobile(unitInfo.getIdmobile());
            if(StringUtils.isNotBlank(unitInfo.getOrganizationalCode())){
                vo.setCertType("1");
                vo.setCertCode(unitInfo.getOrganizationalCode());
            }else if(StringUtils.isNotBlank(unitInfo.getUnifiedSocialCreditCode())){
                vo.setCertType("2");
                vo.setCertCode(unitInfo.getUnifiedSocialCreditCode());
            }else if(StringUtils.isNotBlank(unitInfo.getTaxpayerNum())){
                vo.setCertType("3");
                vo.setCertCode(unitInfo.getTaxpayerNum());
            }
        }
        return vo;
    }
}
