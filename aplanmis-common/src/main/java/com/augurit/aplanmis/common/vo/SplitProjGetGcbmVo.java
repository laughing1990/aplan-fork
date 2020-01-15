package com.augurit.aplanmis.common.vo;

import com.alibaba.fastjson.JSONObject;
import com.augurit.aplanmis.common.vo.agency.SplitProjFromVo;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.util.Assert;

import java.util.Arrays;
import java.util.List;

/**
 * 调用省发改接口获取工程编码使用
 */
@Data
public class SplitProjGetGcbmVo {

    @ApiModelProperty("项目代码（全国投资项目统一代码）")
    private String XMDM;

    @ApiModelProperty("所处阶段（1 工程建设许可阶段  2 施工许可阶段）")
    private String SCJD;

    @ApiModelProperty("前阶段关联工程代码")
    private String QJDGCDM;

    @ApiModelProperty("单项工程名称")
    private String XMMC;

    @ApiModelProperty("工程范围")
    private String GCFW;

    @ApiModelProperty("单项工程投资额")
    private Double ZTZE;

    @ApiModelProperty("单项工程建设规模")
    private String JSGMJNR;

    @ApiModelProperty("用地面积（平方米）")
    private Double YDMJ;

    @ApiModelProperty("建筑面积（平方米）")
    private Double JZMJ;

    @ApiModelProperty("申报单位类型 01建设单位，02施工单位, 03勘察单位，04设计单位， 05监理单位，06代建单位，07其他")
    private String DWLX;

    @ApiModelProperty("项目单位名称")
    private String DWMC;

    @ApiModelProperty("证件类型 A05100企业营业执照(工商注册号)，A05200组织机构代码证，A05300统一社会信用代码，A05900其他")
    private String ZJLX;

    @ApiModelProperty("证件编号")
    private String ZJBH;

    @ApiModelProperty("法人姓名")
    private String FRXM;

    @ApiModelProperty("联系电话")
    private String LXDH;

    @ApiModelProperty("部门名称")
    private String BMMC;

    @ApiModelProperty("部门编码")
    private String BMBM;

    @ApiModelProperty("对应中央平台部门代码")
    private String DYZYPTBMDM;

    @ApiModelProperty("申请人姓名")
    private String SQRXM;

    @ApiModelProperty("申请人在统一身份认证系统的uid")
    private String SQRUID;

    public static SplitProjGetGcbmVo covertSplitProjGetGcbmVo(SplitProjFromVo splitProjFromVo) throws Exception{
        Assert.notNull(splitProjFromVo,"参数splitProjFromVo不能为空");
        String[] dwlx = {"0","1","2","3","4","5","6","7"};
        List<String> unitTypeList = Arrays.asList(dwlx);
        SplitProjGetGcbmVo vo = new SplitProjGetGcbmVo();
        vo.setXMDM(splitProjFromVo.getLocalCode());
        vo.setSCJD(splitProjFromVo.getStageNo());
        vo.setQJDGCDM(splitProjFromVo.getLastGcbm());
        vo.setXMMC(splitProjFromVo.getChildProjName());
        vo.setGCFW(splitProjFromVo.getChildForeignManagement());
        vo.setZTZE(splitProjFromVo.getChildProjInvestSum());
        vo.setJSGMJNR(splitProjFromVo.getChildScaleContent());
        vo.setYDMJ(splitProjFromVo.getChildXmYdmj());
        vo.setJZMJ(splitProjFromVo.getChildBuildAreaSum());
        String unitType = splitProjFromVo.getUnitType();
        vo.setDWLX((unitTypeList.contains(unitType))?"0"+unitType:"07");
        vo.setDWMC(splitProjFromVo.getUnitName());
        String certType = splitProjFromVo.getCertType();
        vo.setZJLX("1".equals(certType)?"A05200":"2".equals(certType)?"A05300":"A05900");
        vo.setZJBH(splitProjFromVo.getCertCode());
        vo.setFRXM(splitProjFromVo.getIdrepresentative());
        vo.setLXDH(splitProjFromVo.getIdmobile());
        return vo;
    }

    public JSONObject covertJSONObject(SplitProjGetGcbmVo splitProjGetGcbmVo){
        Assert.notNull(splitProjGetGcbmVo,"参数splitProjGetGcbmVo不能为空");
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("SCJD", splitProjGetGcbmVo.getSCJD());
        jsonObject.put("GCFW", splitProjGetGcbmVo.getGCFW());
        jsonObject.put("XMMC", splitProjGetGcbmVo.getXMMC());
        jsonObject.put("ZTZE", splitProjGetGcbmVo.getZTZE());
        jsonObject.put("JSGMJNR", splitProjGetGcbmVo.getJSGMJNR());
        jsonObject.put("YDMJ", splitProjGetGcbmVo.getYDMJ());
        jsonObject.put("JZMJ", splitProjGetGcbmVo.getJZMJ());
        jsonObject.put("QJDGCDM", splitProjGetGcbmVo.getQJDGCDM());
        jsonObject.put("DWLX", splitProjGetGcbmVo.getDWLX());
        jsonObject.put("DWMC", splitProjGetGcbmVo.getDWMC());
        jsonObject.put("ZJLX", splitProjGetGcbmVo.getZJLX());
        jsonObject.put("ZJBH", splitProjGetGcbmVo.getZJBH());
        jsonObject.put("FRXM", splitProjGetGcbmVo.getFRXM());
        jsonObject.put("LXDH", splitProjGetGcbmVo.getLXDH());

        //TODO 这几个参数待处理
        jsonObject.put("BMMC", "");
        jsonObject.put("BMBM", "");
        jsonObject.put("DYZYPTBMDM", "");
        jsonObject.put("SPBMDM", "");
        jsonObject.put("SQRXM", "");
        jsonObject.put("SQRUID", "");
        return jsonObject;
    }

}
