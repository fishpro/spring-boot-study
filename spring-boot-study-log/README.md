我们编写任何 Spring Boot 程序，可能绕不开的就是 log 日志框架（组件）。
在大多数程序员眼中日志是用来定位问题的。这很重要。

**目录**

* TOC
{:toc}


[本项目源码下载](https://github.com/fishpro/spring-boot-study/tree/master/spring-boot-study-log)  注意本项目提供的源码已在后期重新编写，有部分日期描述不一致。

如果你只是想知道 Spring boot log 如何使用，请直接观看  **3.2 使用 Spring Boot Logback**

# 1 Log 日志概述
## 1.1 Log 日志组件能干什么
日志能干的事情很多，对于学习程序，测试的工程师来说，日志能够定位问题，解决问题，是最大的功能点。

1. ***记录一切*** 日志帮助我们记录程序功能都干了什么，无论是正常的输入输出还是出现异常，都可以用日志记录
2. ***定位问题*** 日志可以帮助程序员调试问题，帮助测试人员定位问题
3. ***记录分析用户行为*** 统计分析师用来记录用户的一起行为，用于分析用户的习惯和商业价值
4. ***备份和还原实时数据*** 数据库工程师用来作为一种特殊的数据库

![日志组件能干什么](https://www.cnblogs.com/images/cnblogs_com/fishpro/1453719/o_log1.png)

## 1.2 日志的级别 Log Level
日志级别是对日志记录信息的轻重缓急的划分。通常从轻到重划分为：
1. TRACE
2. DEBUG
3. INFO
4. WARN
5. ERROR

![日志的级别](https://www.cnblogs.com/images/cnblogs_com/fishpro/1453719/o_log3.png)

通常当我们指定日志级别为 `INFO` 级别，那么 `TRACE` `DEBUG` 级别的日志就不会被输出打印，同理如果指定日志级别为 `ERROR`，那么其他类型的日志将不会被打印。

## 1.3 日志的输出 Log Import
通常日志以文本流的形式存储在磁盘，也可以把日志存储在关系型数据库中或 No Sql 中
1. 文本
2. 关系型数据库
3. No Sql 
4. Console 控制台

一般日志组件都可以自定义输出格式。

## 1.4 Spring Boot 日志组件 Log Plugin
当然自己开发日志组件也是可以的，实际上也不是很难，在一些特殊场景，很多公司都是用自己开发的日志组件，但是对于大多数应用来说，我们使用标准的日志组件就可以解决我们的问题。

Spring Boot 日志组件最为常见的包括了
1. `Logback` Spring Boot 约定的默认配置
2. `log4j`
3. `log4j2` 
4. `slf4j` 
5. `JUL`

大部分场景我们都是推荐 Spring Boot 自带的日志 `logback`。

很多无良媒体直接抓取文章又不写明来源，我是 fishpro程序鱼，本文作者。

# 2 Spring Boot Logback 

## 3.1 关于 Logback 
在 Spring Boot 中，logback 是基于 slf4j 实现的。

slf4j的全称是Simple Loging Facade For Java，即它仅仅是一个为Java程序提供日志输出的统一接口，并不是一个具体的日志实现方案，他能够实现大部分 日志组件。

![slf4j接口](https://www.cnblogs.com/images/cnblogs_com/fishpro/1453719/o_log_slf4j.png)
 
如上图所示，`logback` 中使用了 `slf4j`,`logback-classic.jar`,`logback-core.jar` 实现了 logback 日志功能。


## 3.2 生成一个用于测试的 Spring Boot 项目
使用IDEA创建项目，其实也是从 (https://start.spring.io/ ) 创建，只是更为方便，我们一般采用从IDEA创建Spring Boot项目。

注意mac和windows的IDEA创建过程是一样的。

1）File>New>Project，如下图选择Spring Initializr 然后点击 【Next】下一步

2）填写GroupId（包名）、Artifact（项目名） 即可。点击 下一步
- groupId=com.fishpro 
- artifactId=log
  
3)选择依赖 Spring Web Starter 前面打钩

4)项目名设置为 spring-boot-study-log

5)**编写个简单的示例，可以看出使用日志组件非常的简单**

```java
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class LogApplication {

    public static void main(String[] args) {
        Logger logger =LoggerFactory.getLogger(LogApplication.class);
        SpringApplication.run(LogApplication.class, args);
        logger.debug("This is a debug message");//注意 spring 默认日志输出级别为 info 所以默认情况下 这句不会打印到控制台
        logger.info("This is an info message");
        logger.warn("This is a warn message");
        logger.error("This is an error message");
    }

}
```
**运行可以看到输出了 hello world** 
```bash
2019-07-10 23:51:49.225  INFO 3906 --- [           main] com.fishpro.log.LogApplication           : Started LogApplication in 1.688 seconds (JVM running for 2.317)
2019-07-10 23:51:49.226  INFO 3906 --- [           main] com.fishpro.log.LogApplication           : This is an info message
2019-07-10 23:51:49.227  WARN 3906 --- [           main] com.fishpro.log.LogApplication           : This is a warn message
2019-07-10 23:51:49.227 ERROR 3906 --- [           main] com.fishpro.log.LogApplication           : This is an error message
```


## 3.3 依赖配置 Pom.xml
看到网上不少人贴出了 logback 的依赖配置，实际上从依赖包中可以看出，默认是不需要单独配置 logback 依赖的。**不需要单独配置、不需要单独配置、不需要单独配置**。

## 3.4 使用 YML 配置 logback

### 3.4.1 编写一个 Controller 层类作为测试用 IndexController
```java

@Controller
public class IndexController {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    @RequestMapping("/")
    String index(){
        logger.debug("This is a debug message");
        logger.info("This is an info message");
        logger.warn("This is a warn message");
        logger.error("This is an error message");
        return "index";
    }
}
```
在浏览器中执行 http://locahost:8081/
![输出日志](https://www.cnblogs.com/images/cnblogs_com/fishpro/1453719/o_logindex1.png)

如上图，我们没有 `DEBUG` 的日志，也就是说 `logger.debug("This is a debug message");` 没有被输出到控制台。

**实际上，Spring Boot 是一种约定大于配置，也就是说，他本身有一个配置文件叫 `base.xml` ，在这个配置文件里面，已经默认配置了输出的日志级别** 让我们来看看 `base.xml` ，如下面的 xml 代码所示，`root level="INFO"` 已经定义了日志输出级别为 `INFO` 。这就是为什么，我看到控制台没有输出 `DEBUG` 那条日志。
```xml
<?xml version="1.0" encoding="UTF-8"?>

<!--
Base logback configuration provided for compatibility with Spring Boot 1.1
-->

<included>
  <include resource="org/springframework/boot/logging/logback/defaults.xml" />
  <property name="LOG_FILE" value="${LOG_FILE:-${LOG_PATH:-${LOG_TEMP:-${java.io.tmpdir:-/tmp}}}/spring.log}"/>
  <include resource="org/springframework/boot/logging/logback/console-appender.xml" />
  <include resource="org/springframework/boot/logging/logback/file-appender.xml" />
  <root level="INFO">
    <appender-ref ref="CONSOLE" />
    <appender-ref ref="FILE" />
  </root>
</included>
```


### 3.4.2 在 application.yml 中配置
如下配置，设置level的级别为 `DEBUG` 
```bash
logging:
  level:
    com.fishpro.log: debug
```
在浏览器中执行 http://locahost:8081/ 输出如下日志 可以看出已经有了 `DEBUG` 日志
```bash
2019-07-08 12:51:30.423  INFO 36966 --- [nio-8081-exec-1] o.a.c.c.C.[Tomcat].[localhost].[/]       : Initializing Spring DispatcherServlet 'dispatcherServlet'
2019-07-08 12:51:30.423  INFO 36966 --- [nio-8081-exec-1] o.s.web.servlet.DispatcherServlet        : Initializing Servlet 'dispatcherServlet'
2019-07-08 12:51:30.428  INFO 36966 --- [nio-8081-exec-1] o.s.web.servlet.DispatcherServlet        : Completed initialization in 5 ms
2019-07-08 12:51:30.447 DEBUG 36966 --- [nio-8081-exec-1] c.f.s.controller.IndexController         : This is a debug message
2019-07-08 12:51:30.447  INFO 36966 --- [nio-8081-exec-1] c.f.s.controller.IndexController         : This is an info message
2019-07-08 12:51:30.448  WARN 36966 --- [nio-8081-exec-1] c.f.s.controller.IndexController         : This is a warn message
2019-07-08 12:51:30.448 ERROR 36966 --- [nio-8081-exec-1] c.f.s.controller.IndexController         : This is an error message
```

### 3.4.3 在 application.yml 详细配置
如下配置代码，指定了日志的输出级别、日志存储路径和输出的格式。
```bash
logging:
  #level 日志等级 指定命名空间的日志输出
  level:
    com.fishpro.log: debug
  #file 指定输出文件的存储路径
  file: logs/app.log
  #pattern 指定输出场景的日志输出格式
  pattern:
    console: "%d %-5level %logger : %msg%n"
    file: "%d %-5level [%thread] %logger : %msg%n"
```
在浏览器中执行 http://locahost:8081/

```bash
00:08:15 [reactor-http-nio-2] DEBUG com.fishpro.log.LogApplication - This is a debug message
00:08:15 [reactor-http-nio-2] INFO  com.fishpro.log.LogApplication - This is an info message
00:08:15 [reactor-http-nio-2] WARN  com.fishpro.log.LogApplication - This is a warn message
00:08:15 [reactor-http-nio-2] ERROR com.fishpro.log.LogApplication - This is an error message
```

同时我们可以看到在项目根路径下多处理 `logs` 文件夹，下面有 `app.log` 文件，`app.log` 文件同时记录了日志的输出。


### 3.4.4 不同环境中配置
我们可以在 `dev`、`prod`、`test` 等环境中配置不同的 log 配置项目，我们只需要根据我们的配置文件来设置即可。

关于如何在 Spring Boot 中配置不同的环境实现 开发环境（dev）、测试环境（test）、生成环境（prod）分离，请参考[Spring Boot 中多环境配置](https://www.cnblogs.com/fishpro/p/11154872.html)



## 3.5 使用 logback-spring.xml 来配置 logback 日志
如果需要更为详细的自定义 logback 日志，那么我们需要使用 xml 配置文件来配置。

使用 logback-spring.xml 来自定义日志的配置，主要需要了解 logback-spring.xml 详细功能点，能够配置哪些内容。

### 3.5.1 指定 logback-spring.xml 路径
首先我们要知道 logback-spring.xml 文件放在哪里。

默认 `logback-spring.xml` 路径在 resource 文件夹下，即你只要在 resource 文件夹下新建文件 `logback-spring.xml` 即可。

### 3.5.2 使用 logback-spring.xml 自定义配置
这里我们使用自定义配置 实现
1. 把日志输出到指定的目录，并按照日期 yyyy-MM-dd 的格式按天存储，注意如果没有特别指定，这里的目录 applog 就是指项目的根目录（如果您的项目是多模块配置并不是指模块的目录）
2. 配置 xml
 ```xml
<?xml version="1.0" encoding="UTF-8"?>
<configuration scan="true" scanPeriod="60 seconds" debug="false">
    <contextName>logback</contextName>
    <!--输出到控制台-->
    <appender name="console" class="ch.qos.logback.core.ConsoleAppender">
        <encoder>
            <!--格式化输出：%d表示日期，%thread表示线程名，%-5level：级别从左显示5个字符宽度%msg：日志消息，%n是换行符-->
            <pattern>%d{HH:mm:ss} [%thread] %-5level %logger{36} - %msg%n</pattern>
            <charset>UTF-8</charset>
        </encoder>
    </appender>

    <!--按天生成日志-->
    <appender name="logFile" class="ch.qos.logback.core.rolling.RollingFileAppender">
        <Prudent>true</Prudent>
        <!-- 过滤器，只打印ERROR级别的日志 -->
        <rollingPolicy class="ch.qos.logback.core.rolling.TimeBasedRollingPolicy">
            <!--日志文件输出的文件名-->
            <FileNamePattern>
                applog/%d{yyyy-MM-dd}/%d{yyyy-MM-dd}.log
            </FileNamePattern>
            <!--日志文件保留天数-->
            <MaxHistory>15</MaxHistory>
        </rollingPolicy>
        <layout class="ch.qos.logback.classic.PatternLayout">
            <Pattern>
                %d{yyyy-MM-dd HH:mm:ss} -%msg%n
            </Pattern>
        </layout>
    </appender>

    <logger name="com.fishpro.log" additivity="false">
        <appender-ref ref="console"/>
        <appender-ref ref="logFile"/>
    </logger>
    
    <!-- 设置Spring&Hibernate日志输出级别 -->
    <logger name="org.springframework" level="WARN"/>
    <logger name="org.mybatis" level="WARN"/> 
    <logger name="com.ibatis" level="DEBUG"/> 
    <logger name="com.ibatis.common.jdbc.SimpleDataSource" level="DEBUG"/> 
    <logger name="com.ibatis.common.jdbc.ScriptRunner" level="DEBUG"/> 
    <logger name="com.ibatis.sqlmap.engine.impl.SqlMapClientDelegate" level="DEBUG"/>


       
    <logger name="java.sql.Connection" level="DEBUG"/>  
    <logger name="java.sql.Statement" level="DEBUG"/> 
    <logger name="java.sql.PreparedStatement" level="DEBUG"/> 
    <logger name="com.ruidou.baoqian.mapper" level="DEBUG"/>

    <!-- 开发环境下的日志配置 -->
    <root level="error">
        <appender-ref ref="console"/>
        <appender-ref ref="logFile"/>
    </root>
</configuration>

 ```

在浏览器中执行 http://locahost:8081/

此时，你会发现工程根目录多出来 applog 文件夹，`applog/2019-07-08/2019-07-08.log` 按照日期分类来记录文件，这很重要。
#### 优先级

**有趣的是，当我们把 xml 文件同 application.yml 都进行配置的时候，他执行了 logback-spring。xml 中的配置，所有 logback-spring.xml 配置是优先的**
 
 

# 4 多日志组件如何统一框架
有的时候一个系统应用 Application 中使用了多个日志框架来处理，比如同时使用了 Logback、log4j如何统一呢？


总结：本章介绍了日志组件 Spring Boot Logback 的简单使用，下一个章节，我们将详细说明 logback-spring.xml 的标签含义及标签的属性含义。

---
扩展篇
- [Spring Boot Aop 如何使用]()
- [Spring Boot 多日志组件如何统一框架]()
- [Spring Boot 项目中使用 Aop 进行日志的全局处理]()

