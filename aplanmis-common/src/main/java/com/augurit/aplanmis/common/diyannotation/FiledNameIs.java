package com.augurit.aplanmis.common.diyannotation;

import java.lang.annotation.*;

/**
 * 自定义注解,添加在属性上
 * 用于记录住建相关表属性变更信息
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface FiledNameIs {
    /**
     * 属性值
     * @return
     */
    String filedValue() default "";
}
