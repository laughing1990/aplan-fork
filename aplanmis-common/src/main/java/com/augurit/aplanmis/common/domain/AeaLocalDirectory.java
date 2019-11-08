package com.augurit.aplanmis.common.domain;

import java.io.Serializable;

import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

/**
 * 本级事项目录-模型
 * <ul>
 * <li>项目名：奥格工程建设审批系统</li>
 * <li>版本信息：v4.0</li>
 * <li>版权所有(C)2019广州奥格智能科技有限公司-版权所有</li>
 * <li>创建人:Administrator</li>
 * <li>创建时间：2019-07-04 17:14:27</li>
 * </ul>
 */
@Data
public class AeaLocalDirectory implements Serializable {
// ----------------------------------------------------- Properties

    private static final long serialVersionUID = 1L;
    private String id; // (主键ID)
    private String directorycode; // (临时事项编码)
    private String directoryId; // (通用事(子)项ID)
    private String basecode; // (公共服务事项基本编码)
    private String orgId; // (实施机关ID)
    private String itemcode; // (事项编码)
    private String wtbm; // (网厅编码)
    private String sqjb; // (事权级别)
    private String bjsxmc; // (本级事项名称)
    private String sfyzx; // (是否有子项)
    private String bjzxmc; // (本级子项名称)
    private String bjzxbm; // (本级子项编码)
    private String sxxz; // (事项性质（1：行政许可事项；3：公共服务服务事项）)
    private String xkdx; // (服务对象)
    private String sfzhlr; // (是否暂缓录入)
    private String slyj; // (设立依据)
    private String bbh; // (版本号)
    private String sxmlzt; // (事项状态)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private java.util.Date fbsj; // (发布时间)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private java.util.Date sssj; // (实施时间)
    private String isold; // (是否旧目录系统事项)

    private String rootOrgId;//根组织ID

}
