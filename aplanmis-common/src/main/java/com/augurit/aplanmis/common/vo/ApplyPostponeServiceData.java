package com.augurit.aplanmis.common.vo;

import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;

/**
 * 申请结束服务延期的请求数据
 * @author tiantian
 * @date 2019/6/27
 */
@Data
public class ApplyPostponeServiceData implements Serializable {

    @NotBlank(message = "采购需求Id")
    private String projPurchaseId;
    /**
     * 服务结束时间
     */
    @NotNull(message = "服务结束时间不能为空")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    private Date serviceEndTime;



}
