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
public class SpglXmspsxblxxxxb extends SpglItemInstEntity implements Serializable {

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
     * 办理处（科）室
     */
    private String blcs;
    /**
     * 办理人
     */
    private String blr;
    /**
     * 办理状态
     */
    @Size(max = 10)
    private Long blzt;
    /**
     * 办理意见
     */
    private String blyj;
    /**
     * 办理时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private java.util.Date blsj;
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
        return TableNameConstant.SPGL_XMSPSXBLXXXXB;
    }

    @Override
    @JsonIgnore
    public String getStepName() {
        return StepNameConstant.ITEM_OPINION_STEP;
    }
}
