package com.fishpro.ehcache.controller;

import com.fishpro.ehcache.domain.UserDO;
import com.fishpro.ehcache.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.ehcache.EhCacheCacheManager;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.support.WebApplicationContextUtils;

@RestController
public class UserController {
    @Autowired
    private UserService userService;
    @GetMapping("/test")
    public String testCache(){
        System.out.println("============以下第一次调用 ================");
        userService.list();
        userService.get(1);
        userService.save(new UserDO(1,"fishpro","123456",1));
        userService.update(new UserDO(1,"fishpro","123456434",1));

        System.out.println("============以下第二次调用 观察 list 和 get 方法 ================");

        userService.list();
        userService.get(1);
        userService.save(new UserDO(1,"fishpro","123456",1));
        userService.update(new UserDO(1,"fishpro","123456434",1));


        System.out.println("============以下第三次调用 先删除 观察 list 和 get 方法 ================");
        userService.delete(1);
        userService.list();
        userService.get(1);
        userService.save(new UserDO(1,"fishpro","123456",1));
        userService.update(new UserDO(1,"fishpro","123456434",1));
        return  "";
    }
}
