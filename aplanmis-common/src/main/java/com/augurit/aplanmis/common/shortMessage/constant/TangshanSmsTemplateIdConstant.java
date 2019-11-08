package com.augurit.aplanmis.common.shortMessage.constant;

/**
 * 唐山电信短信模板ID
 */
public class TangshanSmsTemplateIdConstant {

    /**
     * 审批新提醒
     * 您有一个新的待办事项，项目名称为{ProjName},申请编号为{applyinstCode}。当前环节为{taskName},还有{dueNum}天超时，请尽快处理。
     */
    public static String APPROVE_NEW_NOTICE ="91555528";
    /**
     * 用户受理
     * 您于{applyDate}提交的申报申请已受理，项目名称为{ProjName},申请编号为{applyinstCode}。当前环节为{taskName}。
     */
    public static String USER_ACCEPT ="91555533";
    /**
     * 用户报件
     * 您{applyDate}的申报申请已提交，项目名称为{ProjName},申请编号为{applyinstCode}。
     */
    public static String USER_PIECE ="91555532";

    /**
     * 材料补正
     * 您{applyDate}的申报申请需要补全材料，项目名称为{ProjName},申请编号为{applyinstCode}。请凭申请编号到窗口提交材料，提交材料前，项目审批将暂停。
     */
    public static String MAT_PROOFREAD ="91555534";

    /**
     * 审批超时提醒
     * 您的待办事项还有{dueNumLeft}天超时，项目名称为{ProjName},申请编号为{applyinstCode}。当前环节为{taskName},请尽快处理。
     */
    public static String APPROVE_OVERTIME ="91555531";

    /**
     * 用户办结窗口领取
     * 您申请的事项已于{overDate}办结，项目名称为{ProjName},申请编号为{applyinstCode}。请凭申请编号于3个工作日内到窗口领取结果。
     */
    public static String NODE_END_WIN ="91555535";
    /**
     * 用户办结快递查收
     * 您申请的事项已于{overDate}办结，项目名称为{ProjName},申请编号为{applyinstCode}。办理结果将通过快递形式发送，单号：{transCode}，请注意查收。
     */
    public static String NODE_END_EXPRESS ="91555536";

    /**
     * 超期默许提醒
     * 根据唐山工程建设项目政策文件，您的待办事项，项目名称为{ProjName},申请编号为{applyinstCode}，已超期，系统已默认同意通过，请知悉。
     */
    public static String TACITLY_EXPRESS ="91555727";
}
