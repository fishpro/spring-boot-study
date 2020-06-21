当我们需要在web页面实时接收到推送信息，我们可以用拉的方式，或者推的方式进行。
- 编写一个javascript定时器，定时获取服务器信息
- 使用 websocket 进行实时获取
本文演示的就是使用websocket进行获取信息。

**本文仅仅适用**
- spring boot 2.x
- jdk 1.8+

本将建立一个服务端 websocket，并使用一个 rest api 进行消息方式。使用一个thymeleaf的页面进行js接收

[本项目源码下载](https://github.com/fishpro/spring-boot-study/tree/master/spring-boot-study-websocketdemo)

# 1 新建 Spring Boot Maven 示例工程项目
注意：是用来 IDEA 开发工具
1. File > New > Project，如下图选择 `Spring Initializr` 然后点击 【Next】下一步
2. 填写 `GroupId`（包名）、`Artifact`（项目名） 即可。点击 下一步
    groupId=com.fishpro   
    artifactId=websocketdemo
3. 选择依赖 `Spring Web Starter` 前面打钩。
4. 项目名设置为 `spring-boot-study-websocketdemo`.
   
# 2 引入依赖 Pom

- websocket
- thymeleaf

```xml
 <?xml version="1.0" encoding="UTF-8"?>
 <project xmlns="http://maven.apache.org/POM/4.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
          xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 https://maven.apache.org/xsd/maven-4.0.0.xsd">
     <modelVersion>4.0.0</modelVersion>
     <parent>
         <groupId>org.springframework.boot</groupId>
         <artifactId>spring-boot-starter-parent</artifactId>
         <version>2.3.1.RELEASE</version>
         <relativePath/> <!-- lookup parent from repository -->
     </parent>
     <groupId>com.fishpro</groupId>
     <artifactId>websocketdemo</artifactId>
     <version>0.0.1-SNAPSHOT</version>
     <name>websocketdemo</name>
     <description>Demo project for Spring Boot</description>
 
     <properties>
         <java.version>1.8</java.version>
     </properties>
 
     <dependencies>
         <dependency>
             <groupId>org.springframework.boot</groupId>
             <artifactId>spring-boot-starter-web</artifactId>
         </dependency>
 
         <!-- SpringBoot集成thymeleaf模板 -->
         <dependency>
             <groupId>org.springframework.boot</groupId>
             <artifactId>spring-boot-starter-thymeleaf</artifactId>
         </dependency>
 
 
         <dependency>
             <groupId>org.springframework.boot</groupId>
             <artifactId>spring-boot-starter-websocket</artifactId>
         </dependency>
         <dependency>
             <groupId>org.springframework.boot</groupId>
             <artifactId>spring-boot-starter-test</artifactId>
             <scope>test</scope>
             <exclusions>
                 <exclusion>
                     <groupId>org.junit.vintage</groupId>
                     <artifactId>junit-vintage-engine</artifactId>
                 </exclusion>
             </exclusions>
         </dependency>
     </dependencies>
 
     <build>
         <plugins>
             <plugin>
                 <groupId>org.springframework.boot</groupId>
                 <artifactId>spring-boot-maven-plugin</artifactId>
             </plugin>
         </plugins>
     </build>
 
 </project>

```


# 3 WebSocketConfig 配置
新建 com.fishpro.websocketdemo.config 包，新建 WebSocketConfig 文件，代码如下
```java
package com.fishpro.websocketdemo.config;


import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.AbstractWebSocketMessageBrokerConfigurer;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

/**
 * 通过EnableWebSocketMessageBroker 开启使用STOMP协议来传输基于代理(message broker)的消息,此时浏览器支持使用@MessageMapping 就像支持@RequestMapping一样。
 * AbstractWebSocketMessageBrokerConfigurer
 */
@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) { //endPoint 注册协议节点,并映射指定的URl

        //注册一个名字为"endpointChat" 的endpoint,并指定 SockJS协议。   点对点-用
        registry.addEndpoint("/endpointChat").withSockJS();
    }


    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {//配置消息代理(message broker)
        //点对点式增加一个/queue 消息代理
        registry.enableSimpleBroker("/queue", "/topic");

    }
}

```

# 4 建立前端页面
## 4.1 建立 template下的 index.html
注意静态文件包括了 jquery.js sockjs.min.js stomp.min.js 已放入项目代码中

```html
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta
            content="width=device-width, initial-scale=1.0, maximum-scale=1.0, user-scalable=0"
            name="viewport" />
    <meta content="yes" name="apple-mobile-web-app-capable" />
    <meta content="black" name="apple-mobile-web-app-status-bar-style" />
    <meta name="wap-font-scale" content="no" />
    <title>testing</title>
</head>
<body style="background: none; overflow: hidden;">

<div class="hot-map" id="hot">
</div>

<script type="text/javascript"
        th:src="@{/assets/libs/webSocket/jquery.js}"></script>
<script type="text/javascript"
        th:src="@{/assets/libs/webSocket/sockjs.min.js}"></script>
<script type="text/javascript"
        th:src="@{/assets/libs/webSocket/stomp.min.js}"></script>
<script type="text/javascript">
    function connectNotify() {
        var sock = new SockJS("/endpointChat");
        var stomp = Stomp.over(sock);
        stomp.connect('guest', 'guest', function(frame) {
            stomp.subscribe("/queue/bigtopic/hot", handleNotification);
        });
        function handleNotification(message) {
            $("#hot").append(message+"<br/>");
            console.log(message);
        }
    }

    function handleAjaxResult(result) {
        //do something
    }

    $(function() {
        connectNotify();

    });
</script>

</body>
</html>
```
## 4.2 建立 controller 下的 IndexController
建立HomeController 控制层代码，主要了前端路由，消息发送接口

```java
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


```

## 4.3 运行

右键 RedisApplication 选择 Run RedisApplication 在浏览器中输入 http://localhost:8080/index

使用 http://localhost:8080/send 发送消息，就可以在index页面接收到消息。

![spring boot websocket 注解运行效果](http://www.fishpro.com.cn/wp-content/uploads/2020/06/spring-websocketdemo-1024x609.png)

