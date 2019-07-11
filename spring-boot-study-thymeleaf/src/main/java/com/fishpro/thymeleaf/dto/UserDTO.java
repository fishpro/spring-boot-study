package com.fishpro.thymeleaf.dto;

import javax.jws.soap.SOAPBinding;
import java.util.Date;

public class UserDTO {
    private String username;
    private String sex;
    private Date birthday;

    public UserDTO(){}

    public UserDTO(String username,String sex,Date birthday){
        this.username=username;
        this.sex=sex;
        this.birthday=birthday;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public Date getBirthday() {
        return birthday;
    }

    public void setBirthday(Date birthday) {
        this.birthday = birthday;
    }
}
