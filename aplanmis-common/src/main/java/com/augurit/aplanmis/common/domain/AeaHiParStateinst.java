package com.augurit.aplanmis.common.domain;

import java.io.Serializable;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

/**
 * 主题阶段情形实例表-模型
 * <ul>
 * <li>项目名：奥格工程建设审批系统</li>
 * <li>版本信息：v4.0</li>
 * <li>版权所有(C)2019广州奥格智能科技有限公司-版权所有</li>
 * <li>创建人:Administrator</li>
 * <li>创建时间：2019-07-04 16:45:19</li>
 * </ul>
 */
@Data
public class AeaHiParStateinst implements Serializable {
// ----------------------------------------------------- Properties

    private static final long serialVersionUID = 1L;
    private String stageStateinstId; // (ID)
    private String applyinstId; // (申请实例ID)
    private String stageinstId; // (串联实例ID)
    private String execStateId; // (被选择或被执行的情形定义ID)
    private String parentStateinstId; // (父情形实例ID)
    private String stateinstSeq; // (序列)
    private String creater; // (创建人)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private java.util.Date createTime; // (创建时间)
    private String modifier; // (修改人)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private java.util.Date modifyTime; // (修改时间)
    private String rootOrgId;//根组织ID
}
