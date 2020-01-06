package com.augurit.aplanmis.front.listener.execution;

import com.augurit.agcloud.bpm.common.utils.SpringContextHolder;
import com.augurit.agcloud.bsc.domain.BscJobRemind;
import com.augurit.agcloud.bsc.sc.job.service.BscJobRemindService;
import com.augurit.agcloud.opus.common.domain.OpuOmUserInfo;
import com.augurit.agcloud.opus.common.service.om.OpuOmUserInfoService;
import com.augurit.aplanmis.common.domain.AeaHiApplyinst;
import com.augurit.aplanmis.common.domain.AeaProjInfo;
import com.augurit.aplanmis.common.exception.SmsException;
import com.augurit.aplanmis.common.listener.builder.BscJobRemindBuilder;
import com.augurit.aplanmis.common.mapper.AeaProjInfoMapper;
import com.augurit.aplanmis.common.shortMessage.converter.SendSmsRemindContentConverter;
import lombok.extern.slf4j.Slf4j;
import org.flowable.engine.RuntimeService;
import org.flowable.engine.delegate.TaskListener;
import org.flowable.engine.impl.util.CommandContextUtil;
import org.flowable.task.service.delegate.DelegateTask;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 窗口办结短信通知监听器
 */
@Component
@Slf4j
public class SendSmsToCkryListener implements TaskListener {
    @Autowired
    private SendSmsRemindContentConverter sendSmsTemplateJsonConverter;
    @Autowired
    private BscJobRemindBuilder bscJobRemindBuilder;
    @Autowired
    private BscJobRemindService bscJobRemindService;
    @Autowired
    private AeaProjInfoMapper aeaProjInfoMapper;
    @Autowired
    private OpuOmUserInfoService opuOmUserInfoService;

    @Override
    public void notify(DelegateTask delegateTask) {
        String procInstId = delegateTask.getProcessInstanceId();
        String assignee = delegateTask.getAssignee();
        RuntimeService runtimeService = CommandContextUtil.getProcessEngineConfiguration().getRuntimeService();

        AeaHiApplyinst aeaHiApplyinst = (AeaHiApplyinst) runtimeService.getVariable(procInstId, "form");
        String applyinstCode = aeaHiApplyinst.getApplyinstCode();
        String projInfoId = aeaHiApplyinst.getProjInfoId();
        String applyinstId = aeaHiApplyinst.getApplyinstId();
        try {
            if (assignee != null) {
                if (opuOmUserInfoService == null)
                    opuOmUserInfoService = SpringContextHolder.getBean(OpuOmUserInfoService.class);
                OpuOmUserInfo opuOmUserInfo = opuOmUserInfoService.getOpuOmUserInfoByloginName(assignee);
                if (opuOmUserInfo != null && opuOmUserInfo.getUserMobile() != null) {
                    aeaProjInfoMapper = SpringContextHolder.getBean(AeaProjInfoMapper.class);
                    AeaProjInfo aeaProjInfo = aeaProjInfoMapper.getAeaProjInfoById(projInfoId);
                    String projName = aeaProjInfo.getProjName();
                    if (sendSmsTemplateJsonConverter == null)
                        sendSmsTemplateJsonConverter = SpringContextHolder.getBean(SendSmsRemindContentConverter.class);
//                    String remindContent = sendSmsTemplateJsonConverter.getBanjieForCkryJobRemindContent(applyinstCode,projName, opuOmUserInfo.getUserMobile());
//                    if (remindContent != null)
//                        this.saveSendSmsJobRemind(opuOmUserInfo.getUserMobile(), remindContent, applyinstId);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void saveSendSmsJobRemind(String phoneNum, String remindContent, String applyinstId) {
        if (bscJobRemindBuilder == null)
            bscJobRemindBuilder = SpringContextHolder.getBean(BscJobRemindBuilder.class);
        BscJobRemind acceptJobRemind = bscJobRemindBuilder.build();
        acceptJobRemind.setRemindUserIdList(phoneNum);
        acceptJobRemind.setRemindContent(remindContent);
        acceptJobRemind.setRecordId(applyinstId);

        try {
            if (bscJobRemindService == null)
                bscJobRemindService = SpringContextHolder.getBean(BscJobRemindService.class);
            bscJobRemindService.saveBscJobRemind(acceptJobRemind);
        } catch (Exception e) {
            e.printStackTrace();
            log.info("保存短信提醒jobRemind信息失败！");
            throw new SmsException(e.getMessage());
        }
    }
}
