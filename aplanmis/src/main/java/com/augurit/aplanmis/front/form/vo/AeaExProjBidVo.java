package com.augurit.aplanmis.front.form.vo;

import com.augurit.agcloud.bsc.util.UuidUtil;
import com.augurit.agcloud.framework.security.SecurityContext;
import com.augurit.agcloud.framework.util.StringUtils;
import com.augurit.aplanmis.common.domain.AeaExProjBid;
import com.augurit.aplanmis.common.domain.AeaUnitInfo;
import com.augurit.aplanmis.common.domain.AeaUnitProj;
import org.springframework.util.Assert;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
* 招投标信息-模型
<ul>
    <li>项目名：奥格erp3.0--第一期建设项目</li>
    <li>版本信息：v1.0</li>
    <li>日期：2019-10-31 15:56:12</li>
    <li>版权所有(C)2016广州奥格智能科技有限公司-版权所有</li>
    <li>创建人:Administrator</li>
    <li>创建时间：2019-10-31 15:56:12</li>
    <li>修改人1：</li>
    <li>修改时间1：</li>
    <li>修改内容1：</li>
</ul>
*/
public class AeaExProjBidVo extends AeaExProjBid implements Serializable{
// ----------------------------------------------------- Properties

    private static final long serialVersionUID = 1L;
    private List<AeaUnitInfo> winBidUnits;//中标单位
    private List<AeaUnitInfo> agencyUnits;//招标代理机构
    private List<AeaUnitInfo> costUnits;//造价咨询单位

    public static AeaExProjBidVo from(AeaExProjBid aeaExProjBid) {
        AeaExProjBidVo vo = new AeaExProjBidVo();
        vo.setBidId(aeaExProjBid.getBidId());
        vo.setProvinceProjCode(aeaExProjBid.getProvinceProjCode());
        vo.setProjInfoId(aeaExProjBid.getProjInfoId());
        vo.setWinBidNoticeCode(aeaExProjBid.getWinBidNoticeCode());
        vo.setBidSectionName(aeaExProjBid.getBidSectionName());
        vo.setBidSectionAddr(aeaExProjBid.getBidSectionAddr());
        vo.setUnitAddr(aeaExProjBid.getUnitAddr());
        vo.setBidType(aeaExProjBid.getBidType());
        vo.setBidMode(aeaExProjBid.getBidMode());
        vo.setBidSectionMoney(aeaExProjBid.getBidSectionMoney());
        vo.setBidSectionArea(aeaExProjBid.getBidSectionArea());
        vo.setBidSectionSpan(aeaExProjBid.getBidSectionSpan());
        vo.setBidSectionLength(aeaExProjBid.getBidSectionLength());
        vo.setWinBidTime(aeaExProjBid.getWinBidTime());
        vo.setWinBidMoney(aeaExProjBid.getWinBidMoney());
        vo.setBidConfirmTime(aeaExProjBid.getBidConfirmTime());
        vo.setGovOrgCode(aeaExProjBid.getGovOrgCode());
        vo.setGovOrgName(aeaExProjBid.getGovOrgName());
        vo.setGovAreaCode(aeaExProjBid.getGovAreaCode());
        vo.setBidMemo(aeaExProjBid.getBidMemo());
        vo.setCreater(aeaExProjBid.getCreater());
        vo.setCreateTime(aeaExProjBid.getCreateTime());
        vo.setModifier(aeaExProjBid.getModifier());
        vo.setModifyTime(aeaExProjBid.getModifyTime());
        vo.setRootOrgId(aeaExProjBid.getRootOrgId());
        vo.setWinBidUnits(new ArrayList<AeaUnitInfo>());
        vo.setAgencyUnits(new ArrayList<AeaUnitInfo>());
        vo.setCostUnits(new ArrayList<AeaUnitInfo>());
        return vo;
    }

    public AeaUnitProj toAeaUnitProj(String unitInfoId, String unitType) {
        Assert.isTrue(StringUtils.isNotBlank(unitInfoId), "unitInfoId is null");
            AeaUnitProj aeaUnitProj = new AeaUnitProj();
            aeaUnitProj.setIsOwner("0");
            aeaUnitProj.setProjInfoId(this.getProjInfoId());
            aeaUnitProj.setUnitInfoId(unitInfoId);
            aeaUnitProj.setUnitProjId(UuidUtil.generateUuid());
            aeaUnitProj.setUnitType(unitType);
            aeaUnitProj.setCreater(SecurityContext.getCurrentUserId());
            aeaUnitProj.setCreateTime(new Date());
            aeaUnitProj.setIsDeleted("0");
            return aeaUnitProj;
    }
    public List<AeaUnitInfo> getWinBidUnits() {
        return winBidUnits;
    }

    public void setWinBidUnits(List<AeaUnitInfo> winBidUnits) {
        this.winBidUnits = winBidUnits;
    }

    public List<AeaUnitInfo> getAgencyUnits() {
        return agencyUnits;
    }

    public void setAgencyUnits(List<AeaUnitInfo> agencyUnits) {
        this.agencyUnits = agencyUnits;
    }

    public List<AeaUnitInfo> getCostUnits() {
        return costUnits;
    }

    public void setCostUnits(List<AeaUnitInfo> costUnits) {
        this.costUnits = costUnits;
    }
}
