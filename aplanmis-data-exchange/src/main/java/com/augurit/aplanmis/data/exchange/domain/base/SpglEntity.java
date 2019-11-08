package com.augurit.aplanmis.data.exchange.domain.base;

import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.Size;
import java.util.Date;

/**
 * @author yinlf
 * @Date 2019/9/27
 */
public abstract class SpglEntity {
    /**
     * 地方数据主键
     */
    protected String dfsjzj;

    /**
     * 数据上传状态
     */
    @Size(max = 10)
    protected Long sjsczt;

    /**
     * 上传时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    protected java.util.Date scsj;

    public String getDfsjzj() {
        return dfsjzj;
    }

    public void setDfsjzj(String dfsjzj) {
        this.dfsjzj = dfsjzj;
    }

    public Long getSjsczt() {
        return sjsczt;
    }

    public void setSjsczt(Long sjsczt) {
        this.sjsczt = sjsczt;
    }

    public Date getScsj() {
        return scsj;
    }

    public void setScsj(Date scsj) {
        this.scsj = scsj;
    }

    public abstract String getTableName();

    public abstract String getStepName();
}
