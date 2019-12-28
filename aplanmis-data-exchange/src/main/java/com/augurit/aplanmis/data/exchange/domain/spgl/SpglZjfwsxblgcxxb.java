package com.augurit.aplanmis.data.exchange.domain.spgl;

import java.io.Serializable;
import javax.validation.constraints.Size;

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
public class SpglZjfwsxblgcxxb extends SpglEntity implements Serializable {

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
     * 中介服务事项实例编码
     */
    private String zjfwsxslbm;
    /**
     * 办理状态
     */
    private String blzt;
    /**
     * 办理人
     */
    private String blr;
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
     * 附件名称
     */
    private String fjmc;
    /**
     * 附件ID
     */
    private String fjid;
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
        return null;
    }

    @Override
    public String getStepName() {
        return null;
    }
}
