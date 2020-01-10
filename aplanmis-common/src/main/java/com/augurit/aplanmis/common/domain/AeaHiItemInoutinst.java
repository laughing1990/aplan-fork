package com.augurit.aplanmis.common.domain;

import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;

/**
 * 事项输入输出实例表-模型
 * <ul>
 * <li>项目名：奥格工程建设审批系统</li>
 * <li>版本信息：v4.0</li>
 * <li>版权所有(C)2019广州奥格智能科技有限公司-版权所有</li>
 * <li>创建人:Administrator</li>
 * <li>创建时间：2019-07-04 16:45:13</li>
 * </ul>
 */
@Data
public class AeaHiItemInoutinst implements Serializable {
// ----------------------------------------------------- Properties

    private static final long serialVersionUID = 1L;
    private String inoutinstId; // (ID)
    private String iteminstId; // (事项实例ID)
    private String itemInoutId; // (单个事项输入输出定义ID)
    private String matinstId; // (材料实例ID)
    private String certinstId; // (电子证件证照实例ID)
    private String creater; // (创建人)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private java.util.Date createTime; // (创建时间)
    private String modifier; // (修改人)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private java.util.Date modifyTime; // (修改时间)
    private String isCollected; // (是否已收取，0表示未收取，1表示已收取)
    private String isParIn; // (是否阶段情形输入，0表示单个事项输入，1表示阶段情形输入【当is_input=1】)
    private String parInId; // (阶段情形输入定义ID【当IS_PAR_IN=1】)
    private String rootOrgId;//根组织ID

}
