package com.augurit.aplanmis.data.exchange.datasource;

import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingClass;
import org.springframework.stereotype.Component;

/**
 * @author yinlf
 */
@Aspect
@Component
@ConditionalOnMissingBean(MultiDataSourceAop.class)
public class DataSourceAop {
    @Before("execution(* com.augurit.aplanmis.data.exchange.service.aplanmis.*Service.*(..))")
    public void setDataSource2Aplanmis() {
        if (DataSourceType.DataBaseType.APLANMIS != DataSourceType.getDataBaseType()) {
            DataSourceType.setDataBaseType(DataSourceType.DataBaseType.APLANMIS);
        }
    }

    @Before("execution(* com.augurit.agcloud.bsc.sc.job.service.*Service.*(..))")
    public void setDataSource2Aplanmis2() {
        if (DataSourceType.DataBaseType.APLANMIS != DataSourceType.getDataBaseType()) {
            DataSourceType.setDataBaseType(DataSourceType.DataBaseType.APLANMIS);
        }
    }

    @Before("execution(*  com.augurit.aplanmis.common.service..*Service.*(..))")
    public void setDataSource2Aplanmis3() {
        if (DataSourceType.DataBaseType.APLANMIS != DataSourceType.getDataBaseType()) {
            DataSourceType.setDataBaseType(DataSourceType.DataBaseType.APLANMIS);
        }
    }

    @Before("execution(* com.augurit.aplanmis.data.exchange.service.duogui.*Service.*(..))")
    public void setDataSource2DuoGui() {
        if (DataSourceType.DataBaseType.DUOGUI != DataSourceType.getDataBaseType()) {
            DataSourceType.setDataBaseType(DataSourceType.DataBaseType.DUOGUI);
        }
    }

    @Before("execution(* com.augurit.aplanmis.data.exchange.service.spgl.*Service.*(..))")
    public void setDataSource2Province() {
        if (DataSourceType.DataBaseType.PROVINCE != DataSourceType.getDataBaseType()) {
            DataSourceType.setDataBaseType(DataSourceType.DataBaseType.PROVINCE);
        }
    }

}