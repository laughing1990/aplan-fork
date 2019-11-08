package com.augurit.aplanmis.data.exchange.domain.base;

import lombok.Data;

/**
 * @author yinlf
 * @Date 2019/10/13
 */
@Data
public class SpglInstEntity extends SpglEntity{

    /**
     * 工程代码
     */
    protected String gcdm;

    @Override
    public String getTableName() {
        return null;
    }

    @Override
    public String getStepName() {
        return null;
    }
}
