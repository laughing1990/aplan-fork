package com.augurit.aplanmis.common.convert;

import com.augurit.agcloud.framework.util.StringUtils;
import com.augurit.aplanmis.common.domain.AeaItemCond;
import com.augurit.aplanmis.common.vo.AeaItemFrontCondStateVo;

import java.io.Serializable;

public class AeaItemFrontCondStateConvert implements Serializable {

    private static final long serialVersionUID = 1L;

    public static AeaItemCond useByGetFrontCondState(AeaItemCond aeaItemCond, AeaItemFrontCondStateVo vo) {

        if (StringUtils.isNotBlank(vo.getId())){
            aeaItemCond.setItemCondId(vo.getId());    //设置id
        }

        if (StringUtils.isNotBlank(vo.getName())){    //设置情形名称
            aeaItemCond.setCondName(vo.getName());
        }

        //设置是问题还是答案
        aeaItemCond.setIsQuestion("0"); // 默认都是答案
        if (StringUtils.isNotBlank(vo.getSjlx())){
            if ("1".equals(vo.getSjlx().trim())){
                aeaItemCond.setIsQuestion("1");
            }else {
                aeaItemCond.setIsQuestion("0");
            }
        }

        aeaItemCond.setSfzz(vo.getSfzz()); // 是否终止，选择的申报条件是否需要申报
        aeaItemCond.setMuiltSelect(vo.getMuiltSelect()); // 申报条件需要满足几个
        aeaItemCond.setCondMemo(vo.getBz());// 备注
        aeaItemCond.setUseEl("0"); // 是否启用EL表达式
        aeaItemCond.setIsActive("1"); // 是否启用
        return aeaItemCond;
    }
}
