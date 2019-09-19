如何在不同站点（web服务进程）之间共享会话 Session 呢，原理很简单，就是把这个 Session 独立存储在一个地方，所有的站点都从这个地方读取 Session。

通常我们使用 Redis 来解决这个问题
- Spring Boot 2.1.6 本示例没有使用最新的 有报错
- Redis 5.0.3

![session共享方案](https://images.cnblogs.com/cnblogs_com/fishpro/1453719/o_session2.jpg)


[本项目源码 github 下载 ](https://github.com/fishpro/spring-boot-study/tree/master/spring-boot-study-sharedsession)

# 1 Redis 准备
本示例使用 Redis 5.0.3 操作系统为 Mac ，关于如何安装 redis 请自行搜索。

# 2 建立 Spring Boot 测试项目
## 2.1 新建 Spring Boot Maven 示例工程项目
注意：是用来 IDEA 开发工具
1. File > New > Project，如下图选择 `Spring Initializr` 然后点击 【Next】下一步
2. 填写 `GroupId`（包名）、`Artifact`（项目名） 即可。点击 下一步
    groupId=com.fishpro   
    artifactId=sharedsession
3. 选择依赖 `Spring Web Starter` 前面打钩。
4. 项目名设置为 `spring-boot-study-sharedsession`.

## 2.2 依赖引入 Pom
```xml
<dependencies>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-web</artifactId>
        </dependency>

        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-test</artifactId>
            <scope>test</scope>
        </dependency>

        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-data-redis</artifactId>
        </dependency>
        <dependency>
            <groupId>com.alibaba</groupId>
            <artifactId>fastjson</artifactId>
            <version>1.2.44</version>
        </dependency>
        <dependency>
            <groupId>org.springframework.session</groupId>
            <artifactId>spring-session-data-redis</artifactId>
        </dependency>
    </dependencies>
```

## 2.3 配置文件 application 中配置  
```cmd
server:
  port: 8084
#2.x版本中由于引入了不同客户端，需要指定配置哪种连接池
#jedis客户端
spring:
  cache:
    type: redis
  redis:
    host: 127.0.0.1
    port: 6379
    password:
    database: 0
    jedis:
      pool:
        max-active: 8
        max-wait: -1ms
        max-idle: 8
        min-idle: 0

```

## 2.4 加入测试代码

### 开启 RedisSession
新建 RedisSessionConfig 类，使用 @EnableRedisHttpSession(maxInactiveIntervalInSeconds = 3600) 注解，其中maxInactiveIntervalInSeconds表示默认的 Session 时间
```java
@Configuration
@EnableRedisHttpSession(maxInactiveIntervalInSeconds = 3600)
public class RedisSessionConfig {
}
```

### 编写一个 Controller 文件设置与读取 Session
```java
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
```

## 2.5 测试效果
在不同的端口下启动本项目查 输入
- http://localhost:8084/
- http://localhost:8087/

![不同站点的session共享](https://images.cnblogs.com/cnblogs_com/fishpro/1453719/o_sesseion1.jpg)