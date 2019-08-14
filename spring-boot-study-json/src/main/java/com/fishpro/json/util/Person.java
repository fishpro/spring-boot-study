package com.fishpro.json.util;

import com.alibaba.fastjson.annotation.JSONField;

import java.util.Date;

public class Person {

    @JSONField(name = "age")
    private int age;

    @JSONField(name = "full_name",ordinal = 1)
    private String fullName;

    @JSONField(name = "date_of_birth",format = "yyyy-MM-dd")
    private Date dateOfBirth;

    @JSONField(serialize = false)
    private String alias;

    private String mobile;

    public Person(int age, String fullName, Date dateOfBirth) {
        super();
        this.age = age;
        this.fullName= fullName;
        this.dateOfBirth = dateOfBirth;
        this.mobile="18000000000";
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public Date getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(Date dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }
}
