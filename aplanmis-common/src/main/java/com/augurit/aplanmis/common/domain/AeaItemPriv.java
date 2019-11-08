package com.augurit.aplanmis.common.domain;

import java.io.Serializable;
import java.util.List;

import io.swagger.annotations.ApiModel;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

/**
 * <ul>
 * <li>项目名：奥格工程建设审批系统</li>
 * <li>版本信息：v4.0</li>
 * <li>版权所有(C)2019广州奥格智能科技有限公司-版权所有</li>
 * <li>创建人：yinlf</li>
 * <li>创建时间：2019-07-25 14:14:42</li>
 * </ul>
 */
@ApiModel
@Data
public class AeaItemPriv implements Serializable {

    private static final long serialVersionUID = 1L;


    /**
     * 主键
     */
    private String privId;

    /**
     * 事项版本ID
     */
    private String itemVerId;

    /**
     * BSC的行政区划ID
     */
    private String regionId;

    /**
     * 顶级组织ID
     */
    private String rootOrgId;

    /**
     * 组织ID
     */
    private String orgId;

    /**
     * 是否启用，0表示禁用，1表示启用
     */
    private String isActive;

    /**
     * 是否启用EL表达式
     */
    private String useEl;

    /**
     * EL表达式
     */
    private String elExpr;

    /**
     * 是否允许人工选择下级承办组织，0表示禁止，1表示允许
     */
    private String allowManual;

    /**
     * 创建人
     */
    private String creater;

    /**
     * 创建时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private java.util.Date createTime;

    /**
     * 修改人
     */
    private String modifier;

    /**
     * 修改时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private java.util.Date modifyTime;

    //扩展字段
    private String regionName;//行政区域名字
    private String orgName;//机构名字
    private List<String> itemVerIds;//事项版本号
}
