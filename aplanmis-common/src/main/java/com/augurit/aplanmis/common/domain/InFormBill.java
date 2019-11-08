package com.augurit.aplanmis.common.domain;

import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 告知单
 */
@Data
public class InFormBill implements Serializable {
    private static final long serialVersionUID = 1L;

    private String projName;     //项目名称
    private String projCode;     //项目代码
    private String unitName;     //申请单位（项目法人单位）
    private String itemId;       //事项id
    private String itemName;     //事项名称
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date informDate;    // 补正通知时间
    private String stageName;    //当前阶段/环节

    private Map<String, List<String>> itemMatMap; //key-事项名称，value-事项下需补正的材料名称
}
