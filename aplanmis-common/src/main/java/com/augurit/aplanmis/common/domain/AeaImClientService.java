package com.augurit.aplanmis.common.domain;

import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;

/**
 * -模型
 * <ul>
 * <li>项目名：奥格工程建设审批系统</li>
 * <li>版本信息：v4.0</li>
 * <li>日期：2019-07-09 10:20:43</li>
 * <li>版权所有(C)2019广州奥格智能科技有限公司-版权所有</li>
 * <li>创建人:Augurit</li>
 * <li>创建时间：2019-07-09 10:20:43</li>
 * <li>修改人1：</li>
 * <li>修改时间1：</li>
 * <li>修改内容1：</li>
 * </ul>
 */
@Data
public class AeaImClientService implements Serializable {

    private static final long serialVersionUID = 1L;
    private String clientServiceId; // ()
    private String unitServiceId; // (中介机构服务ID)
    private String unitLinkmanId; // (委托人ID)
    private String isDeleted; // (逻辑删除标记。0表示正常记录，1表示已删除记录。)
    private String creater; // (创建人)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private java.util.Date createTime; // (创建时间)
}
