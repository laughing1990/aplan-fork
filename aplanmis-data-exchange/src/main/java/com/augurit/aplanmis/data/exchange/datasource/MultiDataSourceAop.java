package com.augurit.aplanmis.data.exchange.datasource;

import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingClass;
import org.springframework.context.annotation.Conditional;
import org.springframework.stereotype.Component;

/**
 * @author yinlf
 */
@Aspect
@Component
@Conditional(MultiDataSourceCondition.class)
public class MultiDataSourceAop {
    @Before("execution(* com.augurit.aplanmis.data.exchange.service.aplanmis.*Service.*(..))")
    public void setDataSource2Aplanmis() {
        if (DataSourceType.DataBaseType.APLANMIS != DataSourceType.getDataBaseType()) {
            DataSourceType.setDataBaseType(DataSourceType.DataBaseType.APLANMIS );
        }
    }

    @Before("execution(* com.augurit.agcloud.bsc.sc.job.service.*Service.*(..))")
    public void setDataSource2Aplanmis2() {
        if (DataSourceType.DataBaseType.APLANMIS != DataSourceType.getDataBaseType()) {
            DataSourceType.setDataBaseType(DataSourceType.DataBaseType.APLANMIS );
        }
    }

    @Before("execution(* com.augurit.aplanmis.data.exchange.service.spgl.*Service.*(..))")
    public void setDataSource2Province() {
        DataSourceType.setDataBaseType(DataSourceType.getSmallType());
    }

}