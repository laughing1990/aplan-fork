package com.augurit.aplanmis.data.exchange.domain.spgl;

import com.augurit.aplanmis.data.exchange.constant.StepNameConstant;
import com.augurit.aplanmis.data.exchange.constant.TableNameConstant;
import com.augurit.aplanmis.data.exchange.domain.base.SpglEntity;
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
public class SpglDfxmsplcxxb extends SpglEntity implements Serializable {

    private static final long serialVersionUID = 1L;
    /**
     *
     */
    private String lsh;

    /**
     * 行政区划代码
     */
    private String xzqhdm;
    /**
     * 审批流程编码
     */
    private String splcbm;
    /**
     * 审批流程名称
     */
    private String splcmc;
    /**
     * 审批流程版本号
     */
    @Size(max = 4)
    private Double splcbbh;
    /**
     * 审批流程生效时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private java.util.Date splcsxsj;
    /**
     * 审批流程类型
     */
    @Size(max = 10)
    private Long splclx;
    /**
     * 审批流程说明
     */
    private String splcsm;
    /**
     * 附件名称
     */
    private String fjmc;
    /**
     * 附件类型
     */
    private String fjlx;
    /**
     * 附件ID
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
        return TableNameConstant.SPGL_DFXMSPLCXXB;
    }

    @Override
    @JsonIgnore
    public String getStepName() {
        return StepNameConstant.THEME_VER_STEP;
    }
}
