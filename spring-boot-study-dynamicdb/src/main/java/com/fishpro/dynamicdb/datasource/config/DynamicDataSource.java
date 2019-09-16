package com.fishpro.dynamicdb.datasource.config;

import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

/**
 * 多数据源 继承AbstractRoutingDataSource进行扩展，重新 determineCurrentLookupKey 方法实现多数据源
 *
 * @author  fishpro
 */
public class DynamicDataSource extends AbstractRoutingDataSource {

    //获取当前数据库配置中的数据源
    @Override
    protected Object determineCurrentLookupKey() {
        //peek() 获得当前线程数据源
        return DynamicContextHolder.getDataSource();
    }

}