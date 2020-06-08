Spring Boot  的便捷，无出其右，仍然是三部曲，创建springboot 项目，配置项目，编写示例代码。
安装 Kafka 测试环境请参加： [https://blog.csdn.net/fishpro/article/details/105761986](https://blog.csdn.net/fishpro/article/details/105761986)

本示例代码[https://github.com/fishpro/spring-boot-study/tree/master/spring-boot-study-kafka](https://github.com/fishpro/spring-boot-study/tree/master/spring-boot-study-kafka)

# 1 新建 Spring Boot Maven 示例工程项目
注意：是用来 IDEA 开发工具
1. `File > New > Project`，如下图选择 `Spring Initializr` 然后点击 【Next】下一步
2. 填写 `GroupId`（包名）、`Artifact`（项目名） 即可。点击 下一步
    groupId=com.fishpro   
    artifactId=kafka
3. 选择依赖 `Spring Web Starter` 前面打钩。
4. 项目名设置为 `spring-boot-study-kafka`.

# 2 引入依赖 Pom

```xml
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 https://maven.apache.org/xsd/maven-4.0.0.xsd">
    <modelVersion>4.0.0</modelVersion>
    <parent>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-parent</artifactId>
        <version>2.3.0.RELEASE</version>
        <relativePath/> <!-- lookup parent from repository -->
    </parent>
    <groupId>com.fishpro</groupId>
    <artifactId>kafka</artifactId>
    <version>0.0.1-SNAPSHOT</version>
    <name>kafka</name>
    <description>Demo project for Spring Boot</description>

    <properties>
        <java.version>1.8</java.version>
    </properties>

    <dependencies>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-actuator</artifactId>
        </dependency>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-web</artifactId>
        </dependency>

        <dependency>
            <groupId>org.springframework.kafka</groupId>
            <artifactId>spring-kafka</artifactId>
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
        <dependency>
            <groupId>org.springframework.kafka</groupId>
            <artifactId>spring-kafka-test</artifactId>
            <scope>test</scope>
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
    <repositories>
        <repository>
            <id>public</id>
            <name>aliyun nexus</name>
            <url>http://maven.aliyun.com/nexus/content/groups/public/</url>
            <releases>
                <enabled>true</enabled>
            </releases>
        </repository>
    </repositories>

</project>


```
# 3 application 配置

```yml

spring:
  kafka:
    # 指定kafka server的地址，集群配多个，中间，逗号隔开
    bootstrap-servers: 127.0.0.1:9092
    # 生产者
    producer:
      # 写入失败时，重试次数。当leader节点失效，一个repli节点会替代成为leader节点，此时可能出现写入失败，
      # 当retris为0时，produce不会重复。retirs重发，此时repli节点完全成为leader节点，不会产生消息丢失。
      retries: 0
      # 每次批量发送消息的数量,produce积累到一定数据，一次发送
      batch-size: 16384
      # produce积累数据一次发送，缓存大小达到buffer.memory就发送数据
      buffer-memory: 33554432
      # 指定消息key和消息体的编解码方式
      key-serializer: org.apache.kafka.common.serialization.StringSerializer
      value-serializer: org.apache.kafka.common.serialization.StringSerializer
      properties:
        linger.ms: 1
    # 消费者
    consumer:
      enable-auto-commit: false
      auto-commit-interval: 100ms
      key-deserializer: org.apache.kafka.common.serialization.StringDeserializer
      value-deserializer: org.apache.kafka.common.serialization.StringDeserializer
      properties:
        session.timeout.ms: 15000
      group-id: group
server:
  port: 8081
```

# 4 代码实例
## 4.1 发送消息
建立文件  KafkaDemoController.java 设置为 RestController
```java
package com.fishpro.kafka.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class KafkaDemoController {
    @Autowired
    private KafkaTemplate<String,Object> kafkaTemplate;

    @GetMapping("/message/send")
    public boolean send(@RequestParam String message){
        kafkaTemplate.send("testTopic",message);
        return  true;
    }
}


```

## 4.2 接收消息
新建文件 CustomerListener 设置标签为 @Component
```java
package com.fishpro.kafka.listener;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class CustomerListener {

    @KafkaListener(topics="testTopic")
    public void onMessage(String message){
        System.out.println(message);
    }
}

```
## 4.3 测试发送与接收
输入发送消息 url http://localhost:8081/message/send?message=abc
此时 CustomerListener 也会实时接收到消息。

## 4.4 问题
- 出现了：springboot整合kafka出现No group.id found in consumer config
- **原因是 未配置消费端**

```yml
    # 消费者
    consumer:
      enable-auto-commit: false
      auto-commit-interval: 100ms
      key-deserializer: org.apache.kafka.common.serialization.StringDeserializer
      value-deserializer: org.apache.kafka.common.serialization.StringDeserializer
      properties:
        session.timeout.ms: 15000
      group-id: group
```
## 4.5 异步同步消息
建立异步消息同步消息发送

```java

package com.fishpro.kafka.service;

import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.util.concurrent.ListenableFutureCallback;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

@Service
public class KafkaSendService {

    @Autowired
    private KafkaTemplate<String,Object> kafkaTemplate;

    /**
     * 异步示例
     * */
    public void sendAnsyc(final String topic,final String message){
        ListenableFuture<SendResult<String, Object>> future = kafkaTemplate.send(topic,message);
        future.addCallback(new ListenableFutureCallback<SendResult<String, Object>>() {
            @Override
            public void onSuccess(SendResult<String, Object> result) {
                System.out.println("发送消息成功：" + result);
            }

            @Override
            public void onFailure(Throwable ex) {
                System.out.println("发送消息失败："+ ex.getMessage());
            }
        });
    }

    /**
     * 同步示例
     * */
    public void sendSync(final String topic,final String message){
        ProducerRecord<String, Object> producerRecord = new ProducerRecord<>(topic, message);
        try {
            kafkaTemplate.send(producerRecord).get(10, TimeUnit.SECONDS);
            System.out.println("发送成功");
        }
        catch (ExecutionException e) {
            System.out.println("发送消息失败："+ e.getMessage());
        }
        catch (TimeoutException | InterruptedException e) {
            System.out.println("发送消息失败："+ e.getMessage());
        }
    }
}


```
修改 KafkaDemoController 增加异步同步消息测试

```java
/**
 * kafka 消息发送
 * */
@RestController
public class KafkaDemoController {
    @Autowired
    private KafkaTemplate<String,Object> kafkaTemplate;
    @Autowired
    private KafkaSendService kafkaSendService;

    @GetMapping("/message/send")
    public boolean send(@RequestParam String message){
        kafkaTemplate.send("testTopic",message);
        return  true;
    }

    //同步
    @GetMapping("/message/sendSync")
    public boolean sendSync(@RequestParam String message){
        kafkaSendService.sendSync("synctopic",message);
        return  true;
    }

    //异步示例
    @GetMapping("/message/sendAnsyc")
    public boolean sendAnsys(@RequestParam String message){
        kafkaSendService.sendAnsyc("ansyctopic",message);
        return  true;
    }

    //事务消息发送
//    @GetMapping("/message/sendTransaction")
//    public  boolean sendTransaction(){
//        kafkaTemplate.executeInTransaction(kafkaTemplate -> {
//            kafkaTemplate.send("transactionTopic", "TransactionMessage");
//            return true;
//        });
//        return true;
//    }
}

```
