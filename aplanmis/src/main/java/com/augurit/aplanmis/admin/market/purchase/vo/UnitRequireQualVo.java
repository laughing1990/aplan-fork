package com.augurit.aplanmis.admin.market.purchase.vo;

import com.augurit.aplanmis.common.vo.AeaImQualLevelVo;
import lombok.Data;

import java.util.List;


@Data
public class UnitRequireQualVo {

    private String qualId;

    private String qualName;

    private List<AeaImQualLevelVo> qualLevelList;

}
