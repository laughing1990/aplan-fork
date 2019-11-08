package com.augurit.aplanmis.common.dto;

import lombok.Data;

@Data
public class AeaItemInoutMatDto {
    // aeaItemInout
    private String inoutId;
    private String itemVerId;
    private String stateVerId;
    private String isOwner;
    private String isInput;
    private String fileType;
    private String certId;
    private String isStateIn;
    private String ioutIsDeleted;
    private String ioutCreater;
    private String ioutCreateTime;
    private String ioutModifier;
    private String ioutModifierTime;
    private String itemStateId;
    private Integer ioutSortNo;

    // aeaItemMat
    private String matId;
    private String matCode; // 材料编码
    private String matName; // 材料名称
    private String isGlobalShare; // 是否全局材料
    private String matTypeId; // 材料类型
    private String receiveMode;
    private String paperMatType;// 纸质材料类型 A3 A4
    private String duePaperType; // 验/收
    private String duePaperCount;// 份数
    private String dueCopyType;// 验/收
    private String dueCopyCount;// 份数
    private String matFrom; //材料来源
    private String sampleDoc; // 样表
    private String templateDoc; // 空表
    private String matRequire;
    private String matBasis;
    private String paperIsRequire; // 纸质是否必须
    private String matSortNo;
    private String matMemo; // 备注
    private String isActive; // 是否有效
    private String matIsDeleted; // 是否删除
    private String matCreater;
    private String matCreateTime;
    private String matModifier;
    private String matModifyTime;
    private String attDirId;
    private String matHolder;
    private String isOfficialDoc; // 是否批文批复
    private String attIsRequire; // 电子材料是否必须
    private String isCommon; // 是否通用
    private String reviewKeyPoints; // 审查要点
    private String reviewSampleDoc; //审查样本
    private String reviewBasis; // 审查依据
    private String matFromDept; // 材料来源部门
    private String zcqy; //是否支持容缺
}
