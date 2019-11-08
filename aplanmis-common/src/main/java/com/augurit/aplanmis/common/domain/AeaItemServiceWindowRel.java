package com.augurit.aplanmis.common.domain;

import java.io.Serializable;

import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

/**
 * -模型
 * <ul>
 * <li>项目名：奥格工程建设审批系统</li>
 * <li>版本信息：v4.0</li>
 * <li>版权所有(C)2019广州奥格智能科技有限公司-版权所有</li>
 * <li>创建人:Administrator</li>
 * <li>创建时间：2019-07-04 17:13:57</li>
 * </ul>
 */
@Data
public class AeaItemServiceWindowRel implements Serializable {
// ----------------------------------------------------- Properties

    private static final long serialVersionUID = 1L;
    private String windowRelId; // (服务窗口关联ID)
    private String tableName; // (业务表名（大写）)
    private String pkName; // (业务表主键ID（大写）)
    private String recordId; // (业务记录数据)
    private String windowId; // (服务窗口ID)
    private String creater; // (创建人)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private java.util.Date createTime; // (创建时间)
    private String modifier; // (修改人)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private java.util.Date modifyTime; // (修改时间)
    private String rootOrgId;//根组织ID
    //非表字段
    private String windowName;//窗口名称
    private String linkPhone;//联系方式
    private String workTime;//办公时间
    private String windowAddress;//地址
    private String trafficGuide;//路线指引

}
