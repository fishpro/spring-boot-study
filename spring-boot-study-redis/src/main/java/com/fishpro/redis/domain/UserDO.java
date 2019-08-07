package com.fishpro.redis.domain;

import java.io.Serializable;
import java.util.Date;

public class UserDO  implements Serializable {
    private Integer id;
    private String userName;
    private String password;
    private Integer sex;
    private Date lastLoginTime;

    public  UserDO(){}

    public UserDO(Integer id,String userName,String  password,Integer sex){
        this.id=id;
        this.userName=userName;
        this.password=password;
        this.sex=sex;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Integer getSex() {
        return sex;
    }

    public void setSex(Integer sex) {
        this.sex = sex;
    }

    public Date getLastLoginTime() {
        return lastLoginTime;
    }

    public void setLastLoginTime(Date lastLoginTime) {
        this.lastLoginTime = lastLoginTime;
    }
}