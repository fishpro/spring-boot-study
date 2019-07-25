package com.fishpro.mybatis.controller;


import com.fishpro.mybatis.domain.UserDO;
import com.fishpro.mybatis.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * RESTful API + Mybatis 风格示例 对资源 user 进行操作
 * 本示例没有使用数据库，也没有使用 service 类来辅助完成，所有操作在本类中完成
 * 请注意几天
 *    1.RESTful 风格使用 HttpStatus 状态返回 GET PUT PATCH DELETE 通常返回 201 Create ，DELETE 还有时候返回 204 No Content
 *    2.使用 RESTful 一定是要求具有幂等性，GET PUT PATCH DELETE 本身具有幂等性，但 POST 不具备，无论规则如何定义幂等性，需要根据业务来设计幂等性
 *    3.RESTful 不是神丹妙药，实际应根据实际情况来设计接口
 * */
@RestController
@RequestMapping("/api/user")
public class UserController {

    @Autowired
    private UserService userService;

    /**
     * 测试用 参数为 name
     * */
    @RequestMapping("/hello")
    public String hello(String name){
        return "Hello "+name;
    }

    /**
     * SELECT 查询操作，返回一个JSON数组
     * 具有幂等性
     * */
    @GetMapping("/users")
    @ResponseStatus(HttpStatus.OK)
    public Object getUsers(){

        return userService.list(new HashMap<>());
    }

    /**
     * SELECT 查询操作，返回一个新建的JSON对象
     * 具有幂等性
     * */
    @GetMapping("/users/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Object getUser(@PathVariable("id") String id){

        if(null==id){
            return  null;
        }
        return userService.get(Integer.valueOf(id));
    }

    /**
     * 新增一个用户对象
     * 非幂等
     * 返回 201 HttpStatus.CREATED 对创建新资源的 POST 操作进行响应。应该带着指向新资源地址的 Location 头
     * */
    @PostMapping("/users")
    @ResponseStatus(HttpStatus.CREATED)
    public Object addUser(@RequestBody UserDO user){
        if(user.getId()==0){
            return null;
        }
        userService.save(user);
        return user;
    }

    /**
     * 编辑一个用户对象
     * 幂等性
     * */
    @PutMapping("/users/{id}")
    @ResponseStatus(HttpStatus.CREATED)
    public Object editUser(@PathVariable("id") String id, @RequestBody UserDO user){

        userService.update(user);
        return user;
    }

    /**
     * 删除一个用户对象
     * 幂等性
     * 返回 HttpStatus.NO_CONTENT 表示无返回内容
     * */
    @DeleteMapping("/users/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteUser(@PathVariable("id") String id){
        userService.remove(Integer.valueOf(id));
    }
}


