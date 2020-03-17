package com.fishpro.gsondemo.controller;


import com.fishpro.gsondemo.dto.Address;
import com.fishpro.gsondemo.dto.User;
import com.google.gson.Gson;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

@RequestMapping("/user")
@RestController
public class UserController {

    @GetMapping("/get")
    public User get(){

        List<Address> addressList=new ArrayList<>();
        addressList.add(new Address("江苏省南京市玄武大道1000号","201001","1801989098"));
        addressList.add(new Address("江苏省南京市玄武大道1001号","201001","1811989098"));
        User user = new User(1, "fishpro",addressList);


        //01 基本类型解析
        Gson gson = new Gson();
        int i = gson.fromJson("99", int.class); //100
        double d = gson.fromJson("\"99.99\"", double.class);  //99.99
        boolean b = gson.fromJson("true", boolean.class);     // true
        String str = gson.fromJson("String", String.class);   // String
        System.out.println("int 类型："+i);
        System.out.println("double 类型："+d);
        System.out.println("boolean 类型："+b);
        System.out.println("String 类型："+str);

        //02 实体对象转 json str
        String rst=gson.toJson(user);
        System.out.println("实体对象转:");
        System.out.println(rst);

        //03 对象转实体 json fromJson
        User user2 = gson.fromJson(rst, User.class);
        System.out.println("对象转实体:");
        System.out.println(user2);

        return  user;
    }
}

