package com.augurit.aplanmis.data.exchange.domain.spgl;

import com.augurit.aplanmis.data.exchange.constant.StepNameConstant;
import com.augurit.aplanmis.data.exchange.constant.TableNameConstant;
import com.augurit.aplanmis.data.exchange.domain.base.SpglInstEntity;
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
public class SpglXmspsxblxxb extends SpglInstEntity implements Serializable {

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
     * 审批事项编码
     */
    private String spsxbm;
    /**
     * 审批事项版本号
     */
    @Size(max = 4)
    private Double spsxbbh;
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
     * 审批事项实例编码
     */
    private String spsxslbm;
    /**
     * 审批部门编码
     */
    private String spbmbm;
    /**
     * 审批部门名称
     */
    private String spbmmc;
    /**
     * 受理方式
     */
    @Size(max = 10)
    private Long slfs;
    /**
     * 公开方式
     */
    @Size(max = 10)
    private Long gkfs;
    /**
     * 并联审批实例编码
     */
    private String blspslbm;
    /**
     * 事项办理时限
     */
    @Size(max = 10)
    private Long sxblsx;
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
        return TableNameConstant.SPGL_XMSPSXBLXXB;
    }

    @Override
    @JsonIgnore
    public String getStepName() {
        return StepNameConstant.ITEMINST_STEP;
    }
}
