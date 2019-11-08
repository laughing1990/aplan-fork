package com.augurit.aplanmis.mall.userCenter.vo;

import com.augurit.aplanmis.common.domain.AeaHiIteminst;
import lombok.Data;

import java.util.List;

/**
 * 单项申报实例化后，返回对象实体
 */
@Data
public class SeriesApplyInstantiateResult {
    private String procInstId;
    private String iteminstId;
    private String appinstId;
    private String applyinstId;
    private String applyinstCode;
    List<AeaHiIteminst> aeaHiIteminstList;
}
