package com.fishpro.xmldom4j.domain;

import com.fishpro.xmldom4j.util.SingleValueCalendarConverter;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamConverter;
import com.thoughtworks.xstream.annotations.XStreamImplicit;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

@XStreamAlias("user")
public class User {
    @XStreamAlias("id")
    private Integer userId;

    @XStreamAlias("username")
    private String username;

    @XStreamImplicit
    private List<Address> addresses;


    @XStreamConverter(SingleValueCalendarConverter.class)
    private Calendar created = new GregorianCalendar();


    public User(Integer userId,String username){
        this.userId=userId;
        this.username=username;
    }
    public User(Integer userId,String username,List<Address> addresses){
        this.userId=userId;
        this.username=username;
        this.addresses=addresses;
    }
    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }
}
