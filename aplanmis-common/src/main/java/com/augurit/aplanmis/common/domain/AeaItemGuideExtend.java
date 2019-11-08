package com.augurit.aplanmis.common.domain;

import lombok.Data;

import java.io.Serializable;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

/**
 * 事项操作扩展信息-模型
 */
@Data
public class AeaItemGuideExtend implements Serializable {

    private static final long serialVersionUID = 1L;

    private String id; // (主建)
    private String itemVerId; // (事项版本id)
    private String handleArea; // (通办范围)
    private String handleAreaText; // (通办范围文本)
    private String isExpress; // (是否支持物流快递)
    private String isExpressText; // (是否支持物流快递文本)
    private String isLimit; // (是否有数量限制)
    private String isLimitText; // (是否有数量限制文本)
    private String isSchedule; // (是否支持预约办理)
    private String isScheduleText; // (是否支持预约办理文本)
    private String limitSceneNum; // (到办事现场次数)
    private String limitSceneNumText; // (到办事现场次数文本)
    private String otherDept; // (联办机构)
    private String serviceType; // (审批服务形式)
    private String serviceTypeText; // (审批服务形式文本)
    private String sqrql; // (申请人应享有权利)
    private String sqryw; // (申请人依法履行义务)
    private String taskGuid; // (清单基本信息)
    private String xsnr; // (行使内容)
    private String xtmc; // (业务系统名称)
    private String xzfybm; // (受理行政复议部门)
    private String xzfydh; // (受理行政复议部门电话)
    private String xzfydz; // (受理行政复议地址)
    private String xzfywz; // (行政复议网址)
    private String xzssbm; // (部门（行政诉讼）)
    private String xzssdh; // (电话（行政诉讼）)
    private String xzssdz; // (地址（行政诉讼）)
    private String xzsswz; // (网址（行政诉讼）)
    private String zxckaddress; // (咨询窗口地址)
    private String ssjgwz; // (实施机关咨询网址)
    private String zxzwwbwz; // (政务微博及网址)
    private String zxwxh; // (微信号)
    private String zxdzyx; // (电子邮箱)
    /**
     * 格式：[{"filepath":"http://static.gdzwfw.gov.cn/obs/obs-sxml/txyCos43C75859298F154912720A60C38586874C9C324174250C52C742A917386046EF6294BDF9293EDA9F","neiwangPath":"http://19.15.0.24/gdsqlk/rest/frame/base/attach/attachAction/getContent?isCommondto=true&attachGuid=c99f2d53-ef77-4374-9216-1acc9e102b59","attachname":"建设用地规划许可证.pdf","attachguid":"c99f2d53-ef77-4374-9216-1acc9e102b59"}]
     */
    private String zzzResultGuid; // 许可证件样本
    private String resultName; // 许可证件名称
    private String themeNaturalType; // 主题分类
    private String themeNaturalTypeText; //主题分类文本
    private String zzzSqnr; // 申请内容
    private String zxxhyjdz;// 信函邮寄地址
    private String rootOrgId; // 根组织id

    private Long zzzResultGuidNum; // 样本数量

}
