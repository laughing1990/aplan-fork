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
public class SpglDfxmsplcjdxxb extends SpglEntity implements Serializable {

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
     * 审批流程版本号
     */
    @Size(max = 4)
    private Double splcbbh;
    /**
     * 审批阶段编码
     */
    private String spjdbm;
    /**
     * 审批阶段名称
     */
    private String spjdmc;
    /**
     * 审批阶段序号
     */
    @Size(max = 10)
    private Long spjdxh;
    /**
     * 对应标准审批阶段序号
     */
    private String dybzspjdxh;
    /**
     * 审批阶段时限
     */
    @Size(max = 10)
    private Long spjdsx;
    /**
     * 里程碑事项类型
     */
    @Size(max = 10)
    private Long lcbsxlx;
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
        return TableNameConstant.SPGL_DFXMSPLCJDXXB;
    }

    @Override
    @JsonIgnore
    public String getStepName() {
        return StepNameConstant.STAGE_STEP;
    }
}
