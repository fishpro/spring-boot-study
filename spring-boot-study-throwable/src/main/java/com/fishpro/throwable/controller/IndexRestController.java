package com.fishpro.throwable.controller;

import jdk.nashorn.internal.runtime.regexp.joni.exception.ErrorMessages;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class IndexRestController {
    @RequestMapping("/index")
    public Map index(){
        Map<String,Object> map=new HashMap<>();
        map.put("status","0");
        map.put("msg","正常的输出");
        return map;
    }

    /**
     * 这里人为手动抛出一个异常
     * */
    @RequestMapping("/err")
    public Map err(){

        throw new RuntimeException("抛出一个异常");
    }

    /**
     * 这里抛出的是 RuntimeException 不可查异常，虽然没有使用 try-catch 来捕捉 但系统以及帮助我们抛出了一次
     * */
    @RequestMapping("/matcherr")
    public Map matcherr(){

        Map<String,Object> map=new HashMap<>();
        map.put("status","0");
        map.put("msg","正常的输出");
        int j=0;
        Integer i=9/j;
        return map;
    }

    /**
     * 这里抛出的是 RuntimeException 不可查异 注意这里使用了 try-catch 来捕捉异常，但没有抛出异常
     * */
    @RequestMapping("/nocatch")
    public Map nocatch(){

        Map<String,Object> map=new HashMap<>();
        map.put("status","0");
        map.put("msg","正常的输出 注意这里使用了 try-catch 来捕捉异常，但没有抛出异常，所以没有异常，因为这里抛出的是 RuntimeException 不可查异常，系统也不会报错。");
        int j=0;
        try{
            Integer i=9/j;
        }catch (Exception ex){

        }
        return map;
    }
}
