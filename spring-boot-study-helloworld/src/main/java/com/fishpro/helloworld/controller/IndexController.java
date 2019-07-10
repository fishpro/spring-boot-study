package com.fishpro.helloworld.controller;

import com.fishpro.helloworld.service.MyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class IndexController {
    @Autowired
    MyService myService;
    @GetMapping("/say")
    public String say(){
        return  myService.message();
    }
}
