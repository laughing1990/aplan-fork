package com.augurit.aplanmis.common.domain;

import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.Size;
import java.io.Serializable;

/**
 * 事项输入输出实例表-模型
 * <ul>
 * <li>项目名：奥格erp3.0--第一期建设项目</li>
 * <li>版本信息：v1.0</li>
 * <li>日期：2019-08-03 10:31:32</li>
 * <li>版权所有(C)2016广州奥格智能科技有限公司-版权所有</li>
 * <li>创建人:Administrator</li>
 * <li>创建时间：2019-08-03 10:31:32</li>
 * <li>修改人1：</li>
 * <li>修改时间1：</li>
 * <li>修改内容1：</li>
 * </ul>
 */
@Data
public class AeaHiItemCorrectRealIninst implements Serializable {
// ----------------------------------------------------- Properties

    private static final long serialVersionUID = 1L;
    private String realIninstId; // (ID)
    private String correctId; // (事项实例ID)
    private String dueIninstId; // ()
    private String inoutinstId; // ()
    private String isPass; // (单个事项输入输出定义ID)
    @Size(max = 10)
    private Long attCount; // ()
    @Size(max = 10)
    private Long realPaperCount; // ()
    @Size(max = 10)
    private Long realCopyCount; // ()
    private String creater; // (创建人)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private java.util.Date createTime; // (创建时间)
    private String modifier; // ()
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private java.util.Date modifyTime; // ()
    private String rootOrgId;//根组织ID

    //扩展字段
    private String matinstId;
    private String matinstName;
    private String matId;
}
