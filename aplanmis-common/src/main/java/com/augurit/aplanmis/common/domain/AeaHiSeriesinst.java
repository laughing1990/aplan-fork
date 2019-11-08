package com.augurit.aplanmis.common.domain;

import java.io.Serializable;
import java.util.Map;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

/**
 * 串联申报实例表-模型
 * <ul>
 * <li>项目名：奥格工程建设审批系统</li>
 * <li>版本信息：v4.0</li>
 * <li>版权所有(C)2019广州奥格智能科技有限公司-版权所有</li>
 * <li>创建人:Administrator</li>
 * <li>创建时间：2019-07-04 16:45:21</li>
 * </ul>
 */
@Data
public class AeaHiSeriesinst implements Serializable {
// ----------------------------------------------------- Properties

    private static final long serialVersionUID = 1L;
    private String seriesinstId; // (ID)
    private String appinstId; // (（行政服务中心）业务流程模板实例ID)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private java.util.Date startTime; // (开始时间)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private java.util.Date endTime; // (结束时间)
    private String seriesinstState; // (状态)
    private String seriesinstMemo; // (备注说明)
    private String creater; // (创建人)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private java.util.Date createTime; // (创建时间)
    private String modifier; // (修改人)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private java.util.Date modifyTime; // (修改时间)
    private String applyinstId; // (申请实例ID)
    private String rootOrgId;//根组织ID
    private String stageId; // (所属阶段定义)
    private String isParallel;//是否并行推进事项。0表示否，1表示是

    private String stageName; // (所属阶段名称)
    private String applyinstState;//其申请实例状态
}
