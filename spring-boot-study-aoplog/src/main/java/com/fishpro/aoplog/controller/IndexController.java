package com.fishpro.aoplog.controller;

import com.fishpro.aoplog.annotation.Log;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class IndexController {

    @Log("日志注解，配合WebAspect记录请求前、请求后、请求过程")
    @RequestMapping("/log")
    @ResponseBody
    public String log(String name){
        return "log";
    }
}
