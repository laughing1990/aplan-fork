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
public class SpglXmqqyjxxb extends SpglEntity implements Serializable {

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
     * 项目代码
     */
    private String xmdm;
    /**
     * 前期意见实例编码
     */
    private String qqyjslbm;
    /**
     * 办理单位代码
     */
    private String bldwdm;
    /**
     * 办理单位名称
     */
    private String bldwmc;
    /**
     * 反馈时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private java.util.Date fksj;
    /**
     * 办理人
     */
    private String blr;
    /**
     * 前期意见
     */
    private String qqyj;
    /**
     * 附件名称
     */
    private String fjmc;
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

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private java.util.Date modifyTime;

    private java.util.Date startTime;

    private java.util.Date endTime;

    @Override
    @JsonIgnore
    public String getTableName() {
        return TableNameConstant.SPGL_XMQQYJXXB;
    }

    @Override
    @JsonIgnore
    public String getStepName() {
        return StepNameConstant.PRE_IDEA_STEP;
    }
}
