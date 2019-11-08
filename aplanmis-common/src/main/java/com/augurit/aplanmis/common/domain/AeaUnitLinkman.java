package com.augurit.aplanmis.common.domain;

import java.io.Serializable;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

/**
 * -模型
 * <ul>
 * <li>项目名：奥格工程建设审批系统</li>
 * <li>版本信息：v4.0</li>
 * <li>版权所有(C)2019广州奥格智能科技有限公司-版权所有</li>
 * <li>创建人:小糊涂</li>
 * <li>创建时间：2019-07-04 16:40:49</li>
 * </ul>
 */
@Data
public class AeaUnitLinkman implements Serializable {
// ----------------------------------------------------- Properties

    private static final long serialVersionUID = 1L;
    private java.lang.String unitLinkmanId; // ()
    private java.lang.String unitInfoId; // (企业ID)
    private java.lang.String linkmanInfoId; // (联系人ID)
    private java.lang.String creater; // ()
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private java.util.Date createTime; // ()
    private java.lang.String isBindAccount; // (是否绑定账号：1 是， 0  否)
    private java.lang.String isAdministrators; // (是否为管理员：1 是，0 否)

}
