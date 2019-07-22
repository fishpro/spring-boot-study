package com.fishpro.freemarker.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * 在是一个普通的 Controller 类
 * */
@Controller
public class IndexController {


    /**
     * 路由 /index
     * 返回 index 这里默认配置自动映射到 templages/index
     * */
    @RequestMapping("/index")
    public String index(Model model){
        model.addAttribute("welcome","hello fishpro");
        return "index";
    }
}
