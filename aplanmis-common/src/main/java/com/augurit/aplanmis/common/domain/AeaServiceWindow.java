package com.augurit.aplanmis.common.domain;

import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;

/**
 *
 * 服务窗口实体对象
 *
 * @author jjt
 * @date 2019/7/2
 *
 */
@Data
public class AeaServiceWindow implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 服务窗口ID
     */
    private String windowId;

    /**
     * 窗口名称
     */
    private String windowName;

    /**
     * 行政区划代码
     */
    private String regionId;

    /**
     * 组织机构代码
     */
    private String orgId;

    /**
     * 联系电话
     */
    private String linkPhone;

    /**
     * 办公时间
     */
    private String workTime;

    /**
     * 窗口地址
     */
    private String windowAddress;

    /**
     * 交通指引
     */
    private String trafficGuide;

    /**
     * 备注说明
     */
    private String windowMemo;

    /**
     * 地图链接
     */
    private String mapUrl;

    /**
     * 地图附件
     */
    private String mapAtt;

    /**
     * 创建人
     */
    private String creater;

    /**
     * 创建时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private java.util.Date createTime;

    /**
     * 修改人
     */
    private String modifier;

    /**
     * 修改时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private java.util.Date modifyTime;

    /**
     * 排列顺序号
     */
    private Long sortNo;

    /**
     * 是否启用，0表示禁用，1表示启用
     */
    private String isActive;

    /**
     * 窗口类型，来自于数据字典，0表示综合窗口，1表示收费发证窗口，d表示代办中心
     */
    private String windowType;

    /**
     * 是否公开公示，0表示不公开，1表示公开
     */
    private String isPublic;

    /**
     *  是否所有单项通办，0表示不是，1表示是
     */
    private String isAllItem;

    /**
     * 是否所有阶段通办，0表示不是，1表示是
     */
    private String isAllStage;

    /**
     * 根组织ID
     *
     */
    private String rootOrgId;

    /**
     * 办理范围，0表示全市通办，1表示属地办理
     */
    private String regionRange;

    /**
     * 并联使用事项过滤，0表示不过滤，1表示过滤
     */
    private String stageItemFilter;

    /**
     * 扩展字段: 关键字查询
     */
    private String keyword;

    /**
     * 扩展字段: 业务表名（大写）
     */
    private String tableName;

    /**
     * 扩展字段: 业务表主键ID（大写）
     */
    private String pkName;

    /**
     * 扩展字段: 关联记录id
     */
    private String recordId;

    /**
     * 扩展字段: 地图附件数量
     */
    private Long mapAttNum;

    /**
     * 扩展字段: 行政区域名称
     */
    private String regionName;

    /**
     * 扩展字段: 行政区域编码
     */
    private String regionCode;

    /**
     * 扩展字段: 部门名称
     */
    private String orgName;

    /**
     * 扩展字段: 阶段名称
     */
    private String stageName;

    /**
     * 扩展字段: 阶段主题
     */
    private String themeName;

    /**
     * 窗口颜色，效能督查统计图表使用
     */
    private String echartsColor;

}
