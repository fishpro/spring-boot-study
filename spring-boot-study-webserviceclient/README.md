上一张我们讲到 [Spring Boot 开发 WebService 服务]()，本章研究基于 CXF 调用 WebService。另外本来想写一篇 xfire 作为 client 端来调用 webservice 的代码。后来发现 xfire 在2007 你那巅峰时刻，已经不再更新，而后来的 Spring 已经抛弃了部分 Api。导致现在已经不兼容了。



# 1 新建 Spring Boot Maven 示例工程项目
注意：是用来 IDEA 开发工具
1. File > New > Project，如下图选择 `Spring Initializr` 然后点击 【Next】下一步
2. 填写 `GroupId`（包名）、`Artifact`（项目名） 即可。点击 下一步
    groupId=com.fishpro   
    artifactId=webserviceclient
3. 选择依赖 `Spring Web Starter` 前面打钩。
4. 项目名设置为 `spring-boot-study-webserviceclient`.
   
# 2 引入依赖 Pom

```xml
<dependencies>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-web</artifactId>
        </dependency>
        <!-- https://mvnrepository.com/artifact/org.apache.cxf/cxf-spring-boot-starter-jaxws -->
        <dependency>
            <groupId>org.apache.cxf</groupId>
            <artifactId>cxf-spring-boot-starter-jaxws</artifactId>
            <version>3.2.5</version>
        </dependency> 
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-test</artifactId>
            <scope>test</scope>
        </dependency>
    </dependencies>
```

# 3 编写调用代码

1. 实例化 JaxWsDynamicClientFactory
2. 建立Client请求
3. invoke方法入口，并传递参数

http://localhost:8080/ws/user?wsdl 为上一章我们编写的示例。
```java

@SpringBootApplication
public class WebserviceclientApplication {

    public static void main(String[] args) {
        SpringApplication.run(WebserviceclientApplication.class, args);

        JaxWsDynamicClientFactory dcflient=JaxWsDynamicClientFactory.newInstance();

        Client client=dcflient.createClient("http://localhost:8080/ws/user?wsdl");
        try{
            Object[] objects=client.invoke("getUserById","1");
            System.out.println("getUserById 调用结果："+objects[0].toString());

            Object[] objectall=client.invoke("getUsers");
            System.out.println("getUsers调用部分结果："+objectall[0].toString());

        }catch (Exception e){
            e.printStackTrace();
        }
    }

}

```

右键 WebserviceclientApplication 点击 Run WebserviceclientApplication
```cmd
2019-08-12 18:42:42.341  INFO 63593 --- [           main] o.s.b.w.embedded.tomcat.TomcatWebServer  : Tomcat started on port(s): 8087 (http) with context path ''
2019-08-12 18:42:42.344  INFO 63593 --- [           main] c.f.w.WebserviceclientApplication        : Started WebserviceclientApplication in 2.527 seconds (JVM running for 3.132)
getUserById 调用结果：com.youdomain.webservice.UserDto@3c7d8a4
getUsers调用部分结果：[com.youdomain.webservice.UserDto@4e2824b1, com.youdomain.webservice.UserDto@534d0e20, com.youdomain.webservice.UserDto@7d18338b, com.youdomain.webservice.UserDto@3f4a605f]
```