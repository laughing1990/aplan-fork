package com.augurit.aplanmis.common.convert;

import com.augurit.agcloud.framework.util.StringUtils;
import com.augurit.aplanmis.common.domain.AeaItemState;
import com.augurit.aplanmis.common.vo.AeaStateDateVo;

import java.io.Serializable;

/**
 * 情形装换类
 * 2018.11.15
 */
public class AeaItemStateConvert implements Serializable {

    private static final long serialVersionUID = 1L;

    public static AeaItemState useByGetItemSteat(boolean isNew, AeaItemState aeaItemState, AeaStateDateVo vo) {

        aeaItemState.setIsDeleted("0");                     //设置是否删除
        if (StringUtils.isNotBlank(vo.getId())){
            aeaItemState.setItemStateId(vo.getId());    //设置id
        }

        if (StringUtils.isNotBlank(vo.getName())){          //设置情形名称
            aeaItemState.setStateName(vo.getName());
        }

        if (StringUtils.isNotBlank(vo.getOpen())){
            if ("true".equals(vo.getOpen())){                   //设置是否启用
                aeaItemState.setIsActive("1");
            }
        }

        if (StringUtils.isNotBlank(vo.getSfbx())){
            aeaItemState.setMustAnswer(vo.getSfbx());
        }

        aeaItemState.setIsQuestion("0");
        if (StringUtils.isNotBlank(vo.getSjlx())){
            if ("1".equals(vo.getSjlx().trim())){                      //设置是问题还是答案
                aeaItemState.setIsQuestion("1");
            }else {
                aeaItemState.setIsQuestion("0");
            }
        }

        if (StringUtils.isNotBlank(vo.getSfdx())){
            if ("1".equals(vo.getSfdx().trim())){                             //设置是多选还是单选
                aeaItemState.setAnswerType("m");
            }else{
                aeaItemState.setAnswerType("s");
            }
        }

        return aeaItemState;
    }
}
