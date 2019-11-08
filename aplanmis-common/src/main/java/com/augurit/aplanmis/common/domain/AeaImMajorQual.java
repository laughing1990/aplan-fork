package com.augurit.aplanmis.common.domain;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;

/**
 * -模型
 <ul>
 <li>项目名：奥格erp3.0--第一期建设项目</li>
 <li>版本信息：v1.0</li>
 <li>日期：2019-06-11 18:54:38</li>
 <li>版权所有(C)2016广州奥格智能科技有限公司-版权所有</li>
 <li>创建人:tiantian</li>
 <li>创建时间：2019-06-11 18:54:38</li>
 <li>修改人1：</li>
 <li>修改时间1：</li>
 <li>修改内容1：</li>
 </ul>
 */
@Data
@ApiModel("专业资质要求")
public class AeaImMajorQual implements Serializable {
// ----------------------------------------------------- Properties

    private static final long serialVersionUID = 1L;
    @ApiModelProperty(value = "专业资质关联ID")
    private String majorQualId; // ()
    @ApiModelProperty(value = "专业ID")
    private String majorId; // (专业关联ID)
    private String isDelete; // (是否删除：1 已删除，0 未删除)
    private String creater; // ()
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private java.util.Date createTime; // ()
    @ApiModelProperty(value = "资质等级ID")
    private String qualLevelId; // (资质等级ID)
    private String unitRequireId; // (中介机构要求信息ID)

    private String priority;
}