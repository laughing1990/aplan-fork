package com.augurit.aplanmis.common.domain;

import lombok.Data;

import java.io.Serializable;

/**
 * @author hzl
 * @date 2019/4/16
 *
 * 思维导图的界面控制
 */
@Data
public class AeaMindUi implements Serializable {

    /**
     * 是否显示材料
     */
    private boolean showMat;

    /**
     * 是否显示证照
     */
    private boolean showCert;

    /**
     * 是否显示情形事项
     */
    private boolean showSituationLinkItem;

    /**
     * 是否显示表单
     */
    private boolean showForm;
}
