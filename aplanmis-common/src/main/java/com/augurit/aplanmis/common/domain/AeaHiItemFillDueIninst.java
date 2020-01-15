package com.augurit.aplanmis.common.domain;

import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.Size;
import java.io.Serializable;

/**
 * 事项容缺要求补齐材料实例表-模型
 */
@Data
public class AeaHiItemFillDueIninst implements Serializable {

    private static final long serialVersionUID = 1L;
    private String dueIninstId; // (ID)
    private String fillId; // (补齐ID)
    private String inoutinstId; // (输入输出实例ID)
    private String fillOpinion; // (补齐意见)
    private String timeLimitType; // (补齐截止期限类型，0表示证件领取前，1表示证件签发前，2表示证件领取后3个月)
    @Size(max = 10)
    private Long paperCount; // (要求纸质材料数量)
    @Size(max = 10)
    private Long copyCount; // (要求复印件材料数量)
    private String isNeedAtt; // (是否需要电子件：1 是，0 否)
    private String creater; // (创建人)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private java.util.Date createTime; // (创建时间)
    private String rootOrgId; // (根组织ID)
}
