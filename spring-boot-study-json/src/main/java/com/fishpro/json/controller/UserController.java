package com.fishpro.json.controller;

import com.fishpro.json.dto.Address;
import com.fishpro.json.dto.User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
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
        return  user;
    }
}
