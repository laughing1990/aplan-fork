package com.augurit.aplanmis.data.exchange.datasource;

import com.augurit.agcloud.framework.util.StringUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Condition;
import org.springframework.context.annotation.ConditionContext;
import org.springframework.core.env.Environment;
import org.springframework.core.type.AnnotatedTypeMetadata;
import org.springframework.stereotype.Component;

/**
 * @author yinlf
 * @Date 2019/10/17
 */
@Slf4j
@Component
public class MultiDataSourceCondition implements Condition {
    @Override
    public boolean matches(ConditionContext context, AnnotatedTypeMetadata metadata) {
        Environment environment = context.getEnvironment();
        String property = environment.getProperty("aplanmis.data.exchange.analyse.open");
        if(StringUtils.isNotBlank(property)){
            try {
                Boolean multi = Boolean.valueOf(property);
                return multi;
            }catch (Exception e){
                log.error("配置属性aplanmis.data.exchange.analyse.open出错");
            }
        }
        return false;
    }
}
