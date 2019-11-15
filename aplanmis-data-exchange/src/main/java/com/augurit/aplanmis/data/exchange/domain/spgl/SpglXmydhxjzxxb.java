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
 * @author qhg
 * @date 2019/11/04
 * Copyright(c) 2013 广州奥格智能科技有限公司 版权所有
 */
@ApiModel
@Data
public class SpglXmydhxjzxxb extends SpglEntity implements Serializable {

    private static final long serialVersionUID = 1L;
    /**
     *流水号
     */
    private String lsh;
    /**
     * 行政区划代码
     */
    private String xzqhdm;
    /**
     * 项目代码
     */
    private String xmdm;
    /**
     * 界址文件序号
     */
    private Long jzwjxh;
    /**
     * 界址文件名称
     */
    private String jzwjmc;
    /**
     * 界址文件说明
     */
    private String jzwjsm;
    /**
     * 界址文件附件
     */
    private Byte[] jzwjfj;
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
        return TableNameConstant.SPGL_XMYDHXJZXXB;
    }

    @Override
    @JsonIgnore
    public String getStepName() {
        return StepNameConstant.LAND_RED_LINE_STEP;
    }
}
