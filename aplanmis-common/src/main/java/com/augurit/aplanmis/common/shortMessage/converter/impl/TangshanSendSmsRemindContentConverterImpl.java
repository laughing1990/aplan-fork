package com.augurit.aplanmis.common.shortMessage.converter.impl;

import com.augurit.aplanmis.common.shortMessage.constant.TangshanSmsTemplateIdConstant;
import com.augurit.aplanmis.common.shortMessage.converter.SendSmsRemindContentConverter;
import com.augurit.aplanmis.common.shortMessage.utils.SendSmsUtil;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.Map;

/**
 * 唐山市短信模板转换实现类
 * author:sam
 */
@Service
@Transactional
public class TangshanSendSmsRemindContentConverterImpl implements SendSmsRemindContentConverter {

    /**
     * 事项审批待办提醒
     * @return
     */
    @Override
    public String getBacklogJobRemindContent(String applyinstCode, String projName, String taskName, String dueNum, String phoneNum) {
        //您有一个新的待办事项，项目名称为{ProjName},申请编号为{applyinstCode}。当前环节为{taskName},还有{dueNum}天超时，请尽快处理。

        Map<String,String> map = new HashMap<>();
        map.put("applyinstCode",applyinstCode);
        map.put("ProjName",projName);
        map.put("taskName",taskName);
        map.put("dueNum",dueNum);
        String templateParamJson = SendSmsUtil.getTemplateParam(map);

        String result = SendSmsUtil.getSmsJobRemindContent(phoneNum,TangshanSmsTemplateIdConstant.APPROVE_NEW_NOTICE,templateParamJson);

        return result;
    }

    /**
     * 窗口受理提醒（提醒业主）
     * @return
     */
    @Override
    public String getWinAcceptJobRemindContent(String applyDate,String projName,String applyinstCode,String taskName,String phoneNum) {
        //您于{applyDate}提交的申报申请已受理，项目名称为{ProjName},申请编号为{applyinstCode}。当前环节为{taskName}。

        Map<String,String> map = new HashMap<>();
        map.put("applyDate",applyDate);
        map.put("ProjName",projName);
        map.put("applyinstCode",applyinstCode);
        map.put("taskName",taskName);
        String templateParamJson = SendSmsUtil.getTemplateParam(map);

        String result = SendSmsUtil.getSmsJobRemindContent(phoneNum,TangshanSmsTemplateIdConstant.USER_ACCEPT,templateParamJson);
        return result;
    }

    /**
     * 办结快递邮寄查收提醒（提醒业主）
     * @return
     */
    @Override
    public String getNodeEndMailJobRemindContent(String overDate,String projName,String applyinstCode,String transCode,String phoneNum) {
        //您申请的事项已于{overDate}办结，项目名称为{ProjName},申请编号为{applyinstCode}。办理结果将通过快递形式发送，单号：{transCode}，请注意查收。
        Map<String,String> map = new HashMap<>();
        map.put("overDate",overDate);
        map.put("ProjName",projName);
        map.put("applyinstCode",applyinstCode);
        map.put("transCode",transCode);
        String templateParamJson = SendSmsUtil.getTemplateParam(map);

        String result = SendSmsUtil.getSmsJobRemindContent(phoneNum,TangshanSmsTemplateIdConstant.NODE_END_EXPRESS,templateParamJson);
        return result;
    }

    /**
     * 办结窗口领取提醒（提醒业主）
     * @return
     */
    @Override
    public String getNodeEndWinJobRemindContent(String overDate,String projName,String applyinstCode,String phoneNum) {
        //您申请的事项已于{overDate}办结，项目名称为{ProjName},申请编号为{applyinstCode}。请凭申请编号于3个工作日内到窗口领取结果。

            Map<String,String> map = new HashMap<>();
            map.put("overDate",overDate);
            map.put("ProjName",projName);
            map.put("applyinstCode",applyinstCode);
            String templateParamJson = SendSmsUtil.getTemplateParam(map);

            String result = SendSmsUtil.getSmsJobRemindContent(phoneNum,TangshanSmsTemplateIdConstant.NODE_END_WIN,templateParamJson);
        return result;
    }

