package com.augurit.aplanmis.common.domain;

import java.io.Serializable;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

/**
 * 项目变更信息-模型
 * <ul>
 * <li>项目名：奥格工程建设审批系统</li>
 * <li>版本信息：v4.0</li>
 * <li>版权所有(C)2019广州奥格智能科技有限公司-版权所有</li>
 * <li>创建人:小糊涂</li>
 * <li>创建时间：2019-07-04 16:43:27</li>
 * </ul>
 */
@Data
public class AeaProjInfoLog implements Serializable {
// ----------------------------------------------------- Properties

    private static final long serialVersionUID = 1L;
    private java.lang.String logId; // (主键)
    private java.lang.String projInfoId; // (项目ID)
    private java.lang.String userName; // (操作人姓名)
    private java.lang.String opsName; // (操作项)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private java.util.Date changeTime; // (变更时间)
    private java.lang.String changeText; // (变更内容)
    private java.lang.String creater; // (创建人)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private java.util.Date createTime; // (创建时间)
    private java.lang.String reasonMemo; // (原因/备注)
    private java.lang.String changeSource; // (更新来源，例如：窗口人员登记、项目单位登记)
    private String rootOrgId;//根组织ID
}
