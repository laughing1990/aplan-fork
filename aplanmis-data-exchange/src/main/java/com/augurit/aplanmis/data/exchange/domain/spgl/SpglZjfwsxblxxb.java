package com.augurit.aplanmis.data.exchange.domain.spgl;

import java.io.Serializable;
import javax.validation.constraints.Size;

import com.augurit.aplanmis.data.exchange.constant.StepNameConstant;
import com.augurit.aplanmis.data.exchange.constant.TableNameConstant;
import com.augurit.aplanmis.data.exchange.domain.base.SpglEntity;
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
public class SpglZjfwsxblxxb extends SpglEntity implements Serializable {

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
     * 项目代码
     */
    private String xmdm;
    /**
     * 中介服务事项名称
     */
    private String zjfwsxmc;
    /**
     * 中介服务事项编码
     */
    private String zjfwsxbm;
    /**
     * 对应标准中介服务事项编码
     */
    private String dybzzjfwsxbm;
    /**
     * 中介服务事项实例编码
     */
    private String zjfwsxslbm;
    /**
     * 审批事项实例编码
     */
    private String spsxslbm;
    /**
     * 办理时限
     */
    @Size(max = 10)
    private Long blsx;
    /**
     * 时限类型(代码参照附录“时限类型")
     */
    @Size(max = 10)
    private Long sxlx;
    /**
     * 中介机构代码
     */
    private String zjjgdm;
    /**
     * 中介机构名称
     */
    private String zjjgmc;
    /**
     * 委托人
     */
    private String wtr;
    /**
     * 委托人代码
     */
    private String wtrdm;
    /**
     * 数据有效标识(代码参照附录“数据有效标识”。)
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
        return TableNameConstant.SPGL_ZJFWSXBLXXB;
    }

    @Override
    public String getStepName() {
        return StepNameConstant.PROJ_PURCHASE_STEP;
    }
}
