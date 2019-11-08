package com.augurit.aplanmis.data.exchange.domain.spgl;

import com.augurit.aplanmis.data.exchange.constant.StepNameConstant;
import com.augurit.aplanmis.data.exchange.constant.TableNameConstant;
import com.augurit.aplanmis.data.exchange.domain.base.SpglItemInstEntity;
import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModel;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.Size;
import java.io.Serializable;

/**
 * @author yinlf
 * @date 2019/08/31
 * Copyright(c) 2013 广州奥格智能科技有限公司 版权所有
 */
@ApiModel
@Data
public class SpglXmspsxpfwjxxb extends SpglItemInstEntity implements Serializable {

    private static final long serialVersionUID = 1L;
    /**
     * 流水号
     */
    private String lsh;
    /**
     * 行政区划代码
     */
    private String xzqhdm;
    /**
     * 批复日期(审批事项办结（通过）时必填。格式为：yyyy-mm-dd)
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private java.util.Date pfrq;
    /**
     * 批复文号(审批事项办结（通过）时必填。)
     */
    private String pfwh;
    /**
     * 批复文件标题(审批事项办结（通过）时必填。)
     */
    private String pfwjbt;
    /**
     * 批复文件有效期限(审批事项办结（通过），并且有期限的事项时必填。格式为：yyyy-mm-dd，如果无有效期，请填写9999-01-01。)
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private java.util.Date pfwjyxqx;
    /**
     * 附件名称(附件的文件名)
     */
    private String fjmc;
    /**
     * 附件类型(附件的文件类型，如：jpg,pdf)
     */
    private String fjlx;
    /**
     * 附件内容(附件访问地址)
     */
    private String fjid;
    /**
     * 数据有效标识
     */
    @Size(max = 10)
    private Long sjyxbs;
    /**
     * 数据无效原因
     */
    private String sjwxyy;
    /**
     * 失败原因
     */
    private String sbyy;
    /**
     * 更新时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private java.util.Date gxsj;

    @Override
    @JsonIgnore
    public String getTableName() {
        return TableNameConstant.SPGL_XMSPSXPFWJXXB;
    }

    @Override
    @JsonIgnore
    public String getStepName() {
        return StepNameConstant.OFFICIAL_DOC_STEP;
    }
}
