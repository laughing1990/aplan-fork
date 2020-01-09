package com.augurit.aplanmis.mall.userCenter.vo;

import com.augurit.agcloud.bsc.util.UuidUtil;
import com.augurit.aplanmis.common.domain.AeaHiSmsInfo;
import com.augurit.aplanmis.common.domain.AeaProjInfo;
import com.augurit.aplanmis.common.domain.AeaUnitInfo;
import com.augurit.aplanmis.common.domain.AeaUnitProj;
import com.augurit.aplanmis.common.vo.AeaUnitInfoVo;
import com.augurit.aplanmis.common.vo.LinkmanTypeVo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotEmpty;
import java.util.ArrayList;
import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
@ApiModel("项目单位领件信息vo")
public class SmsProjUnitInfoVo extends AeaProjInfo{

    @ApiModelProperty(value = "申请实例ID")
    private String applyinstId;
    @ApiModelProperty(value = "单位项目关联list")
    private List<AeaUnitProj> aeaUnitProjs;

    @ApiModelProperty(value = "新增的单位list")
    private List<com.augurit.aplanmis.common.vo.AeaUnitInfoVo> unitInfos;

    @ApiModelProperty(value = "当前企业用户的人员设置")
    private List<LinkmanTypeVo> linkmanTypeVos;

    @ApiModelProperty(value = "申报时传的联系人ID参数")
    private String linkmanInfoId;
    @ApiModelProperty(value = "申报时传的申报方式参数")
    private String applySubject;
    @ApiModelProperty(value = "是否单项申报 1是 0否")
    private String isSeriesApprove;
    @ApiModelProperty(value = "事项版本ID（isSeriesApprove==1时）")
    private String itemVerId;


    public static List<AeaUnitInfo> returnForm(List<com.augurit.aplanmis.common.vo.AeaUnitInfoVo> unitInfos) {
        List<AeaUnitInfo> list = new ArrayList<>();
        if (unitInfos.size() > 0) {
            for (com.augurit.aplanmis.common.vo.AeaUnitInfoVo vo : unitInfos) {
                list.add(AeaUnitInfoVo.returnForm(vo));
            }
        }
        return list;
    }

    @ApiModelProperty(value = "主键smsId", dataType = "string", allowEmptyValue = true)
    private String id;

    @ApiModelProperty(value = "签收方式: 0,邮政快递; 1, 窗口取证 2 网上签收", required = true, allowableValues = "0, 1")
    @NotEmpty(message = "receiveMode is null")
    private String receiveMode;

    @ApiModelProperty(value = "领取方式， 1：一次领取，0：多次领取",  allowableValues = "0, 1")
    private String receiveType;


    @ApiModelProperty(value = "收件人名字", required = true, example = "小明")
    @NotEmpty(message = "addressName is null")
    private String addresseeName;

    @ApiModelProperty(value = "收件人联系方式", required = true, example = "18810011001")
    @NotEmpty(message = "addresseePhone is null")
    private String addresseePhone;

    @ApiModelProperty(value = "收件人身份证", required = true, example = "360423189821340987")
    @NotEmpty(message = "addresseeIdcard is null")
    private String addresseeIdcard;

    @ApiModelProperty(value = "收件人所在省份", example = "广东省")
    private String addresseeProvince;

    @ApiModelProperty(value = "收件人所在城市", example = "广州市")
    private String addresseeCity;

    @ApiModelProperty(value = "收件人区县", example = "天河区")
    private String addresseeCounty;

    @ApiModelProperty(value = "收件人详细地址", example = "广东省广州市天河区1029号")
    private String addresseeAddr;

    public AeaHiSmsInfo toSmsInfo() {
        AeaHiSmsInfo aeaHiSmsInfo = new AeaHiSmsInfo();
        aeaHiSmsInfo.setId(UuidUtil.generateUuid());
        return setAeaHiSmsInfo(aeaHiSmsInfo);
    }

    public AeaHiSmsInfo merge(AeaHiSmsInfo aeaHiSmsInfo) {
        return setAeaHiSmsInfo(aeaHiSmsInfo);
    }

    private AeaHiSmsInfo setAeaHiSmsInfo(AeaHiSmsInfo aeaHiSmsInfo) {
        aeaHiSmsInfo.setReceiveMode(this.receiveMode);
        aeaHiSmsInfo.setReceiveType(this.receiveType);
        aeaHiSmsInfo.setAddresseeName(this.addresseeName);
        aeaHiSmsInfo.setAddresseePhone(this.addresseePhone);
        aeaHiSmsInfo.setApplyinstId(this.applyinstId);
        aeaHiSmsInfo.setAddresseeProvince(this.addresseeProvince);
        aeaHiSmsInfo.setAddresseeCity(this.addresseeCity);
        aeaHiSmsInfo.setAddresseeCounty(this.addresseeCounty);
        aeaHiSmsInfo.setAddresseeAddr(this.addresseeAddr);
        aeaHiSmsInfo.setAddresseeIdcard(this.addresseeIdcard);
        return aeaHiSmsInfo;
    }
}
