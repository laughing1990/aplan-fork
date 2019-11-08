/*
package com.augurit.aplanmis.front.listener.task;

import com.augurit.agcloud.bpm.common.domain.ActStoAppinst;
import com.augurit.agcloud.bpm.common.service.ActStoAppinstService;
import com.augurit.agcloud.bpm.common.utils.SpringContextHolder;
import com.augurit.agcloud.framework.ui.result.ResultForm;
import com.augurit.agcloud.framework.util.JsonUtils;
import com.augurit.aplanmis.common.domain.AeaHiApplyinst;
import com.augurit.aplanmis.common.domain.AeaHiSmsInfo;
import com.augurit.aplanmis.common.domain.AeaLinkmanInfo;
import com.augurit.aplanmis.common.mapper.AeaHiApplyinstMapper;
import com.augurit.aplanmis.common.mapper.AeaHiSmsInfoMapper;
import com.augurit.aplanmis.common.mapper.AeaLinkmanInfoMapper;
import com.augurit.aplanmis.common.service.instance.AeaHiApplyinstService;
import org.flowable.engine.delegate.DelegateExecution;
import org.flowable.engine.delegate.ExecutionListener;
import org.flowable.engine.delegate.TaskListener;
import org.flowable.task.service.delegate.DelegateTask;

import java.util.List;

*/
/**
 * 邮寄方式取件发送短信监听器
 *//*

public class WinQuJianSendMsgTaskListener implements TaskListener {

    @Override
    public void notify(DelegateTask delegateTask) {
        String procInstId = delegateTask.getProcessInstanceId();
        ActStoAppinstService actStoAppinstService = SpringContextHolder.getBean(ActStoAppinstService.class);
        AeaHiApplyinstMapper aeaHiApplyinstMapper = SpringContextHolder.getBean(AeaHiApplyinstMapper.class);
        AeaHiSmsInfoMapper aeaHiSmsInfoMapper = SpringContextHolder.getBean(AeaHiSmsInfoMapper.class);
        AeaHiApplyinstService aeaHiApplyinstService = SpringContextHolder.getBean(AeaHiApplyinstService.class);
        AeaLinkmanInfoMapper aeaLinkmanInfoMapper = SpringContextHolder.getBean(AeaLinkmanInfoMapper.class);
        FlowNodeSendSMSImpl flowNodeSendSMSImpl = SpringContextHolder.getBean(FlowNodeSendSMSImpl.class);
        ActStoAppinst appinst = new ActStoAppinst();
        appinst.setProcinstId(procInstId);

        try {
            List<ActStoAppinst> appinstList = actStoAppinstService.listActStoAppinst(appinst);
            if (appinstList != null && appinstList.size() > 0) {
                ActStoAppinst stoAppinst = appinstList.get(0);
                String applyinstId = stoAppinst.getMasterRecordId();
                AeaHiApplyinst aeaHiApplyinst = aeaHiApplyinstMapper.getAeaHiApplyinstById(applyinstId);
                //短信发送
                List<AeaHiSmsInfo> smsInfoList = aeaHiSmsInfoMapper.getAeaHiSmsInfoByApplyinstId(aeaHiApplyinst.getApplyinstId());
                if (smsInfoList != null && smsInfoList.size() > 0) {
                    AeaHiSmsInfo aeaHiSmsInfo = smsInfoList.get(0);
                    String receiveMode = aeaHiSmsInfo.getReceiveMode();
                    if("1".equals(receiveMode)){
                        //窗口领证
                        AeaHiApplyinst applyinst = aeaHiApplyinstService.getAeaHiApplyinstById(aeaHiApplyinst.getApplyinstId());
                        String linkManInfoId = applyinst.getLinkmanInfoId();
                        if (org.apache.commons.lang.StringUtils.isBlank(linkManInfoId)) {
                            throw new RuntimeException("找不到联系人!");
                        }
                        AeaLinkmanInfo linkman = aeaLinkmanInfoMapper.getOneById(linkManInfoId);
                        if (linkman == null) {
                            throw new RuntimeException("联系人信息为空!");
                        }
                        String phoneNo = linkman.getLinkmanMobilePhone();
                        String templateParamJson = FlowNodeSendSMSImpl.getNodeEndWinTemplateParamJson1(applyinst);
                        ResultForm resultForm = flowNodeSendSMSImpl.flowNodeSendSms(phoneNo, templateParamJson, SmsTemplateIdConstant.NODE_END_WIN);
                        System.out.println(JsonUtils.toJson(resultForm));
                    }
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
*/
