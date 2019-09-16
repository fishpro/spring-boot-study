package com.fishpro.dynamicdb.datasource.annotation;

import java.lang.annotation.*;

/**
 * 多数据源注解
 * 在方法名上加入 DataSource('名称')
 *
 * @author fishpro
 * */
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
public @interface DataSource {
    String value() default "";
}
