除了 CXF 我们还可以使用 Spring Boot 自身默认的组件 JAX-WS 来实现 WebService 的调用。



[本项目源码 github 下载 ](https://github.com/fishpro/spring-boot-study/tree/master/spring-boot-study-webservicejaxws)

# 1 新建 Spring Boot Maven 示例工程项目
注意：是用来 IDEA 开发工具
1. File > New > Project，如下图选择 `Spring Initializr` 然后点击 【Next】下一步
2. 填写 `GroupId`（包名）、`Artifact`（项目名） 即可。点击 下一步
    groupId=com.fishpro   
    artifactId=webservicejaxws
3. 选择依赖 `Spring Web Starter` 前面打钩。
4. 项目名设置为 `spring-boot-study-webservicejaxws`.

**注意不需要额外导入第三方组件**

# 2 自动生成 JAX-WS 代码
右键包 user (没有新建一个包名)
![图片](https://www.cnblogs.com/images/cnblogs_com/fishpro/1453719/o_webservice3.jpg)

弹出生成框中输入wsdl的地址

![图片](https://www.cnblogs.com/images/cnblogs_com/fishpro/1453719/o_webservice2.jpg)

在启动类中输入测试代码

```java
@SpringBootApplication
public class WebservicejaxwsApplication {

    public static void main(String[] args) {
        SpringApplication.run(WebservicejaxwsApplication.class, args);
        UserService userService=new UserService();
        UserDto userDto= userService.getUserPortName().getUserById("1");
        System.out.println("userdto "+userDto.getUserName());
    }

}
```

**
右键 WebserviceclientApplication 点击 Run WebserviceclientApplication**

```cmd
2019-08-12 21:40:29.230  INFO 64345 --- [           main] c.f.w.WebservicejaxwsApplication         : Started WebservicejaxwsApplication in 2.087 seconds (JVM running for 2.765)
userdto admin
```