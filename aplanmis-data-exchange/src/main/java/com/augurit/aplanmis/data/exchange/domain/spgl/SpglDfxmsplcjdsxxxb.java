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
public class SpglDfxmsplcjdsxxxb extends SpglEntity implements Serializable {

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
     * 审批阶段序号
     */
    @Size(max = 10)
    private Long spjdxh;
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
     * 审批事项名称
     */
    private String spsxmc;
    /**
     * 对应标准审批事项编码
     */
    private String dybzspsxbm;
    /**
     * 是否实行告知承诺制
     */
    @Size(max = 10)
    private Long sfsxgzcnz;
    /**
     * 办件类型
     */
    @Size(max = 10)
    private Long bjlx;
    /**
     * 申请对象
     */
    @Size(max = 10)
    private Long sqdx;
    /**
     * 办理结果送达方式
     */
    private String bljgsdfs;
    /**
     * 审批事项办理时限
     */
    @Size(max = 10)
    private Long spsxblsx;
    /**
     * 审批部门编码
     */
    private String spbmbm;
    /**
     * 审批部门名称
     */
    private String spbmmc;
    /**
     * 前置审批事项编码
     */
    private String qzspsxbm;
    /**
     * 是否里程碑事项
     */
    @Size(max = 10)
    private Long sflcbsx;
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

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private java.util.Date modifyTime;

    private java.util.Date startTime;

    private java.util.Date endTime;

    @Override
    @JsonIgnore
    public String getTableName() {
        return TableNameConstant.SPGL_DFXMSPLCJDSXXXB;
    }

    @Override
    @JsonIgnore
    public String getStepName() {
        return StepNameConstant.STAGE_ITEM_STEP;
    }
}
