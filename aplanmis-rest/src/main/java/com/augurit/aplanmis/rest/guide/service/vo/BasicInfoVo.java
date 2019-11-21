package com.augurit.aplanmis.rest.guide.service.vo;

import com.augurit.aplanmis.common.domain.AeaItemBasic;
import com.augurit.aplanmis.rest.guide.constant.HandlingType;
import com.augurit.aplanmis.rest.guide.constant.ItemType;
import lombok.Data;

@Data
public class BasicInfoVo {

    // 基本信息id
    private String itemBasicId;
    // 事项版本id
    private String itemVerId;
    // 事项名称
    private String itemName;
    // 事项类型
    private String itemTypeName;
    private String itemTypeValue;
    // 基本编码
    private String baseCode;
    // 实施编码
    private String carryOutCode;
    // 行使层级
    private String executeLevel;
    // 处罚形为
    private String punishAction;
    // 实施主体
    private String carryOutSubject;
    // 实施主题性质
    private String carryOutPropery;
    // 法定办结时限
    private String lawTimeLimit;
    // 业务系统
    private String businessSystem;
    // 办件类型
    private String handingTypeName;
    private String handingTypeValue;
    // 权力来源
    private String authrotyFrom;
    //联办机构
    private String unionOrganization;

    public static BasicInfoVo from(AeaItemBasic aeaItemBasic) {
        BasicInfoVo vo = new BasicInfoVo();
        vo.setItemBasicId(aeaItemBasic.getItemBasicId());
        vo.setItemVerId(aeaItemBasic.getItemVerId());
        vo.setItemName(aeaItemBasic.getItemName());

        ItemType itemType = ItemType.fromValue(aeaItemBasic.getItemType());
        vo.setItemTypeName(itemType.getName());
        vo.setItemTypeValue(itemType.getValue());

        vo.setBaseCode(aeaItemBasic.getBasecode());
        vo.setCarryOutCode(aeaItemBasic.getSscode());
        vo.setExecuteLevel("市级");
        vo.setPunishAction("对违反《广播电视视频点播业务管理办法》第二十八条规定，播出前端未按规定与广播电视行政部门监控系统进行联网行为的处罚");
        vo.setCarryOutSubject("唐山市");
        vo.setCarryOutPropery("法定机关");
        vo.setLawTimeLimit(aeaItemBasic.getDueNum() > 0 ? aeaItemBasic.getDueNum() + "工作日" : "无");
        vo.setBusinessSystem("无");

        HandlingType handlingType = HandlingType.fromValue(aeaItemBasic.getBjType());
        vo.setHandingTypeName(handlingType.getName());
        vo.setHandingTypeValue(handlingType.getValue());

        vo.setAuthrotyFrom("法定本级行使");
        vo.setUnionOrganization("无");

        return vo;
    }

}
