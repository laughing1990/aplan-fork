package com.augurit.aplanmis.common.domain;

import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;

/**
 * 申报撤件实例和申请实例历史状态关联表-模型
 * <ul>
 * <li>项目名：奥格工程建设审批系统</li>
 * <li>版本信息：v4.0</li>
 * <li>日期：2019-12-12 14:18:59</li>
 * <li>版权所有(C)2019广州奥格智能科技有限公司-版权所有</li>
 * <li>创建人:Administrator</li>
 * <li>创建时间：2019-12-12 14:18:59</li>
 * <li>修改人1：</li>
 * <li>修改时间1：</li>
 * <li>修改内容1：</li>
 * </ul>
 */
@Data
public class AeaHiApplyinstCancelLog implements Serializable {
// ----------------------------------------------------- Properties

    private static final long serialVersionUID = 1L;
    private String cancelLogId; // (主键)
    private String applyinstCancelId; // (申报撤件实例ID)
    private String applyStateHistId; // (申请实例历史状态ID)
    private String creater; // (创建人)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private java.util.Date createTime; // (创建时间)
}
