package com.augurit.aplanmis.common.domain;

import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;

/**
 * -模型
 * <ul>
 * <li>项目名：奥格erp3.0--第一期建设项目</li>
 * <li>版本信息：v1.0</li>
 * <li>日期：2019-07-02 16:00:48</li>
 * <li>版权所有(C)2016广州奥格智能科技有限公司-版权所有</li>
 * <li>创建人:Augurit</li>
 * <li>创建时间：2019-07-02 16:00:48</li>
 * <li>修改人1：</li>
 * <li>修改时间1：</li>
 * <li>修改内容1：</li>
 * </ul>
 */
@Data
public class AeaImAvoidUnit implements Serializable {
// ----------------------------------------------------- Properties

    private static final long serialVersionUID = 1L;
    private String avoidUnitId; // ()
    private String projPurchaseId; // (采购需求ID)
    private String unitInfoId; // (需回避的企业ID)
    private String isDelete; // (是否删除：1 已删除，0 未删除)
    private String creater; // ()
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private java.util.Date createTime; // ()

}
