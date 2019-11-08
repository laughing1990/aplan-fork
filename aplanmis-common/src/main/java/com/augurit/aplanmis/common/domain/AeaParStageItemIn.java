package com.augurit.aplanmis.common.domain;

import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.Size;
import java.io.Serializable;

/**
 * 阶段事项与输入关联定义表-模型
 */

@Data
public class AeaParStageItemIn implements Serializable {

    private static final long serialVersionUID = 1L;
    private String itemInId; // (ID)
    private String stageItemId; // (阶段事项关联ID)
    private String inId; // (事项定义ID)
    private String creater; // (创建人)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private java.util.Date createTime; // (创建时间)
    @Size(max = 10)
    private Long sortNo; // (排序字段)

}
