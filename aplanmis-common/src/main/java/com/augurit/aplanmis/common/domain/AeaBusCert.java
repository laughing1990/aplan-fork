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
 * <li>创建时间：2019-07-04 17:12:19</li>
 * </ul>
 */
@Data
public class AeaBusCert implements Serializable {

    private static final long serialVersionUID = 1L;
    private String busCertId; // ()
    private String busTableName; // ()
    private String pkName; // ()
    private String busRecordId; // ()
    private String certId; // ()
    private String isDelete; // (是否删除，0表示未删除，1表示已删除)
    private String creater; // (创建人)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private java.util.Date createTime; // (创建时间)
    private String modifier; // ()
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private java.util.Date modifyTime; // ()
    private String memo; // (备注)
    private String rootOrgId;//根组织ID

    //扩展字段
    private String[] saveCertIds;     //要关联的证书id
    private String[] cancelCertIds;   //要取消关联的证书id
}
