package com.augurit.aplanmis.common.domain;

import java.io.Serializable;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

/**
 * 设立依据-模型
 */
@Data
public class AeaItemGuideAccordings implements Serializable {

    private static final long serialVersionUID = 1L;

    private String rowguid; // (主建)
    private String itemVerId; // (事项版本ID)
    private String accordingnumber; // (依据文号)
    private String bfjg; // (颁布机关)
    private String lawname; // ()
    private String lawsoucr; // ()
    private Long ordernumber; // ()
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private java.util.Date ssrq; // ()
    private String termscontent; // ()
    private String termsnumber; // ()
    private String flfgqwguid; // (法律法规全文)
    private String rootOrgId; // 根组织id
}

