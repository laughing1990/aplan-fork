package com.augurit.aplanmis.data.exchange.domain.aplanmis;

import io.swagger.annotations.ApiModel;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;

/**
 * @author yinlf
 * @date 2019/09/02
 * Copyright(c) 2013 广州奥格智能科技有限公司 版权所有
 */
@ApiModel
@Data
public class EtlJob implements Serializable {

    private static final long serialVersionUID = 1L;
    /**
     * 作业ID
     */
    private String jobId;
    /**
     * 作业名称
     */
    private String jobName;
    /**
     * 运行状态(0:运行结束，1:开始运行)
     */
    private String runStatus;
    /**
     * 运行开始时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private java.util.Date startTime;
    /**
     * 运行结束时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private java.util.Date endTime;
}
