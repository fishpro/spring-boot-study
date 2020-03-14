package com.fishpro.helloworld.controller;

//import com.fishpro.helloworld.service.MyService;  // 用于演示多模块
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * helloword示例
 * <pre>
 *     class 上方的 @RequestMapping("/hello") 表示类的路由，即类下所有方法上的路由都是在此路由下面（url）
 * </pre>
 * */
@RequestMapping("/hello")
@RestController
public class IndexController {
    // 下面代码是演示 多模块使用 在 pom.xml 中开启
//    @Autowired
//    MyService myService;
    @GetMapping("/say")
    public String say(){
        return "Hello World";
        /**
         * 下面代码是演示 多模块使用 在 pom.xml 中开启
         * <dependency>
         * 			<groupId>com.fishpro.helloworld</groupId>
         * 			<artifactId>service</artifactId>
         * 			<version>0.0.1-SNAPSHOT</version>
         * 		</dependency>
         * */
//        return  myService.message();
    }
}
