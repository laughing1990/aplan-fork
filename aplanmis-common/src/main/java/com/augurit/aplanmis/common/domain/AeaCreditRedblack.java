package com.augurit.aplanmis.common.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;

/**
* 信用管理-红黑名单管理-模型
*/
@Data
public class AeaCreditRedblack implements Serializable{

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    private String redblackId;

    /**
     * 是否黑名单，0表示红名单，1表示黑名单
     */
    private String isBlack;

    /**
     * 对象类型，u表示单位，l表示联系人
     */
    private String redblackType;

    /**
     * 企业ID
     */
    private String unitInfoId; 

    /**
     * 联系人ID
     */
    private String linkmanInfoId; 

    /**
     * 列入红黑名单原因
     */
    private String redblackReason;

    /**
     * 程度，例如：一般失信
     */
    private String redblackLevel;

    /**
     * 认证机关
     */
    private String creditUnit; 

    /**
     * 认证依据
     */
    private String creditBasis; 

    /**
     * 认证时间
     */
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date affirmTime; 

    /**
     * 列入红黑名单时间
     */
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date includeTime; 

    /**
     * 截止日期
     */
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date endTime; 

    /**
     * 是否有效，0表示无效，1表示有效
     */
    private String isValid;

    /**
     * 数据无效原因
     */
    private String invalidReason;

    /**
     * 创建人
     */
    private String creater; 

    /**
     * 创建时间
     */
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private Date createTime; 

    /**
     * 更新人
     */
    private String modifier; 

    /**
     * 更新时间
     */
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private Date modifyTime;

    /**
     * 是否同步，0表示手工录入，1表示自动同步
     */
    private String isSync;

    /**
     * 同步来源
     */
    private String syncSource;

    /**
     * 同步时间
     */
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date syncTime;

    /**
     * 根组织id
     */
    private String rootOrgId;

    /**
     * 扩展字段：关键字查询
     */
    private String keyword;

    /**
     * 联系人姓名
     */
    private String linkmanName;

    /**
     * 项目（法人）单位名称
     */
    private String applicant;
}
