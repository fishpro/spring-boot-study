package com.fishpro.jpa.domain;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;

@Entity
@Table(name="t_user_role")
@IdClass(UserRoleKey.class) //注意这里是引入了 定义的符合主键类
public class UserRoleDO {
    @Id
    private Integer userId;
    @Id
    private Integer roleId;

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Integer getRoleId() {
        return roleId;
    }

    public void setRoleId(Integer roleId) {
        this.roleId = roleId;
    }
}
