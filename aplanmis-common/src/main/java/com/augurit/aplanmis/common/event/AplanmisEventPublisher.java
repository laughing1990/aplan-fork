package com.augurit.aplanmis.common.event;

import com.augurit.agcloud.framework.security.SecurityContext;
import com.augurit.agcloud.framework.util.StringUtils;
import com.augurit.aplanmis.common.constants.ApplyState;
import com.augurit.aplanmis.common.constants.ItemStatus;
import com.augurit.aplanmis.common.event.def.*;
import com.augurit.aplanmis.common.event.vo.ApplyEventVo;
import com.augurit.aplanmis.common.event.vo.IteminstEventVo;
import com.augurit.aplanmis.common.exception.SmsException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@EnableConfigurationProperties
public class AplanmisEventPublisher {

    @Autowired
    private AplanmisEventProperties aplanmisEventProperties;

    @Autowired
    private ApplicationEventPublisher publisher;

    public void publishEvent(AplanmisEvent aplanmisEvent) {
        publishEvent(aplanmisEvent,false);
    }

    public void publishEvent(AplanmisEvent aplanmisEvent,boolean showException) {
        log.info("Publishing event, type: {}, desc: {}", aplanmisEvent.getEventType().getValue(), aplanmisEvent.getEventType().getDesc());

        try {
            publisher.publishEvent(aplanmisEvent);
        }catch (SmsException e){
            if(showException){
                throw e;
            }
        }
    }

    public boolean eventEnabled(AplanmisEventType eventType) {
        return aplanmisEventProperties.isEnable() && aplanmisEventProperties.contains(eventType);
    }

    public void conditionalPublishEvent(ApplyEventVo applyEventVo){

        try {

            String applyinstState = applyEventVo.getApplyinstState();

            boolean eventEnabled = false;

            if (StringUtils.isNotBlank(applyinstState)) {
                // 待补全
                if (ApplyState.IN_THE_SUPPLEMENT.getValue().equals(applyinstState)) {

                }
                // 已补全
                else if (ApplyState.SUPPLEMENTARY.getValue().equals(applyinstState)) {

                }
                // 已接件未审核(目前仅监控网厅申报件)
                else if (ApplyState.RECEIVE_UNAPPROVAL_APPLY.getValue().equals(applyinstState)) {
                    String applyinstId = applyEventVo.getApplyinstId();
                    if (eventEnabled(AplanmisEventType.APPLY_START) && StringUtils.isNotBlank(applyinstId)) {
                        eventEnabled = true;
                        publisher.publishEvent(new ApplyStartAplanmisEvent(this, applyinstId));
                    }
                }
                // 已受理
                else if (ApplyState.ACCEPT_DEAL.getValue().equals(applyinstState)) {
                    String applyinstId = applyEventVo.getApplyinstId();
                    if (eventEnabled(AplanmisEventType.APPLY_ACCEPT) && StringUtils.isNotBlank(applyinstId)) {
                        eventEnabled = true;
                        publisher.publishEvent(new ApplyAcceptAplanmisEvent(this, applyinstId, SecurityContext.getCurrentUserId()));
                    }
                }
                // 不予受理
                else if (ApplyState.OUT_SCOPE.getValue().equals(applyinstState)) {
                    if (eventEnabled(AplanmisEventType.APPLY_OUT_SCOPE)) {
                        eventEnabled = true;
                        publisher.publishEvent(new ApplyOutScopeAplanmisEvent(this));
                    }
                }
                // 已办结
                else if (ApplyState.COMPLETED.getValue().equals(applyinstState)) {
                    if (eventEnabled(AplanmisEventType.APPLY_COMPLETED)) {
                        eventEnabled = true;
                        publisher.publishEvent(new ApplyCompletedAplanmisEvent(this, applyEventVo.getApplyinstId()));
                    }
                }
                // 材料待补全
                else if (ApplyState.IN_THE_SUPPLEMENT.getValue().equals(applyinstState)) {
                    if (eventEnabled(AplanmisEventType.APPLY_MATERIAL_COMPLETION_START)) {
                        eventEnabled = true;
                        publisher.publishEvent(new ApplyMaterialCompletionStartAplanmisEvent(this, applyEventVo.getApplyinstId()));
                    }
                }
            } else {
                if (applyEventVo.isShowException()) {
                    throw new SmsException("参数有误");
                }
            }

            if (!eventEnabled && applyEventVo.isShowException()) {
                throw new SmsException("没有开放对应的短信类型");
            }
        }catch (SmsException e){
            if(applyEventVo.isShowException()){
                throw e;
            }
        }
    }

