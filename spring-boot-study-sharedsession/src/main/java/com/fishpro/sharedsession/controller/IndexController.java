package com.fishpro.sharedsession.controller;

import org.springframework.session.data.redis.config.annotation.web.http.EnableRedisHttpSession;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@EnableRedisHttpSession
@RestController
public class IndexController {

    @GetMapping("")
    public String index(HttpServletRequest request){
        request.getSession().setAttribute("username", "公众号 程序鱼");
        request.getSession().setMaxInactiveInterval(10*1000);
        String username = (String)request.getSession().getAttribute("username");

        return "username"+username+ " session_id:"+request.getSession().getId();
    }
}
