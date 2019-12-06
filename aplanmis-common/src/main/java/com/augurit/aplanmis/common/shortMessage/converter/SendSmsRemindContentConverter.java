package com.augurit.aplanmis.common.shortMessage.converter;

/**
 * 短信发送提醒内容JSON转换基类
 * author:sam
 */
public interface SendSmsRemindContentConverter {


    /**
     * 事项审批待办提醒
     * @return
     */
    public String getBacklogJobRemindContent(String applyinstCode, String projName, String taskName, String dueNum, String phoneNum);

    /**
     * 窗口受理提醒（提醒业主）
     * @return
     */
    public String getWinAcceptJobRemindContent(String applyDate,String projName,String applyinstCode,String taskName,String phoneNum);

    /**
     * 材料补全提醒（提醒业主）
     * @return
     */
    public String getBuQuanJobRemindContent(String applyDate,String projName,String applyinstCode,String taskName,String phoneNum);

    /**
     * 办结快递邮寄查收提醒（提醒业主）
     * @return
     */
    public String getNodeEndMailJobRemindContent(String overDate,String projName,String applyinstCode,String transCode,String phoneNum);

    /**
     * 办结窗口领取提醒（提醒业主）
     * @return
     */
    public String getNodeEndWinJobRemindContent(String overDate,String projName,String applyinstCode,String phoneNum);

    /**
     * 材料补正提醒（提醒业主）
     * @return
     */
    public String getBuzhengJobRemindContent(String applyDate,String projName,String applyinstCode,String phoneNum);

    /**
     * 网厅申报成功提醒（提醒业主）
     * @return
     */
    public String getNetApplyJobRemindContent(String applyDate,String projName,String applyinstCode,String phoneNum);

    /**
     * 审批超时提醒
     * @return
     */
    public String getOverTimeJobRemindContent(String dueNum,String projName,String applyinstCode,String taskName,String phoneNum);

    /**
     * 办件预警提醒
     * @return
     */
    public String getWaringJobRemindContent(String dueNum,String projName,String applyinstCode,String taskName,String phoneNum);

    /**
     *  超时默许提醒（仅用于唐山）
     * @return
     */
    public String getTacitlyJobRemindContent(String projName,String applyinstCode,String phoneNum);

    /**
     * 流程催办短信提醒
     * @param projName 项目名称
     * @param projCode 项目代码
     * @param applyinstCode 申报流水号
     * @param phoneNum 手机号
     * @return
     */
    public String getBpmRemindSmsContent(String projName,String projCode,String applyinstCode,String phoneNum);

    /**
     * 办件容缺办结补正短信提醒
     * @param projName 项目名称
     * @param projCode 项目代码
     * @param applyinstCode 申报流水号
     * @param itemName 事项名称
     * @param phoneNum 手机号
     * @return
     */
    public String getToleranceBzRemindSmsContent(String projName,String projCode,String applyinstCode,String itemName,String phoneNum);

}
