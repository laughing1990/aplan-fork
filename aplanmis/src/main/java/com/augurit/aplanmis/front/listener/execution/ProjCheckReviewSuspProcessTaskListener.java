package com.augurit.aplanmis.front.listener.execution;

import com.augurit.agcloud.bpm.common.domain.ActStoAppinst;
import com.augurit.agcloud.bpm.common.engine.BpmProcessService;
import com.augurit.agcloud.bpm.common.mapper.ActStoAppinstMapper;
import com.augurit.agcloud.bpm.common.utils.SpringContextHolder;
import com.augurit.agcloud.framework.security.SecurityContext;
import com.augurit.aplanmis.common.constants.ApplyState;
import com.augurit.aplanmis.common.constants.ItemStatus;
import com.augurit.aplanmis.common.domain.AeaHiApplyinst;
import com.augurit.aplanmis.common.domain.AeaHiIteminst;
import com.augurit.aplanmis.common.domain.AeaHiParStageinst;
import com.augurit.aplanmis.common.mapper.AeaHiIteminstMapper;
import com.augurit.aplanmis.common.mapper.AeaHiParStageinstMapper;
import com.augurit.aplanmis.common.service.instance.AeaHiApplyinstService;
import org.flowable.engine.RuntimeService;
import org.flowable.engine.delegate.DelegateExecution;
import org.flowable.engine.delegate.ExecutionListener;
import org.flowable.engine.delegate.TaskListener;
import org.flowable.engine.impl.util.CommandContextUtil;
import org.flowable.task.service.delegate.DelegateTask;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

/**
 * 竣工验收进入申请人整改环节更改申报状态及挂起流程监听器
 */
@Transactional
public class ProjCheckReviewSuspProcessTaskListener implements TaskListener {

    @Override
    public void notify(DelegateTask delegateTask) {
        RuntimeService runtimeService = CommandContextUtil.getProcessEngineConfiguration().getRuntimeService();
        AeaHiApplyinstService aeaHiApplyinstService = SpringContextHolder.getBean(AeaHiApplyinstService.class);
        ActStoAppinstMapper actStoAppinstMapper = SpringContextHolder.getBean(ActStoAppinstMapper.class);
        BpmProcessService bpmProcessService = SpringContextHolder.getBean(BpmProcessService.class);

        String procInstId = delegateTask.getProcessInstanceId();

        AeaHiApplyinst aeaHiApplyinst = (AeaHiApplyinst) runtimeService.getVariable(procInstId, "form");

        String appinstId= null;
        String masterProcInstId = null;

        try {
            ActStoAppinst actStoAppinst = new ActStoAppinst();
            actStoAppinst.setMasterRecordId(aeaHiApplyinst.getApplyinstId());
            actStoAppinst.setRootOrgId(SecurityContext.getCurrentOrgId());
            List<ActStoAppinst> list = actStoAppinstMapper.listActStoAppinst(actStoAppinst);

            if (list != null && list.size() > 0) {
                appinstId = list.get(0).getAppinstId();
                masterProcInstId = list.get(0).getProcinstId();
            }

            // 更新申请状态为待复验
            aeaHiApplyinstService.updateAeaHiApplyinstStateAndInsertTriggerAeaLogItemStateHist(aeaHiApplyinst.getApplyinstId(), delegateTask.getId(), appinstId, ApplyState.WAIT_REVIEW.getValue(), null);

            //挂起流程：挂起当前流程和主流程
            bpmProcessService.suspendProcessInstanceById(masterProcInstId);//挂起当前流程
            bpmProcessService.suspendProcessInstanceById(procInstId);//挂起主流程
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
