package com.fishpro.websocketdemo.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@RequestMapping("")
@Controller
public class HomeController {

    @Autowired
    private SimpMessagingTemplate template;
    private String prefix = "";

    /**
     * 前端路由
     * */
    @GetMapping("/index")
    public String index() {
        return prefix + "/index";
    }

    /**
     * 发送模板
     * */
    @GetMapping("/send")
    @ResponseBody
    public String send(){

        template.convertAndSend("/queue/bigtopic/hot", "hot");
        return "hot";
    }
}

