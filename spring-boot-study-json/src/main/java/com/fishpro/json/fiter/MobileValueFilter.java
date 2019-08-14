package com.fishpro.json.fiter;

import com.alibaba.fastjson.serializer.ValueFilter;

public class MobileValueFilter implements ValueFilter {
    public Object process(Object object, String name, Object value) {
        // 只要字段名中包含mobile，则值输出为一串星号
        if (name.toLowerCase().contains("mobile")) {
            return "****";
        }
        return value;
    }
}
