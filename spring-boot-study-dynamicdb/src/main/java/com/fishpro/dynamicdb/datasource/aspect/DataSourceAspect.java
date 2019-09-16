package com.fishpro.dynamicdb.datasource.aspect;

import com.fishpro.dynamicdb.datasource.annotation.DataSource;
import com.fishpro.dynamicdb.datasource.config.DynamicContextHolder;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;


/**
 * 多数据源，切面处理类
 *
 */
@Aspect
@Component
@Order(Ordered.HIGHEST_PRECEDENCE)
public class DataSourceAspect {
    protected Logger logger = LoggerFactory.getLogger(getClass());

    /**
     * 切面点 指定注解
     * */
    @Pointcut("@annotation(com.fishpro.dynamicdb.datasource.annotation.DataSource) " +
            "|| @within(com.fishpro.dynamicdb.datasource.annotation.DataSource)")
    public void dataSourcePointCut() {

    }

    /**
     * 拦截方法指定为 dataSourcePointCut
     * */
    @Around("dataSourcePointCut()")
    public Object around(ProceedingJoinPoint point) throws Throwable {
        MethodSignature signature = (MethodSignature) point.getSignature();
        Class targetClass = point.getTarget().getClass();
        Method method = signature.getMethod();

        DataSource targetDataSource = (DataSource)targetClass.getAnnotation(DataSource.class);
        DataSource methodDataSource = method.getAnnotation(DataSource.class);
        if(targetDataSource != null || methodDataSource != null){
            String value;
            if(methodDataSource != null){
                value = methodDataSource.value();
            }else {
                value = targetDataSource.value();
            }

            DynamicContextHolder.setDataSource(value);
            logger.debug("set datasource is {}", value);
        }

        try {
            return point.proceed();
        } finally {
            DynamicContextHolder.clearDataSource();
            logger.debug("clean datasource");
        }
    }
}