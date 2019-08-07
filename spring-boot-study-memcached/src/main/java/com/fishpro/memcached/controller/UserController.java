package com.fishpro.memcached.controller;

import net.spy.memcached.MemcachedClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;

@RestController
public class UserController {

    @Autowired
    private MemcachedClient memcachedClient;


    @RequestMapping("/test")
    public  String test(){
        System.out.println("======set/get 方式演示===============================");
        memcachedClient.set("FPCACHE",3,"THIS IS TEST 这是测试");
        System.out.println("设置与读取 FPCACHE 值:"+memcachedClient.get("FPCACHE"));

        memcachedClient.set("FPCACHE",3,"使用SET添加到一个存在的值的缓存");
        System.out.println("再次读取 FPCACHE 值:"+memcachedClient.get("FPCACHE"));

        System.out.println("======add 方式演示===============================");
        memcachedClient.add("FPCACHE",3,"使用ADD添加到一个存在的值的缓存");
        System.out.println("再次读取 FPCACHE 值:"+memcachedClient.get("FPCACHE"));

        memcachedClient.add("FPCACHE2",3,"使用ADD添加到新的缓存键FPCACHE2中");
        System.out.println("再次读取 FPCACHE2 值:"+memcachedClient.get("FPCACHE2"));

        System.out.println("======replace 方式演示===============================");
        memcachedClient.replace("FPCACHE",3,"使用Replace替换FPCACHE键对应的缓存值");
        System.out.println("replace方式读取 FPCACHE 值:"+memcachedClient.get("FPCACHE"));

        try {
            Thread.sleep(3001);
        }catch (Exception ex){}
        System.out.println("3秒过后再次获取缓存 FPCACHE: "+memcachedClient.get("FPCACHE"));

        System.out.println("======delete 方式演示===============================");
        memcachedClient.delete("FPCACHE");
        System.out.println("replace方式读取 FPCACHE 值:"+memcachedClient.get("FPCACHE"));

        return "";
    }
}
