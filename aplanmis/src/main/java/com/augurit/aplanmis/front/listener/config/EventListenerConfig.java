package com.augurit.aplanmis.front.listener.config;

import com.augurit.agcloud.framework.util.StringUtils;
import com.augurit.aplanmis.common.event.def.ItemCompletedEvent;
import com.augurit.aplanmis.common.event.def.ApplyAcceptAplanmisEvent;
import com.augurit.aplanmis.common.service.project.AeaProjStageService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;

/**
 * @author yinlf
 * @Date 2019/9/3
 */
@Configuration
@Slf4j
public class EventListenerConfig {

    @Autowired
    AeaProjStageService service;

    /**
     * 事项办结事件监听器
     *
     * @param itemCompletedEvent
     */
    @EventListener
    @Async
    public void handleItemCompletedEvent(ItemCompletedEvent itemCompletedEvent) {
        log.info("监听到事项办结事件已发布,发布源为：{}",itemCompletedEvent.getSource());
        service.itemCompletedUpdateProjStageState(itemCompletedEvent.getIteminstId(),itemCompletedEvent.getOrgId(),itemCompletedEvent.getUserId());
    }

    /**
     * 阶段发起申报事件监听器
     *
     * @param stageStartApplyEvent
     */
    @EventListener
    @Async
    public void handleItemCompletedEvent(ApplyAcceptAplanmisEvent stageStartApplyEvent) {
        log.info("监听到阶段发起申报事件已发布,发布源为：{}",stageStartApplyEvent.getSource());
        if(StringUtils.isNotBlank(stageStartApplyEvent.getApplyinstId())){
            service.stageApplyUpdateAeaProjStageState(stageStartApplyEvent.getApplyinstId(),stageStartApplyEvent.getUserId());
        }else{
            String[] projInfoIds = stageStartApplyEvent.getProjInfoIds();
            for(String projInfoId:projInfoIds){
                service.stageApplyUpdateAeaProjStageState(stageStartApplyEvent.getStageId(),projInfoId,stageStartApplyEvent.getUserId());
            }
        }
    }

}