    /**
     * 材料补正提醒（提醒业主）
     * @return
     */
    @Override
    public String getBuzhengJobRemindContent(String applyDate,String projName,String applyinstCode,String phoneNum) {
        //您{applyDate}的申报申请需要补全材料，项目名称为{ProjName},申请编号为{applyinstCode}。请凭申请编号到窗口提交材料，提交材料前，项目审批将暂停。

        Map<String,String> map = new HashMap<>();
        map.put("applyDate",applyDate);
        map.put("ProjName",projName);
        map.put("applyinstCode",applyinstCode);
//        map.put("itemName",itemName);
//        map.put("orgName",phoneNum);
        String templateParamJson = SendSmsUtil.getTemplateParam(map);
        String result = SendSmsUtil.getSmsJobRemindContent(phoneNum,TangshanSmsTemplateIdConstant.MAT_PROOFREAD,templateParamJson);
        return result;
    }

    /**
     * 网厅申报成功提醒（提醒业主）
     * @return
     */
    @Override
    public String getNetApplyJobRemindContent(String applyDate,String projName,String applyinstCode,String phoneNum) {
        //您{applyDate}的申报申请已提交，项目名称为{ProjName},申请编号为{applyinstCode}。

        Map<String,String> map = new HashMap<>();
        map.put("applyDate",applyDate);
        map.put("ProjName",projName);
        map.put("applyinstCode",applyinstCode);
        String templateParamJson = SendSmsUtil.getTemplateParam(map);

        String result = SendSmsUtil.getSmsJobRemindContent(phoneNum,TangshanSmsTemplateIdConstant.USER_PIECE,templateParamJson);
        return result;
    }

    /**
     * 审批超时提醒
     * @return
     */
    @Override
    public String getOverTimeJobRemindContent(String dueNum,String projName,String applyinstCode,String taskName,String phoneNum) {
        //您的待办事项还有{dueNumLeft}天超时，项目名称为{ProjName},申请编号为{applyinstCode}。当前环节为{taskName},请尽快处理。
        Map<String,String> map = new HashMap<>();
        map.put("dueNumLeft",dueNum);
        map.put("ProjName",projName);
        map.put("applyinstCode",applyinstCode);
        map.put("taskName",taskName);
        String templateParamJson = SendSmsUtil.getTemplateParam(map);
        String result = SendSmsUtil.getSmsJobRemindContent(phoneNum,TangshanSmsTemplateIdConstant.APPROVE_OVERTIME,templateParamJson);
        return result;
    }

    /**
     * 办件预警提醒（未实现）
     * @return
     */
    @Override
    public String getWaringJobRemindContent(String dueNum, String projName, String applyinstCode, String taskName, String phoneNum) {
        return null;
    }

    /**
     * 超时默许提醒
     * @return
     */
    @Override
    public String getTacitlyJobRemindContent(String projName,String applyinstCode,String phoneNum) {
        //根据唐山工程建设项目政策文件，您的待办事项，项目名称为{ProjName},申请编号为{applyinstCode}，已超期，系统已默认同意通过，请知悉。
        Map<String,String> map = new HashMap<>();
        map.put("ProjName",projName);
        map.put("applyinstCode",applyinstCode);
        String templateParamJson = SendSmsUtil.getTemplateParam(map);
        String result = SendSmsUtil.getSmsJobRemindContent(phoneNum,TangshanSmsTemplateIdConstant.TACITLY_EXPRESS,templateParamJson);
        return result;
    }

    /**
     * 流程催办短信提醒
     * @param projName 项目名称
     * @param projCode 项目代码
     * @param applyinstCode 申报流水号
     * @param phoneNum 手机号
     * @return
     */
    @Override
    public String getBpmRemindSmsContent(String projName, String projCode, String applyinstCode, String phoneNum) {

        //do something  各地市自己实现

        return null;
    }
}
