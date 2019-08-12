package com.fishpro.webservice.dto;

/**
 * 用户传输实体对象 通常我们把展示层与服务层之间传输的对象使用Dto后缀来标识。
 * */
public class UserDto {

    private Integer userId;//用户id
    private String userName;//用户名称
    private String password;//用户密码
    private Integer sex;//用户性别 0女 1男

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
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
}
