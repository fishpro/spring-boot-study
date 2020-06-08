最近上来看了下，还有兄弟在看这个教程，不胜荣幸，我最近上 github 的实际不多，有问题请加微信号 fishpro 或 QQ 502086
# 为什么要写 Spring Boot 系列文章

好记性不如烂笔头，学习 Spring Boot 一定要自己多练练，并能够把他记录下来，当有需要的时候，可以查看自己编写功能教程，能够很快获取相关代码，使用到项目中。

本系列文章是针对 Spring Boot 各个知识点实战训练的笔记，所有代码都经过测试获取正确的结果。并且不断的添加到本系列中。

本教程适用 Spring Boot 2.x 版本，当有代码对 Spring Boot 版本有特别要求的时候的时候，示例说明中有特别说明。

**喜欢的给个 star 呗**

# Spring Boot 知识图谱-入门教程目录

本系列文章一般都是一个知识点一个独立的项目源码，综合应用可能包含多个知识点，比如动态数据源切换技术本身包括 Aop、注解、数据库访问、连接池。

![Spring Boot 2.0](https://img.shields.io/badge/Spring%20Boot-2.0-brightgreen.svg)
![Mysql 5.6](https://img.shields.io/badge/Mysql-5.6-blue.svg)
![JDK 1.8](https://img.shields.io/badge/JDK-1.8-brightgreen.svg)
![Maven](https://img.shields.io/badge/Maven-3.5.0-yellowgreen.svg)
![license](https://img.shields.io/badge/license-MPL--2.0-blue.svg)

## 了解 Spring Boot
- [Spring Boot 入门前的准备-Java JDK for window mac 安装](https://www.cnblogs.com/fishpro/p/spring-knowledge-graph-1-window-mac-install-jdk.html)
- [Spring Boot 入门前的准备-IntelliJ IDEA 开发工具的安装与使用](https://www.cnblogs.com/fishpro/p/spring-knowledge-graph-1-java-ide.html)
- [Spring Boot 概述](https://www.cnblogs.com/fishpro/p/11135358.html)
- [Spring Boot 特性及Spring Boot 2.0新特性](https://www.cnblogs.com/fishpro/p/11135362.html)
- [Spring Boot 学习前你应该知道的 Maven 知识](https://www.cnblogs.com/fishpro/p/11140900.html)
- [Spring Boot 学习之 IDEA 环境下多模块 Multi Modules](https://www.cnblogs.com/fishpro/p/11165827.html)
- [Spring Boot 学习之 IDEA 环境下的 github 创建提交与修改](https://www.cnblogs.com/fishpro/p/11167353.html)
- [Spring Boot 快速入门 HelloWorld示例](https://www.cnblogs.com/fishpro/p/spring-boot-study-helloworld.html)
- [Spring Boot 快速入门 HelloWorld示例详解](https://www.cnblogs.com/fishpro/p/10675293.html)
- [Spring Boot 学习方法论-如何正确的入门 Sprint Boot](https://www.cnblogs.com/fishpro/p/11144008.html)

## Spring Boot 环境及配置

- [Spring Boot 多环境配置](https://www.cnblogs.com/fishpro/p/11154872.html) 
- [Spring Boot 自定义 Banner](https://www.cnblogs.com/fishpro/p/spring-boot-study-banner.html)
- Spring Boot 生产环境部署方式
- [Spring Boot 配置文件和命令行配置](https://www.cnblogs.com/fishpro/p/spring-boot-study-cfg.html)
- [Spring Boot 利用 nginx 实现生产环境的伪热更新](https://www.cnblogs.com/fishpro/p/spring-boot-study-hotstart.html)

## Spring Boot Web 开发

- [Spring Boot @Controller @RestController 使用教程](https://www.cnblogs.com/fishpro/p/spring-boot-study-restcontroller.html)
- [SpringBoot RESTful API 架构风格实践](https://www.cnblogs.com/fishpro/p/spring-boot-study-restful.html)
- [Spring Boot RestApi 测试教程 Mock 的使用](https://www.cnblogs.com/fishpro/p/spring-boot-study-resttest-mock.html)
- [Spring Boot 集成 Swagger2 教程](https://www.cnblogs.com/fishpro/p/spring-boot-study-swagger2.html)

## Spring Boot 基础

- [Spring Boot Log 日志管理](https://www.cnblogs.com/fishpro/p/11167469.html)
- [Spring Boot 全局异常处理](https://www.cnblogs.com/fishpro/p/11179688.html)
- [Spring Boot 使用 Aop 实现日志全局拦截](https://www.cnblogs.com/fishpro/p/11183086.html)
- Spring Boot 中的日期处理
- [Spring Boot Thymeleaf 模板引擎的使用](https://www.cnblogs.com/fishpro/p/11175391.html)
- [Spring Boot FreeMarker 使用教程](https://www.cnblogs.com/fishpro/p/spring-boot-study-freemarker.html)
- SpringBoot使用模板 Velocity（Spring Boot 已经不支持 Velocity了

## Spring Boot 持久层技术

- [Spring Boot 数据库应用 JDBC 使用教程](https://www.cnblogs.com/fishpro/p/spring-boot-study-jdbc.html)
- [Spring Boot 数据库应用 Mybatis使用教程](https://www.cnblogs.com/fishpro/p/spring-boot-study-mybatis.html)
- [Spring Boot 数据库应用 使用Jpa使用教程](https://www.cnblogs.com/fishpro/p/spring-boot-study-jpa.html)
- [Spring Boot 数据库应用 动态数据源](https://www.cnblogs.com/fishpro/p/spring-boot-study-dynamicdb.html)


- [Spring Boot Kafka 入门示例](https://www.cnblogs.com/fishpro/p/12784514.html)

## Spring Boot 缓存技术

- [Spring Boot 中的缓存应用（上）使用ehcache](https://www.cnblogs.com/fishpro/p/spring-boot-study-ehcache.html)
- [Spring Boot 中的缓存应用（中）使用Redis](https://www.cnblogs.com/fishpro/p/spring-boot-study-redis.html)
- [Spring Boot 中的缓存应用（下）使用Memcached](https://www.cnblogs.com/fishpro/p/spring-boot-study-memcached.html)


## Spring Boot 安全

- [Spring Boot 权限应用 使用Shiro](https://www.cnblogs.com/fishpro/p/spring-boot-study-study.html)
- [Spring Boot 权限应用 使用Spring Security](https://www.cnblogs.com/fishpro/p/spring-boot-study-securing.html)
- [Spring Boot 权限应用 使用Jwt](https://www.cnblogs.com/fishpro/p/spring-boot-study-jwt.html)
- [Spring Boot Shiro 整合验证码]

## Spring Boot 常用操作

- [Spring Boot 文件上传](https://www.cnblogs.com/fishpro/p/spring-boot-study-upload.html)
- [Spring Boot 操作 Excel](https://www.cnblogs.com/fishpro/p/spring-boot-study-excel.html)
- [Spring Boot使用Quartz定时任务](https://www.cnblogs.com/fishpro/p/spring-boot-study-quartz.html)
- [Spring Boot 邮件发送](https://www.cnblogs.com/fishpro/p/spring-boot-study-sendemail.html)
- [Spring Boot FTP上传与下载](https://www.cnblogs.com/fishpro/p/spring-boot-study-ftpclient.html)
- [Spring Boot 中使用 HttpClient 进行 POST GET PUT DELETE](https://www.cnblogs.com/fishpro/p/spring-boot-study-httpclient.html)
- [Spring Boot 开放 WebService 服务](https://www.cnblogs.com/fishpro/p/spring-boot-study-webservice.html)
- [Spring Boot 使用 CXF 调用 WebService 服务](https://www.cnblogs.com/fishpro/p/spring-boot-study-cxfclient.html)
- [Spring Boot 使用 JAX-WS 调用 WebService 服务](https://www.cnblogs.com/fishpro/p/spring-boot-study-webservicejaxws.html)
- [Spring Boot Json应用 JackJson FastJson](https://www.cnblogs.com/fishpro/p/spring-boot-study-jackjson.html)
- [Spring Boot 使用 Dom4j XStream 操作 Xml](https://www.cnblogs.com/fishpro/p/spring-boot-study-xml.html)

## Spring Boot 生产环境实战

- [Spring Boot 利用 nginx 实现生产环境的伪热更新](https://www.cnblogs.com/fishpro/p/spring-boot-study-hotstart.html)
- [Spring Boot 多站点利用 Redis 实现 Session 共享](https://www.cnblogs.com/fishpro/p/spring-boot-study-sharedsession.html)

