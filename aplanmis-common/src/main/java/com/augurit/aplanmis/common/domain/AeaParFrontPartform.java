package com.augurit.aplanmis.common.domain;

import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.Size;
import java.io.Serializable;

/**
 * 阶段的扩展表单前置检测表-模型
 * <ul>
 * <li>项目名：奥格erp3.0--第一期建设项目</li>
 * <li>版本信息：v1.0</li>
 * <li>日期：2019-11-01 10:48:23</li>
 * <li>版权所有(C)2016广州奥格智能科技有限公司-版权所有</li>
 * <li>创建人:Administrator</li>
 * <li>创建时间：2019-11-01 10:48:23</li>
 * <li>修改人1：</li>
 * <li>修改时间1：</li>
 * <li>修改内容1：</li>
 * </ul>
 */
@Data
public class AeaParFrontPartform implements Serializable {
    private static final long serialVersionUID = 1L;
    private String frontPartformId; // (ID)
    private String stageId; // (阶段ID)
    private String stagePartformId; // (阶段扩展表单ID)
    @Size(max = 10)
    private Long sortNo; // (排序字段)
    private String isActive; // ()
    private String frontPartformMemo; // ()
    private String creater; // (创建人)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private java.util.Date createTime; // (创建时间)
    private String modifier; // ()
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private java.util.Date modifyTime; // ()
    private String rootOrgId; // (根组织id)


    //非表字段
    //查询关键字
    private String keyword;
}
