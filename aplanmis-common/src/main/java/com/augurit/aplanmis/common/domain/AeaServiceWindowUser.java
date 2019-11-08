package com.augurit.aplanmis.common.domain;

import io.swagger.annotations.ApiModel;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;

/**
 * <ul>
 * <li>项目名：奥格工程建设审批系统</li>
 * <li>版本信息：v4.0</li>
 * <li>版权所有(C)2019广州奥格智能科技有限公司-版权所有</li>
 * <li>创建人：yinlf</li>
 * <li>创建时间：2019-07-25 14:14:40</li>
 * </ul>
 */
@ApiModel
@Data
public class AeaServiceWindowUser implements Serializable {

    private static final long serialVersionUID = 1L;


    /**
     * 主键
     */
    private String windowUserId;

    /**
     * 窗口ID
     */
    private String windowId;

    /**
     * 用户ID
     */
    private String userId;

    /**
     * 是否启用，0表示禁用，1表示启用
     */
    private String isActive;

    /**
     * 创建人
     */
    private String creater;

    /**
     * 创建时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private java.util.Date createTime;

    private String rootOrgId; //跟组织ID

    /**
     * 扩展字段：登录名
     */
    private String loginName;

    /**
     * 扩展字段：用户名
     */
    private String userName;

    /**
     * 扩展字段：人员性别
     */
    private String userSex;

    /**
     * 扩展字段：手机号码
     */
    private String userMobile;

    /**
     * 扩展字段：组织名称
     */
    private String orgName;

    /**
     * 扩展字段：窗口名称
     */
    private String windowName;

    /**
     * 扩展字段：窗口所属行政区划ID
     */
    private String regionId;
}
