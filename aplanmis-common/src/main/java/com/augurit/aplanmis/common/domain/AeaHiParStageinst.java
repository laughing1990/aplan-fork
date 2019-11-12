package com.augurit.aplanmis.common.domain;

import java.io.Serializable;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

/**
 * 阶段/环节实例表-模型
 * <ul>
 * <li>项目名：奥格工程建设审批系统</li>
 * <li>版本信息：v4.0</li>
 * <li>版权所有(C)2019广州奥格智能科技有限公司-版权所有</li>
 * <li>创建人:Administrator</li>
 * <li>创建时间：2019-07-04 16:45:17</li>
 * </ul>
 */
@Data
public class AeaHiParStageinst implements Serializable {
// ----------------------------------------------------- Properties

    private static final long serialVersionUID = 1L;
    private String stageinstId; // (ID)
    private String themeVerId; // (主题实例ID)
    private String stageId; // (阶段定义ID)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private java.util.Date startTime; // (开始时间)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private java.util.Date endTime; // (结束时间)
    private String stageinstState; // 阶段状态：已废除
    private String stageinstMemo; // (备注说明)
    private String creater; // (创建人)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private java.util.Date createTime; // (创建时间)
    private String modifier; // (修改人)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private java.util.Date modifyTime; // (修改时间)
    private String appinstId; // (（行政服务中心）业务流程模板实例ID)
    private String applyinstId; // (申请实例ID)
    private String businessState; // (（业务状态）business_state:1、申办，2、预审（网上预受理），3、受理，4、审批，5、补正告知，6、补正受理，7、办结，8、制证，9、领取登记，10 征求中、11 无需征求、12 已征求)
    private String rootOrgId;//根组织ID
    //  扩展字段
    private String stageName; // (阶段名称)
    private Integer sortNo;
    @Size(max = 22)
    private Double dueNum; // (承诺办结时限数字)
    private String dueUnit; // (承诺办结时限单位)
    private String iconCss; // (图标CSS样式)
    private String bgCss; // (背景CSS样式)
    /**
     * 是否分情形
     */
    private String isNeedState;

    private String queryThemeVerIds;//查询主题版本ID。多个ID用单引号和逗号拼接成字符串

    private String themeName;//主题名称
    private String applyinstState;//其申请实例的状态

    private String handWay;//办理方式 0 多事项直接合并办理  1 按阶段多级情形组织事项办理

}
