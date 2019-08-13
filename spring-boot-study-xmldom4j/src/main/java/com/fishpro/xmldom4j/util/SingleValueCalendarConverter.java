package com.fishpro.xmldom4j.util;

import com.thoughtworks.xstream.converters.Converter;
import com.thoughtworks.xstream.converters.MarshallingContext;
import com.thoughtworks.xstream.converters.SingleValueConverter;
import com.thoughtworks.xstream.converters.UnmarshallingContext;
import com.thoughtworks.xstream.converters.basic.AbstractSingleValueConverter;
import com.thoughtworks.xstream.io.HierarchicalStreamReader;
import com.thoughtworks.xstream.io.HierarchicalStreamWriter;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 * 日期转换器
 * */
public class SingleValueCalendarConverter implements Converter {

    public void marshal(Object source, HierarchicalStreamWriter writer,
                        MarshallingContext context) {
        Calendar calendar = (Calendar) source;
        writer.setValue(String.valueOf(calendar.getTime().getTime()));
    }

    public Object unmarshal(HierarchicalStreamReader reader,
                            UnmarshallingContext context) {
        GregorianCalendar calendar = new GregorianCalendar();
        calendar.setTime(new Date(Long.parseLong(reader.getValue())));
        return calendar;
    }

    public boolean canConvert(Class type) {
        return type.equals(GregorianCalendar.class);
    }
}