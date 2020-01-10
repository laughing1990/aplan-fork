package com.augurit.aplanmis.mall.userCenter.vo;

import com.augurit.agcloud.framework.util.StringUtils;
import com.augurit.aplanmis.common.apply.item.GuideComputedItem;
import com.augurit.aplanmis.common.domain.AeaLinkmanInfo;
import com.augurit.aplanmis.common.vo.guide.GuideDetailVo;
import com.augurit.aplanmis.mall.main.vo.ParallelApproveItemVo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import java.util.List;

@Data
@ApiModel("并联审批事项确认返回结果VO")
public class ApplyIteminstConfirmVo {
    @ApiModelProperty(value = "是否变更主题  1是 0否")
    private String isThemeChange;

    @ApiModelProperty(value = "牵头部门选择主题ID")
    private String leaderThemeId;
    @ApiModelProperty(value = "牵头部门选择主题名称")
    private String leaderThemeName;

    @ApiModelProperty(value = "智能引导选择主题名称")
    private String itThemeName;

    @ApiModelProperty(value = "申请人选择主题ID")
    private String applyThemeId;
    @ApiModelProperty(value = "申请人选择主题名称")
    private String applyThemeName;

    @ApiModelProperty(value = "牵头部门阶段ID")
    private String stageId;
    @ApiModelProperty(value = "通用阶段名称")
    private String stageName;
    @ApiModelProperty(value = "牵头部门阶段国家标准 对应国家标准审批阶段，多选，1 立项用地规划许可 2 工程建设许可 3 施工许可 4 竣工验收 5 并行推进")
    private String dybzspjdxh;//DYBZSPJDXH

    @ApiModelProperty(value = "并行审批事项列表")
    private List<GuideComputedItem> coreIteminstList;
    @ApiModelProperty(value = "并联推进事项列表")
    private List<GuideComputedItem> parallelIteminstList;

    @ApiModelProperty(value = "部门意见")
    private String deptComments;

    @ApiModelProperty(value = "项目ID")
    private String projInfoId;
    @ApiModelProperty(value = "项目名称")
    private String projName;
    @ApiModelProperty(value = "项目/工程代码")
    private String gcbm;

    @ApiModelProperty(value = "是否智能引导 1 是 0否")
    private String isItSel;

    @ApiModelProperty(value = "阶段情形ID集合")
    private List<String> stateIds;

    @ApiModelProperty(value = "申报主体 0表示个人，1表示企业", required = true, allowableValues = "0, 1")
    private String applySubject;
    @ApiModelProperty(value = "申办单位ID", required = false)
    private String unitInfoId;
    @ApiModelProperty(value = "联系人ID", required = true)
    private String linkmanInfoId;

    @ApiModelProperty(value = "申报实例ID")
    private String applyinstId;

    @ApiModelProperty(value = "联系人信息，用于办件领取信息回显")
    private SmsInfoVo smsInfoVo;

    @ApiModelProperty(value = "0 个人 1单位")
    private String loginType;
    @ApiModelProperty(value = "证件编码 loginType=0时，表示身份证  loginType=l时，表示企业统一信用代码")
    private String idCardCode;

    @ApiModelProperty(value = "部门辅导ID")
    private String guideId;


    public static ApplyIteminstConfirmVo formatGuide(GuideDetailVo detail) {
        ApplyIteminstConfirmVo vo=new ApplyIteminstConfirmVo();

        vo.setApplyThemeId(detail.getAeaHiGuide().getThemeId());
        vo.setApplyThemeName(detail.getAeaHiGuide().getThemeName());

        vo.setLeaderThemeId(StringUtils.isNotBlank(detail.getNewThemeId())?detail.getNewThemeId():detail.getAeaHiGuide().getThemeId());
        vo.setLeaderThemeName(StringUtils.isNotBlank(detail.getNewThemeName())?detail.getNewThemeName():detail.getAeaHiGuide().getThemeName());

        vo.setStageId(StringUtils.isNotBlank(detail.getNewStageId())?detail.getNewStageId():detail.getAeaHiGuide().getStageId());
        vo.setStageName(detail.getAeaHiGuide().getStageName());
        vo.setDeptComments(detail.getAeaHiGuide().getLeaderOrgOpinion());
        vo.setIsThemeChange(detail.isThemeChanged()?"1":"0");
        return vo;
    }

    public static SmsInfoVo formatLinkmanInfo(AeaLinkmanInfo aeaLinkmanInfo,String applyinstId){
        SmsInfoVo smsInfoVo=new SmsInfoVo();
        smsInfoVo.setAddresseeName(aeaLinkmanInfo.getLinkmanName());
        smsInfoVo.setAddresseeIdcard(aeaLinkmanInfo.getLinkmanCertNo());
        smsInfoVo.setAddresseePhone(aeaLinkmanInfo.getLinkmanMobilePhone());
        smsInfoVo.setApplyinstId(applyinstId);
        return smsInfoVo;
    }
}
