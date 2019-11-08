package com.augurit.agcloud.bpm.front.service;

import com.augurit.agcloud.bpm.common.domain.ActStoRemind;
import com.augurit.agcloud.bpm.common.domain.ActStoRemindReceiver;
import com.augurit.agcloud.bpm.front.domain.ActStoRemindForm;
import com.augurit.agcloud.bpm.front.domain.BpmTaskAndUser;

import java.util.List;

public interface BpmFrontRemindService {

    /**
     * 根据流程实例ID获取当前审批节点的审批人集合
     * @param processInstanceId 流程实例ID
     * @return
     */
    public List<BpmTaskAndUser> getTaskUsersByProcessInstanceId(String processInstanceId) throws Exception;

    /**
     * 保存流程督办信息（保存督办主表和从表）
     * @param actStoRemind
     * @param receivers
     * @return
     * @throws Exception
     */
    public boolean saveProcessRemind(ActStoRemind actStoRemind, List<ActStoRemindReceiver> receivers) throws Exception;

    /**
     * 保存流程督办信息（保存督办主表和从表）并保存提醒消息
     * @param actStoRemind
     * @param receivers
     * @return
     * @throws Exception
     */
    public boolean saveProcessRemindAndAoaMsg(ActStoRemind actStoRemind, List<ActStoRemindReceiver> receivers, String projName,String applyinstCode) throws Exception;

    /**
     * 保存流程督办信息（保存督办主表和从表）并保存提醒消息
     * @param actStoRemindForms
     * @return
     * @throws Exception
     */
    public boolean saveProcessRemindAndAoaMsgs(List<ActStoRemindForm> actStoRemindForms, String projName,String applyinstCode) throws Exception;

    /**
     * 根据流程实例ID获取模板名称
     * @return
     */
    public String getTplAppNameByProcessInstanceId(String procInstId) throws Exception;
}
