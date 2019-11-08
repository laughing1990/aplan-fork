package com.augurit.aplanmis.data.exchange.domain.aplanmis;

import io.swagger.annotations.ApiModel;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.Size;
import java.io.Serializable;

/**
 * @author yinlf
 * @date 2019/09/02
 * Copyright(c) 2013 广州奥格智能科技有限公司 版权所有
 */
@ApiModel
@Data
public class EtlErrorLog implements Serializable {

    private static final long serialVersionUID = 1L;
    /**
     * 主键
     */
    @Size(max = 10)
    private Long logId;
    /**
     * 上传记录ID
     */
    private Long jobLogId;
    /**
     * 任务名称
     */
    private String jobName;
    /**
     * 步骤名称
     */
    private String stepName;
    /**
     * 业务表(视图)名称
     */
    private String tableName;
    /**
     * 业务主键
     */
    private String recordId;
    /**
     * 错误信息
     */
    private String errDesc;
    /**
     * 错误编码
     */
    private String errCode;
    /**
     * 错误序号
     */
    private String errNum;
    /**
     * 发生错误的列
     */
    private String errColumn;
    /**
     * 发生错误的值
     */
    private String errValue;
    /**
     * 程序诊断结果
     */
    private String diagnoseResult;
    /**
     * 发生错误的列
     */
    private String isSolve;
    /**
     * 发送错误的时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private java.util.Date createTime;
}
