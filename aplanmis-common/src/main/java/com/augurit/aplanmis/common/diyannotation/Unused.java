package com.augurit.aplanmis.common.diyannotation;

import java.lang.annotation.*;

/**
 * 标记未使用到的方法
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.SOURCE)
@Documented
public @interface Unused {

    String value() default "";
}
