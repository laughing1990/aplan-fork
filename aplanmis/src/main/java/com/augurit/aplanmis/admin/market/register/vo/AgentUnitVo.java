package com.augurit.aplanmis.admin.market.register.vo;

import com.augurit.aplanmis.common.domain.AeaUnitInfo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.beans.BeanUtils;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.ArrayList;
import java.util.List;

@Data
@ApiModel(value = "入住申请单位对象")
public class AgentUnitVo {
    private java.lang.String unitInfoId;

    @ApiModelProperty("单位名称")
    private java.lang.String applicant;
    @ApiModelProperty(value = "统一社会信用代码", dataType = "string")
    private String unifiedSocialCreditCode;

    @ApiModelProperty("法人姓名")
    private String idrepresentative;

    @ApiModelProperty("法人身份证号码")
    private java.lang.String idno;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @ApiModelProperty("创建时间")
    private java.util.Date createTime;

    @ApiModelProperty("是否为中介机构：1 是，0 否")
    private java.lang.String isImUnit;

    @ApiModelProperty("是否为业主单位：1 是，0 否")
    private java.lang.String isOwnerUnit;

    @ApiModelProperty("审核状态:0 审核失败，1 已审核，2 审核中")
    private String auditFlag;

    @ApiModelProperty("逻辑删除标记。0表示正常记录，1表示已删除记录。")
    private java.lang.String isDeleted;


    public AgentUnitVo() {
    }

    public AgentUnitVo(AeaUnitInfo unitInfo) {
        BeanUtils.copyProperties(unitInfo, this);
    }

    public static List<AgentUnitVo> changeToVoList(List<AeaUnitInfo> list) {

        List<AgentUnitVo> volist = new ArrayList<>();
        if (null == list || list.isEmpty()) {
            return volist;
        }
        for (AeaUnitInfo info : list) {
            AgentUnitVo vo = new AgentUnitVo(info);
            volist.add(vo);
        }
        return volist;
    }
}
