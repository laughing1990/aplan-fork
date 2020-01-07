package com.augurit.aplanmis.common.vo;

import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;

@Data
public class AeaCertiVo implements Serializable {
   // * 建设项目用地规划许可证-模型
    private String projInfoId; // (项目ID)

    private String certLandId;
    private String certLandCode; // (用地规划许可证编号)
    private String landNature; // (用地性质，来自于数据字典)
    private String landAreaValue; // (用地面积数值)
    private String landAreaUnit; // (用地面积单位)
    private String govOrgCodeLand; // (发证机关机构代码)
    private String govOrgNameLand; // (发证机关)
    @DateTimeFormat(pattern="yyyy-MM-dd")
    private java.util.Date publishTimeLand; // (发证日期)

//* 建设工程规划许可证-模型
  //  private String projInfoId; // (项目ID)
    private String  certProjectId;
    private String certProjectCode; // (建设工程规划许可证编号)
    private String publishOrgCodeProject; // (核发机关组织机构代码)
    private String publishOrgNameProject; // (核发机关)
    @DateTimeFormat(pattern="yyyy-MM-dd")
    private java.util.Date publishTimeProject; // (核发日期)

    /**
     * 建设项目选址意见书-模型
     */

  //  private String projInfoId; // (项目ID)
    private String siteId;
    private String siteCode; // (建设项目选址意见书批文号)
    private String landAreaValueSite; // (拟用地面积数值)
    private String landAreaUnitSite; // (拟用地面积单位)
    private String constructionSize; // (拟建设规模)
    private String govOrgCodeSite; // (核发机关组织机构代码)
    private String govOrgNameSite; // (核发机关)
    @DateTimeFormat(pattern="yyyy-MM-dd")
    private java.util.Date publishTimeSite; // (核发日期)

    //扩展字段
    private String formId;
    private String refEntityId;  //申请实例ID

}
