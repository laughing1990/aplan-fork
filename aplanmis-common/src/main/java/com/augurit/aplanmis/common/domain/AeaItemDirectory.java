package com.augurit.aplanmis.common.domain;

import java.io.Serializable;
import javax.validation.constraints.Size;

import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

/**
 * 通用事(子)项表-模型
 * <ul>
 * <li>项目名：奥格工程建设审批系统</li>
 * <li>版本信息：v4.0</li>
 * <li>版权所有(C)2019广州奥格智能科技有限公司-版权所有</li>
 * <li>创建人:Administrator</li>
 * <li>创建时间：2019-07-04 17:12:38</li>
 * </ul>
 */
@Data
public class AeaItemDirectory implements Serializable {
// ----------------------------------------------------- Properties

    private static final long serialVersionUID = 1L;
    private String directoryId; // (主键)
    private String tysxm; // (通用事(子)项码)
    private String tysxmc; // (通用事(子)项名称)
    @Size(max = 22)
    private Long proXh; // (省级事(子)项序号)
    private String proSxmc; // (省级事(子)项名称)
    @Size(max = 22)
    private Long citXh; // (市级事(子)项序号)
    private String citSxmc; // (市级事(子)项名称)
    @Size(max = 22)
    private Long couXh; // (县级事(子)项序号)
    private String couSxmc; // (县级事(子)项名称)
    private String spbm; // (审批部门)
    @Size(max = 22)
    private Long xh; // (序号)
    private String tymlyj; // (通用目录依据)
    private String spdx; // (审批对象)
    private String bbh; // (版本号)
    private String tymlzt; // (通用目录状态)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private java.util.Date fbsj; // (发布时间)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private java.util.Date sssj; // (实施时间)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private java.util.Date syncTime; // (同步时间)
    private String tymlyjIds; // (通用目录依据IDS)
    private String parentId; // (父ID)
    private String creater; // (创建人)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private java.util.Date createTime; // (创建时间)
    private String modifier; // (修改人)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private java.util.Date modifyTime; // (修改时间)
    private String rootOrgId;//根组织ID
}