    public void conditionalPublishEvent4Iteminst(IteminstEventVo iteminstEventVo) {
        try {
            String iteminstState = iteminstEventVo.getIteminstState();

            boolean eventEnabled = false;

            if (StringUtils.isNotBlank(iteminstState)) {
                // 已接件
                if (ItemStatus.RECEIVE_APPLY.getValue().equals(iteminstState)) {

                }
                // 已撤件
                else if (ItemStatus.BACK_APPLY.getValue().equals(iteminstState)) {

                }
                // 窗口受理
                else if (ItemStatus.ACCEPT_DEAL.getValue().equals(iteminstState)) {

                }
                // 不受理
                else if (ItemStatus.REFUSE_DEAL.getValue().equals(iteminstState)) {

                }
                // 不予受理
                else if (ItemStatus.OUT_SCOPE.getValue().equals(iteminstState)) {

                }
                // 补正开始
                else if (ItemStatus.CORRECT_MATERIAL_START.getValue().equals(iteminstState)) {
                    if (eventEnabled(AplanmisEventType.APPLY_CORRECT_MATERIAL_START)) {
                        eventEnabled = true;
                        publisher.publishEvent(new ApplyCorrectMaterialStartAplanmisEvent(this,iteminstEventVo.getIteminstId()));
                    }
                }
                // 补正结束
                else if (ItemStatus.CORRECT_MATERIAL_END.getValue().equals(iteminstState)) {
                    if (eventEnabled(AplanmisEventType.APPLY_CORRECT_MATERIAL_END)) {
                        eventEnabled = true;
                        publisher.publishEvent(new ApplyCorrectMaterialEndAplanmisEvent(this));
                    }
                }
                // 部门受理
                else if (ItemStatus.DEPARTMENT_DEAL_START.getValue().equals(iteminstState)) {

                }
                // 特程开始
                else if (ItemStatus.SPECIFIC_PROC_START.getValue().equals(iteminstState)) {
                    if (eventEnabled(AplanmisEventType.APPLY_SPECIFIC_PROC_START)) {
                        eventEnabled = true;
                        publisher.publishEvent(new ApplySpecificProcStartAplanmisEvent(this));
                    }
                }
                // 特程结束
                else if (ItemStatus.SPECIFIC_PROC_END.getValue().equals(iteminstState)) {
                    if (eventEnabled(AplanmisEventType.APPLY_SPECIFIC_PROC_END)) {
                        eventEnabled = true;
                        publisher.publishEvent(new ApplySpecificProcEndAplanmisEvent(this));
                    }
                }
                // 办结通过
                else if (ItemStatus.AGREE.getValue().equals(iteminstState)) {
                    if (eventEnabled(AplanmisEventType.APPLY_AGREE)) {
                        eventEnabled = true;
                        publisher.publishEvent(new ApplyAgreeAplanmisEvent(this));
                    }
                }
                // 容缺通过
                else if (ItemStatus.AGREE_TOLERANCE.getValue().equals(iteminstState)) {
                    if (eventEnabled(AplanmisEventType.APPLY_AGREE_TOLERANCE)) {
                        eventEnabled = true;
                        publisher.publishEvent(new ApplyAgreeToleranceAplanmisEvent(this));
                    }
                }
                // 办结不通过
                else if (ItemStatus.DISAGREE.getValue().equals(iteminstState)) {
                    if (eventEnabled(AplanmisEventType.APPLY_DISAGREE)) {
                        eventEnabled = true;
                        publisher.publishEvent(new ApplyDisagreeAplanmisEvent(this));
                    }
                }
                // 撤回
                else if (ItemStatus.RECALL.getValue().equals(iteminstState)) {

                }
                // 撤销
                else if (ItemStatus.REVOKE.getValue().equals(iteminstState)) {

                }
            }else{
                if (iteminstEventVo.isShowException()) {
                    throw new SmsException("参数有误");
                }
            }

            if (!eventEnabled && iteminstEventVo.isShowException()) {
                throw new SmsException("没有开放对应的短信类型");
            }

        }catch (SmsException e){
            if(iteminstEventVo.isShowException()){
                throw e;
            }
        }


    }
}
