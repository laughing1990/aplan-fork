package com.augurit.aplanmis.data.exchange.domain.spgl;

import java.io.Serializable;
import javax.validation.constraints.Size;

import com.augurit.aplanmis.data.exchange.constant.StepNameConstant;
import com.augurit.aplanmis.data.exchange.constant.TableNameConstant;
import com.augurit.aplanmis.data.exchange.domain.base.SpglItemInstEntity;
import io.swagger.annotations.ApiModel;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

/**
 * @author ylf_i
 * @date 2019/12/17
 * Copyright(c) 2013 广州奥格智能科技有限公司 版权所有
 */
@ApiModel
@Data
public class SpglXmspsxbltbcxxxb extends SpglItemInstEntity implements Serializable {

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
     * 特别程序
     */
    @Size(max = 10)
    private Long tbcx;
    /**
     * 特别程序名称
     */
    private String tbcxmc;
    /**
     * 特别程序开始时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private java.util.Date tbcxkssj;
    /**
     * 特别程序时限类型
     */
    @Size(max = 10)
    private Long tbcxsxlx;
    /**
     * 特别程序时限
     */
    @Size(max = 10)
    private Long tbcxsx;
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
     * 上传时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private java.util.Date scsj;
    /**
     * 更新时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private java.util.Date gxsj;

    @Override
    public String getTableName() {
        return TableNameConstant.SPGL_XMSPSXBLTBCXXXB;
    }

    @Override
    public String getStepName() {
        return StepNameConstant.ITEM_SPECIAL_STEP;
    }
}
