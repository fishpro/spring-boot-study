package com.fishpro.ehcache;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

// @EnableCaching 开启缓存
@SpringBootApplication
@EnableCaching
public class EhcacheApplication {

    public static void main(String[] args) {

        SpringApplication.run(EhcacheApplication.class, args);
    }

}
