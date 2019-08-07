本章学习 Mmecached 在 Spring Boot 中的使用教程。Memcached 与 Redis 各有好处。本文主要学习 Spring Boot 中如何应用集成 Mmecached

- spring boot 1.5.x/2.x
- memcached
- jdk 1.8+

# 1 安装 memcached
window 下安装比较方便，直接 双击 exe 安装文件即可；

mac 下安装使用命令行安装
```bash
brew install libmemcached
brew install memcached
rew services start memcached
```
注意，如果遇到 update .... 需要等待很久的，就请按组合键 command+c 取消brew的更新，直接执行命令。

# 2 新建 Spring Boot Maven 示例工程项目 
注意：是用来 IDEA 开发工具
1. File > New > Project，如下图选择 `Spring Initializr` 然后点击 【Next】下一步
2. 填写 `GroupId`（包名）、`Artifact`（项目名） 即可。点击 下一步
    groupId=com.fishpro   
    artifactId=memcached
3. 选择依赖 `Spring Web Starter` 前面打钩。
4. 项目名设置为 `spring-boot-study-memcached`.

# 3 引入依赖 Pom
```xml
<dependencies>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-web</artifactId>
        </dependency>
        <!-- https://mvnrepository.com/artifact/net.spy/spymemcached add by fishpro at 2019-08-07-->
        <dependency>
            <groupId>net.spy</groupId>
            <artifactId>spymemcached</artifactId>
            <version>2.12.3</version>
        </dependency>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-test</artifactId>
            <scope>test</scope>
        </dependency>
    </dependencies>
```

# 4 配置 Memcached 对 MemcachedClient进行初始化
MemcacheConfig 路径 src/main/java/com/fishpro/memcached/config/RedisController（路径.java）
```java
package com.fishpro.memcached.config;

import net.spy.memcached.MemcachedClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CachingConfigurerSupport;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;
import java.net.InetSocketAddress;

@EnableCaching
@Configuration
public class MemcacheConfig extends CachingConfigurerSupport {

    @Value("${memcached.port}")
    private Integer port;
    @Value("${memcached.ip}")
    private String ip;
    @Bean
    public MemcachedClient memcachedClient(){
        try {
            return new MemcachedClient(new InetSocketAddress(ip,port));
        }catch (IOException e){
            e.printStackTrace();
            return null;
        }
    }
}

```

# 5 编写RestController测试代码
不太习惯使用单元测试，这里给出 RestController 的测试代码

|方法|作用|
|---|---|
|set(key,value)|向key中添加值如果存就替换|
|add(key,value)|向key中添加值如果存在就不替换|
|replace(key,value)|替换缓存key的值为value|
|delete(key)|删除缓存key的值|

注意生效时间单位秒

```java
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


```

# 6 运行示例

右键 MemcachedApplication 选择 Run MemcachedApplication 在浏览器中输入 http://localhost:8080/test


----
参考
[官方 Memcached github](https://github.com/memcached/memcached)