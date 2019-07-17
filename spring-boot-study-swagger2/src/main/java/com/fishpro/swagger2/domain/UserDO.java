package com.fishpro.swagger2.domain;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(value="user对象",description="用户对象user")
public class UserDO {
    @ApiModelProperty(value="用户ID",name="userId",example="1")
    private Integer userId;
    @ApiModelProperty(value="用户名",name="userName",example="fishpro")
    private String userName;

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
}