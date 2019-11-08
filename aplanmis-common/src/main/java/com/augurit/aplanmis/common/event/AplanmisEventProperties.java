package com.augurit.aplanmis.common.event;

import com.google.common.collect.Sets;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.Set;

@ConfigurationProperties(prefix = "aplanmis.event")
@Configuration
@Getter
@Setter
public class AplanmisEventProperties {

    public static final Set<String> defaultTypeList = Sets.newHashSet(
            AplanmisEventType.APPLY_START.getValue(),
            AplanmisEventType.APPLY_OUT_SCOPE.getValue(),
            AplanmisEventType.APPLY_CORRECT_MATERIAL_START.getValue(),
            AplanmisEventType.APPLY_CORRECT_MATERIAL_END.getValue(),
            AplanmisEventType.APPLY_SPECIFIC_PROC_START.getValue(),
            AplanmisEventType.APPLY_SPECIFIC_PROC_END.getValue(),
            AplanmisEventType.APPLY_AGREE.getValue(),
            AplanmisEventType.APPLY_AGREE_TOLERANCE.getValue(),
            AplanmisEventType.APPLY_DISAGREE.getValue(),
            AplanmisEventType.APPLY_COMPLETED.getValue(),
            AplanmisEventType.Apply_MAIL_QU_JIAN.getValue(),
            AplanmisEventType.APPLY_ACCEPT.getValue(),
            AplanmisEventType.STATE_ITEM_COMPLETED.getValue(),
            AplanmisEventType.Apply_ACCEPT_DEAL.getValue(),
            AplanmisEventType.APPROVE_OVER_TIME.getValue(),
            AplanmisEventType.MONITOR_CASE_OVERDUE.getValue(),
            AplanmisEventType.WARNING_TASK.getValue(),
            AplanmisEventType.BPM_REMIND_SEND.getValue(),
            AplanmisEventType.BPM_NODE_SEND.getValue()
    );

    /*
    是否启用事件机制
     */
    private boolean enable = true;

    /*
    需要发送的事件列表
     */
    private Set<String> typeList = defaultTypeList;

    public boolean contains(AplanmisEventType eventType) {
        return typeList.contains(eventType.getValue());
    }
}
