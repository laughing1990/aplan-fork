package com.augurit.aplanmis.common.vo.conditional;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;
import java.util.Set;

/**
 * @author tiantian
 * @date 2019/9/4
 */
@Data
@ApiModel("多条件查询参数")
public class ConditionalQueryRequest implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "代码类型,0为项目代码,1为工程编码")
    private String projCodeType;
    @ApiModelProperty(value = "查询代码")
    private String projCode;
    @ApiModelProperty(value = "项目名称")
    private String projName;
    @ApiModelProperty(value = "单位")
    private String ownerUnit;
    @ApiModelProperty(value = "联系人")
    private String linkman;
    @ApiModelProperty(value = "所在区域")
    private String region;
    @ApiModelProperty(value = "立项类型")
    private String projType;
    @ApiModelProperty(value = "投资类型")
    private String investCategory;
    @ApiModelProperty(value = "行业分类", notes = "多个以,隔开")
    private String industry;
    @ApiModelProperty(value = "建设性质")
    private String buildNature;
    @ApiModelProperty(value = "审批阶段")
    private String approvalStage;
    @ApiModelProperty(value = "查询开始时间")
    @Deprecated
    private String startTime;
    @ApiModelProperty(value = "查询结束时间")
    @Deprecated
    private String endTime;
    @ApiModelProperty(value = "项目级别")
    private String projLevel;
    @ApiModelProperty(value = "申报状态")
    private String applyState;
    @ApiModelProperty(value = "申报流水号")
    private String applyinstCode;
    @ApiModelProperty(value = "单位类型")
    private String unitType;
    @ApiModelProperty(value = "事项实例状态")
    private String iteminstState;
    @ApiModelProperty(value = "当前登录名", hidden = true)
    private String loginName;
    @ApiModelProperty(value = "当前组织id", hidden = true)
    private String currentOrgId;
    @ApiModelProperty(value = "是否只查询委办局或所属窗口的记录", notes = "true为只查询委办局或所属窗口的记录")
    private boolean entrust;
    @ApiModelProperty(value = "是否只查询自己经手的记录", notes = "true为只查询自己经手的记录")
    private boolean handler;
    @ApiModelProperty(value = "行业分类数组", hidden = true)
    private String[] industries;
    @ApiModelProperty(value = "窗口Id", hidden = true)
    private String windowId;
    @ApiModelProperty(value = "登录用户Id", hidden = true)
    private String userId;

    @ApiModelProperty(value = "事项名称")
    private String iteminstName;

    @ApiModelProperty(value = "所在窗口/部门人员列表", hidden = true)
    private List<String> userList;

    @ApiModelProperty(value = "综合查询关键字")
    private String keyword;

    @ApiModelProperty(value = "申报来源")
    private String applySource;

    @ApiModelProperty(value = "申报类型")
    private String applyType;

    @ApiModelProperty(value = "主题")
    private String theme;

    @ApiModelProperty(value = "业务状态")
    private String instState;

    @ApiModelProperty(value = "申报开始时间")
    private String applyStartTime;
    @ApiModelProperty(value = "申报结束时间")
    private String applyEndTime;

    @ApiModelProperty(value = "到达开始时间")
    private String arriveStartTime;
    @ApiModelProperty(value = "到达结束时间")
    private String arriveEndTime;

    @ApiModelProperty(value = "受理开始时间")
    private String acceptStartTime;
    @ApiModelProperty(value = "受理结束时间")
    private String acceptEndTime;

    @ApiModelProperty(value = "办理开始时间")
    private String processStartTime;
    @ApiModelProperty(value = "办理结束时间")
    private String processEndTime;

    @ApiModelProperty(value = "办结开始时间")
    private String concludedStartTime;
    @ApiModelProperty(value = "办结结束时间")
    private String concludedEndTime;

    @ApiModelProperty(value = "不予受理开始时间")
    private String dismissedStartTime;
    @ApiModelProperty(value = "不予受理结束时间")
    private String dismissedEndTime;

    @ApiModelProperty(value = "设定补正/补全开始时间")
    private String correctDueStartTime;
    @ApiModelProperty(value = "设定补正/补全结束时间")
    private String correctDueEndTime;

    @ApiModelProperty(value = "实施主体id")
    private String organizerId;

    @ApiModelProperty(value = "补正/补全开始时间")
    private String correctStartTime;
    @ApiModelProperty(value = "补正/补全结束时间")
    private String correctEndTime;

    @ApiModelProperty(value = "特殊程序查询开始时间")
    private String specialStartTime;
    @ApiModelProperty(value = "特殊程序查询结束时间")
    private String specialEndTime;
    @ApiModelProperty(value = "取件方式1窗口0邮寄")
    private String receiveMode;

    @ApiModelProperty(value = "事项性质大分类")
    private String itemNature;

    @ApiModelProperty(value = "当前用户所属组织ID列表",hidden = true)
    private Set<String> currentUserOrgIdList;

    @ApiModelProperty(value = "对应国家标准辅线服务")
    private String sublineType;

    @ApiModelProperty(value = "是否已办结,1为是")
    private String isConcluding;

    @ApiModelProperty(value = "阶段排序号")
    private Long stageIndex;

    @ApiModelProperty(value = "当前用户所属组织ID和上级ID",hidden = true)
    private Set<String> selfAndParentOrgIdList;


}
