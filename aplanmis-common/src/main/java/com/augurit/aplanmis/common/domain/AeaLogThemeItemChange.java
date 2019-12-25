package com.augurit.aplanmis.common.domain;
import java.io.Serializable;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;
/**
* 主题及事项申报变更记录表-模型
<ul>
    <li>项目名：奥格工程建设审批系统</li>
    <li>版本信息：v4.0</li>
    <li>版权所有(C)2019广州奥格智能科技有限公司-版权所有</li>
    <li>创建人:Apple</li>
    <li>创建时间：2019-12-25 11:56:41</li>
</ul>
*/
@Data
public class AeaLogThemeItemChange implements Serializable{
        private static final long serialVersionUID = 1L;
        private java.lang.String changeId; // (主键)
        private java.lang.String projInfoId; // (项目ID)
        private java.lang.String isApplyinstTrigger; // (是否由申报实例触发变更，0表示直接修改变更，1表示发起申报导致变更)
        private java.lang.String applyinstId; // (申报实例ID【IS_APPLYINST_TRIGGER=1】)
        private java.lang.String stageinstId; // (并联阶段实例ID【IS_APPLYINST_TRIGGER=1】)
        private java.lang.String isThemeChange; // (是否变更主题，0表示变更事项，1表示变更主题)
        private java.lang.String changeAction; // (变更操作，c表示change，a表示add，d表示delete)
        private java.lang.String oldThemeId; // (旧主题ID【IS_THEME_CHANGE=1且CHANGE_ACTION=c】)
        private java.lang.String oldThemeName; // (旧主题名称【IS_THEME_CHANGE=1且CHANGE_ACTION=c】)
        private java.lang.String newThemeId; // (新主题ID【IS_THEME_CHANGE=1且CHANGE_ACTION=c】)
        private java.lang.String newThemeName; // (新主题名称【IS_THEME_CHANGE=1且CHANGE_ACTION=c】)
        private java.lang.String oldIteminstId; // (旧事项实例ID【IS_THEME_CHANGE=0且CHANGE_ACTION=c或d】)
        private java.lang.String oldIteminstName; // (旧事项实例名称或描述【IS_THEME_CHANGE=0且CHANGE_ACTION=c或d】)
        private java.lang.String newIteminstId; // (新事项实例ID【IS_THEME_CHANGE=0且CHANGE_ACTION=c或a】)
        private java.lang.String newIteminstName; // (新事项实例名称或描述【IS_THEME_CHANGE=0且CHANGE_ACTION=c或a】)
        private java.lang.String opsOrgId; // (操作部门ID)
        private java.lang.String opsOrgName; // (操作部门名称)
        private java.lang.String opsUserId; // (操作用户ID)
        private java.lang.String opsUserName; // (操作用户名称)
        private java.lang.String opsOpinion; // (意见内容)
    @DateTimeFormat(pattern="yyyy-MM-dd")
        private java.util.Date opsTime; // (操作时间)
        private java.lang.String opsMemo; // (操作备注说明)
        private java.lang.String creater; // (创建人)
    @DateTimeFormat(pattern="yyyy-MM-dd")
        private java.util.Date createTime; // (创建时间)
        private java.lang.String rootOrgId; // (根组织ID)

}
