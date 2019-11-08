package com.augurit.aplanmis.data.exchange.domain.aplanmis;

import java.io.Serializable;
import javax.validation.constraints.Size;

import io.swagger.annotations.ApiModel;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

/**
 * @author yinlf
 * @date 2019/09/02
 * Copyright(c) 2013 广州奥格智能科技有限公司 版权所有
 */
@ApiModel
@Data
public class EtlJobDetailLog implements Serializable {

    private static final long serialVersionUID = 1L;
    /**
     * 主键
     */
    @Size(max = 10)
    private Long detailLogId;
    /**
     * 上传记录ID
     */
    @Size(max = 10)
    private Long jobLogId;
    /**
     * 上传表名
     */
    private String tableName;
    /**
     * 读取数据
     */
    @Size(max = 10)
    private Long readNum;
    /**
     * 写入数据
     */
    @Size(max = 10)
    private Long writtenNum;
    /**
     * 发生错误数
     */
    @Size(max = 10)
    private Long errorNum;
    /**
     * 创建时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private java.util.Date createTime;
}
