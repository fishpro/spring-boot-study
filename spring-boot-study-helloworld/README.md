@[TOC](Spring Boot 快速入门 Helloworld 示例)

**本文重点**
`Spring Boot` 快速入门（`Spring Boot `2.0及以上）

如何使用 `Spring Boot `快速入门 快速创建 `HelloWorld` 项目，主要涉及到

1. 创建(生成）一个Spring Boot标准项目
2. 配置Pom.xml文件
3. 编写示例代码
4. 编写测试代码

5. 运行和调试
6. 打包发布

**源码下载**
 本文源码下载 [Github-spring-boot-study-helloword](https://github.com/fishpro/spring-boot-study/tree/master/spring-boot-study-helloworld)

本文首次在博客园发布，如遇到图片打不开的情况，请移步到[博客园浏览此文章](https://www.cnblogs.com/fishpro/p/spring-boot-study-helloworld.html)。
 

# 1 Spring Boot 是什么 
`Spring Boot ` 是 `Spring` 家族成员之一

> Spring Boot 是由Pivotal团队提供的全新框架，其设计目的是用来简化新Spring应用的初始搭建以及开发过程。
> 该框架使用了特定的方式来进行配置，从而使开发人员不再需要定义样板化的配置。下面摘自spring boot中文文档
> 
> 为所有的Spring开发提供一个从根本上更快的和广泛使用的入门经验 开箱即用，但你可以通过不采用默认设置来摆脱这种方式
> 提供一系列大型项目常用的非功能性特征 绝对不需要代码生成及XML配置

 

# 2 Spring Boot 有什么优点
以下谈到个人认为的几点

1. 入门门槛低，只要懂程序的java、c#、php、js都能快速入门
2. 强大的生态，几乎没有什么功能是需要自己从头开始开发
3. 部署方便，独立服务器也好、云部署也好、docker也好都非常方便 


# 3 Spring Boot快速入门HelloWord
一般 `SpringBoot` 开发的步骤包括

1. 生成一个 `Spring Boot` 项目
2. 配置 `Pom.xml`
3. 编辑代码
4. 编写测试代码
5. 运行
6. 打包发布

　　

## 3.1. 生成一个Spring Boot项目
这里我们介绍 在浏览器中实现一个 `http://localhost:8999/hello/say web` 程序。这里使用 IntelliJ IDEA  作为IDE环境来编译。也可以使用其他IDE。

我们一般采用从 `IDEA` 创建 `Spring Boot` 项目，见1.2.

### 3.1.1 使用start.spring.io创建项目
1. 打开 [`https://start.spring.io/`](https://start.spring.io/)

2. 选择构建工具 `Maven Project`、`Java`、`Spring Boot ` 版本 2.1.4 (注意这里文档版本是2.1.4，但在下面的实践中2.1.4本地的mvn有问题，后面换成了2.0.0) 、填写`Group`、`Articfact` 及一些工程基本信息，可参考下图所示：


![在这里插入图片描述](https://imgconvert.csdnimg.cn/aHR0cHM6Ly9pbWcyMDE4LmNuYmxvZ3MuY29tL2Jsb2cvNjY0MTk3LzIwMTkwNC82NjQxOTctMjAxOTA0MDcyMjUxMzc0MzYtMzgxNjg3Mzc4LnBuZw?x-oss-process=image/format,png)
如上图一些英文的含义

`groupId`：`groupId` 分为几个字段，例如 `com.companyname `，前面的 com 叫【域】，后面的是你自己起的域名。这里是 com.fishpro

`artifactId`：`artifactId` 一般是项目名或者模块名。这里是 `springstudy`

`Dependencies`：依赖,输入 web，选择 web

 

例如公司名称：baidu，项目名 peach 那么 `groupId=com.baidu ` `artifactId=peach ` 最终在java项目的基本包名为 com.baidu.peach。当然groupId也可以是com.baidu.look 这种形式，完全有开发者自己定义。

本示例项目 `groupId=com.fishpro artifactId=springstudy`

 

3.  点击绿色按钮【**Generate Project**】生成项目，浏览器则自动下载项目，我命名的是springhello，那么下载的是springhello.zip

4. 如下图，下载的位 `maven` 项目结构的 `java` 项目，使用 `idea` 开发工具打开这个文件夹，则会自动加载 `maven` 项目。

![在这里插入图片描述](https://imgconvert.csdnimg.cn/aHR0cHM6Ly9pbWcyMDE4LmNuYmxvZ3MuY29tL2Jsb2cvNjY0MTk3LzIwMTkwNC82NjQxOTctMjAxOTA0MDYxMzUxNDE1NTUtNjY3NzcxNjA4LnBuZw?x-oss-process=image/format,png)

用 IDEA 打开（open) 文件后，idea自动加载项目的依赖，请稍等片刻，一般在2到5分钟，这取决于您的网络和系统性能。

也可以使用 功能，导入文件夹为 `Maven` 项目，一路点击【next】即可。


![在这里插入图片描述](https://imgconvert.csdnimg.cn/aHR0cHM6Ly9pbWcyMDE4LmNuYmxvZ3MuY29tL2Jsb2cvNjY0MTk3LzIwMTkwNC82NjQxOTctMjAxOTA0MDYxMzU2NDYxMDktMTM0NTg2NTIyMy5wbmc?x-oss-process=image/format,png)
 

5. IDEA 导入 `Spring Boot ` 的项目结构，截图如下：

![在这里插入图片描述](https://imgconvert.csdnimg.cn/aHR0cHM6Ly9pbWcyMDE4LmNuYmxvZ3MuY29tL2Jsb2cvNjY0MTk3LzIwMTkwNC82NjQxOTctMjAxOTA0MDYxNzU1MzU1NTYtMTYyMTY5NTIyMi5wbmc?x-oss-process=image/format,png)

 

**项目结构简单说明**

`src/main/java` 下是主程序的java代码存放处
`src/resource` 是资源文件包括图片、Css、Javascript等静态文件和配置文件yml或properties文件
`src/test/java` 测试代码 
 

```bash
.idea #IDEA 软件特有的文件夹，隐藏文件 
.mvn #Maven项目特有的文件，隐藏文件
src #存放源码包括了java代码和配置文件和资源文件
     main #java代码
       java
         com
           pringbook
             SpringstudyApplication #启动类java文件
        resource #资源文件包括静态文件
           application.properties #项目的配置文件，比如配置服务器端口等
     test #测试文件夹
     mvnw #maven项目辅助文件，自动生成
     mvnw.cmd #maven项目辅助文件，自动生成
     pom.xml #maven项目配置文件，类似于C#的web.config
     study.iml #iml 是IDEA软件的项目标识文件，一般是自动创建的
  Externel Libraries #lib文件，这个不用管，是自动的
```

　　

### 3.1.2 使用IDEA 创建项目
使用IDEA创建项目，其实也是从 https://start.spring.io/ 创建，只是更为方便，我们一般采用从IDEA创建Spring Boot项目。

注意mac和windows的IDEA创建过程是一样的。

 1. File>New>Project，如下图选择Spring Initializr 然后点击 【Next】下一步

使用IDEA创建Spring Boot 项目1
![在这里插入图片描述](https://imgconvert.csdnimg.cn/aHR0cHM6Ly9pbWcyMDE4LmNuYmxvZ3MuY29tL2Jsb2cvNjY0MTk3LzIwMTkwNC82NjQxOTctMjAxOTA0MDcwODQzNTM2NjEtMTY5NDIxMTk1MS5wbmc?x-oss-process=image/format,png)
 

 2. 填写GroupId（包名）、Artifact（项目名） 即可。

![在这里插入图片描述](https://imgconvert.csdnimg.cn/aHR0cHM6Ly9pbWcyMDE4LmNuYmxvZ3MuY29tL2Jsb2cvNjY0MTk3LzIwMTkwNC82NjQxOTctMjAxOTA0MDcwODQ1NTk1MTQtOTI4OTY3NTI2LnBuZw?x-oss-process=image/format,png)

groupId=com.fishpro

artifactId=springstudy

 

 

3. 选择依赖，我们选择Web


![在这里插入图片描述](https://imgconvert.csdnimg.cn/aHR0cHM6Ly9pbWcyMDE4LmNuYmxvZ3MuY29tL2Jsb2cvNjY0MTk3LzIwMTkwNC82NjQxOTctMjAxOTA0MDcwODQ3MjI2NjctMTY4Nzk5MzkwOC5wbmc?x-oss-process=image/format,png)
 

 

 

# 2.配置Pom.xml
注意如果生成项目的时候没有设置 `Dependencies`，选择web，那么这里要在 `Pom.xml` 中设置，`Pom.xml` 设置依赖也非常的简单，直接把 `<dependency></dependency>` 的节点拷贝到pom依赖节点中即可。

`Pom.xml` 属于 `maven` 项目结构的项目依赖项配置文件，主要管理第三方包的引用。

默认项目配置了`spring-boot-starter` 和 `spring-boot-starter-test` ，配置如下

```yaml
<dependencies>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter</artifactId>
        </dependency>

        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-test</artifactId>
            <scope>test</scope>
        </dependency>
</dependencies>
```

因为示例项目是web项目所以需要引入web模块，在dependency下增加节点，增加后如下：

```yml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-web</artifactId>
</dependency>

<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-test</artifactId>
    <scope>test</scope>
</dependency>
```

　

**注意，如果IDEA没有自动导入，那么前往右下角，点击【Import Changes】**
![在这里插入图片描述](https://imgconvert.csdnimg.cn/aHR0cHM6Ly9pbWcyMDE4LmNuYmxvZ3MuY29tL2Jsb2cvNjY0MTk3LzIwMTkwNC82NjQxOTctMjAxOTA0MDYxODA5MjgwMDctOTU0MDg5OTIwLnBuZw?x-oss-process=image/format,png)


有的时候会出问题了！

1.Failed to read artifact descriptor for org.springframework.boot:spring-boot-starter-web:jar:2.1.4.RELEASE less... (⌘F1) 
Inspects a Maven model for resolution problems.

未能加载spring-boot-starter-web:jar，这个应该是mvn管理器加载问题。去掉里面依赖

 答：spring boot 2.1.4的mvn有问题，改成2.0.0就好了。当然这可能是其他原因，我使用IDEA创建的项目就没有这个问题。


# 3.编写代码
## 3.1 Web项目的常用目录结构
通常，我们使用三层结构来编写。

应用层（`Controller`）、服务层（`Service`）、数据层（`Dao`）

我们也增加对应的包目录。

## 3.2 增加Controller包
1. 在本示例中，右键springstudy包名，新建包名 `controller` (注意一般是消息)

2. 在controller下新建 `HelloWorldController.java`  (注意首字母大写)

3. 在 `HelloWorldController` 中增加 `java` 代码

```java
@RestController
@RequestMapping("/hello")
public class HelloWorldController {
    @RequestMapping("/say")
    public String say(){
        return "Hello World";
    }
}
```
　　
## 3.3 更改Web端口

因我的系统端口默认 `8080` 倍 `nginx` 占领了，我把本次项目的启动端口改为 `8999`

在 `resources\application.properties`  中设置（注意有的网络教程中是 `application.yml`  其实这是另一种配置文件格式，就想json和xml 只是格式不同，功能作用一样）
 
```bash
#设置端口号
server.port=8999
```

　　 

# 4.编写测试代码
测试代码在 `src\test\java` 下面编写

1. 在本示例中，右键 `src\test\java\com\fishpro\springstudy` 包名，新建包名 `controller ` (注意一般是消息)
2. 在 `controller` 下新建 `HelloWorldControllerTests.java`  (注意对应于main下，一般后缀Tests)
3. 在 `HelloWorldControllerTests` 中增加 `java` 代码

```java

package com.fishpro.springstudy.controller;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

@RunWith(SpringRunner.class)
@SpringBootTest
public class HelloWorldControllerTests {
    private MockMvc mockMvc;
    @Before
    public void setUp() throws Exception {
        mockMvc = MockMvcBuilders.standaloneSetup(new HelloWorldController()).build();
    }

    @Test
    public void getHello() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/hello/say").accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(MockMvcResultHandlers.print())
                .andReturn();
    }
}
```

 

　　

# 5.运行
## 5.1 运行程序或调试程序
点击右上角，绿色运行三角形按钮，启动运行，或点击它旁边的爬虫按钮，进行调试。

或者点击菜单

```bash
Run>Run 'SpringstudyApplication'
Run>Debug 'SpringstudyApplication'
```
 ![在这里插入图片描述](https://imgconvert.csdnimg.cn/aHR0cHM6Ly9pbWcyMDE4LmNuYmxvZ3MuY29tL2Jsb2cvNjY0MTk3LzIwMTkwNC82NjQxOTctMjAxOTA0MDcyMzEyMzUzOTAtMTc1NDg3ODgwOS5wbmc?x-oss-process=image/format,png)

浏览器输入 `http://localhost:8999/hello/say` 注意示例代码中的端口号可能不一致。



 

## 5.2 运行测试用例
右键 `HelloWorldControllerTests.java` 选择 Run 'HelloWorldControllerTests' with Coverage。注意示例代码中的端口号可能不一致。

 ![在这里插入图片描述](https://imgconvert.csdnimg.cn/aHR0cHM6Ly9pbWcyMDE4LmNuYmxvZ3MuY29tL2Jsb2cvNjY0MTk3LzIwMTkwNC82NjQxOTctMjAxOTA0MDcwOTU4MTAzNjEtMTQ0ODQ3OTQ2My5wbmc?x-oss-process=image/format,png)

 

# 6.打包发布
通常我们一jar方式打包发布，war方式用于单独的发布到已有的tomcat web服务器中，以后的实践中再讲。

1. 选择 `View`> `Tool Windows`>`Terminal`

2. 输入命令

```bash
mvn clean

mvn install　
```
在根目录下有个target 文件夹，

![在这里插入图片描述](https://imgconvert.csdnimg.cn/aHR0cHM6Ly9pbWcyMDE4LmNuYmxvZ3MuY29tL2Jsb2cvNjY0MTk3LzIwMTkwNC82NjQxOTctMjAxOTA0MDcxMDAzMjYyNDEtMTM0MTIwNzI3Ni5wbmc?x-oss-process=image/format,png)

 3. 模拟服务器环境，运行jar文件，输入命令，后则可以在浏览器中得到结果。

```bash
java -jar springstudy-0.0.1-SNAPSHOT.jar
```

 

总之，Spring Boot是一个优秀的实战型框架，他既简单又强大。

# 7 问题
1. Failed to read artifact descriptor for org.springframework.boot:spring-boot-starter-web:jar:2.1.4.RELEASE less... (⌘F1) 
Inspects a Maven model for resolution problems.

未能加载spring-boot-starter-web:jar，这个应该是mvn管理器加载问题。

2. 端口问题

默认是8080端口，如果端口被占用了（例如mac的nginx默认是8080），需要修改，那么在 resources\application.properties中设置

```bash
#设置端口号
server.port=8999
```

 

　　

**源码下载**
 本文源码下载 [Github-spring-boot-study-helloword](https://github.com/fishpro/spring-boot-study/tree/master/spring-boot-study-helloworld)

 

点个赞呗
 