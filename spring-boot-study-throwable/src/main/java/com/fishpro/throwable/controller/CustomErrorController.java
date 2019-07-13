package com.fishpro.throwable.controller;

import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

public class CustomErrorController implements ErrorController {

    private static final String ERROR_PATH = "/error";

    @RequestMapping(
            value = {ERROR_PATH},
            produces = {"text/html"}
    )

    /**
     * 用户 Controller 带返回的
     * */
    public ModelAndView errorHtml(HttpServletRequest request, HttpServletResponse response) {
        int code = response.getStatus();
        if (404 == code) {
            return new ModelAndView("error/404");
        } else if (403 == code) {
            return new ModelAndView("error/403");
        } else {
            return new ModelAndView("error/500");
        }

    }

    @RequestMapping(value = ERROR_PATH)
    public Map handleError(HttpServletRequest request, HttpServletResponse response) {
        Map<String,Object> map=new HashMap<>();
        int code = response.getStatus();
        if (404 == code) {
            map.put("status",404);
            map.put("msg","未找到资源文件");
        } else if (403 == code) {
            map.put("status",403);
            map.put("msg","没有访问权限");
        } else if (401 == code) {
            map.put("status",401);
            map.put("msg","登录过期");
        } else {
            map.put("status",500);
            map.put("msg","服务器错误");
        }
        return  map;
    }

    @Override
    public String getErrorPath(){return ERROR_PATH;}
}
