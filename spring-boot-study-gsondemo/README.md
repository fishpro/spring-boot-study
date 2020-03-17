使用 android 开发的同学，可能对 `Gson` 更为了解，`Gson` 是谷歌官方推出的支持 `JSON` -- `Java Object` 相互转换的 `Java` 序列化/反序列化 库，之前由于没有用过，所以学习一下。

在 `Spring Boot` 中我们也可以是哟合那个 `Gson` 作为 `json` 处理。本章主要学习 `Gson` 的用法

我们在使用 Gson 的时候必须先移除 Spring 自带的 jackson。

[本项目源代码下载](https://github.com/fishpro/spring-boot-study/tree/master/spring-boot-study-gsondemo)


# 1 新建 Spring Boot Maven 示例工程项目

注意：本示例是用来 IDEA 开发工具（window、mac 通用）
1. `File > New > Project`，如下图选择 `Spring Initializr` 然后点击 【Next】下一步
2. 填写 `GroupId`（包名）、`Artifact`（项目名） 即可。点击 下一步
    groupId=com.fishpro   
    artifactId=gsondemo
3. 选择依赖 `Spring Web Starter` 前面打钩。
4. 项目名设置为 `spring-boot-study-gsondemo`.

# 2 配置 application.properties (或 application.yml)
设置一个启动端口

```bash
server.port = 8089
```


# 2 引入第三方类库 Gson
## 2.1 从 pom.xml 中移除 spring boot 自带的 jackson
找到下面  `spring-boot-study-gsondemo` 下的 `pom.xml`，去掉下面代码
```xml
<dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-web</artifactId>
        </dependency>

```

替换成如下代码，其中 `exclusions` 表示排除。

```xml
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-web</artifactId>
            <exclusions>
                <exclusion>
                    <groupId>com.fasterxml.jackson.core</groupId>
                    <artifactId>jackson-databind</artifactId>
                </exclusion>
            </exclusions>
        </dependency>

```

## 2.2 向 pom.xml 添加第三方库 Gson
```xml
<dependency>
    <groupId>com.google.code.gson</groupId>
    <artifactId>gson</artifactId>
</dependency>
```

## 2.3 设置 Gson 为 controller 的消息转换器
**注意这个步骤很重要，不然在 restcontroller 或 contoller 自动转换 json 的时候不会使用 gson。**

### 2.3.1 使用 @Configuration （推荐）
- 在 gsondemo 下新建 package 名称 config
- 在 config 下新建 GsonConfig 
```java
package com.fishpro.gsondemo.config;

import org.springframework.boot.autoconfigure.http.HttpMessageConverters;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.GsonHttpMessageConverter;

import java.util.ArrayList;
import java.util.Collection;

@Configuration
public class GsonConfig {
    @Bean
    public HttpMessageConverters customConverters() {

        Collection<HttpMessageConverter<?>> messageConverters = new ArrayList<>();

        GsonHttpMessageConverter gsonHttpMessageConverter = new GsonHttpMessageConverter();
        messageConverters.add(gsonHttpMessageConverter);

        return new HttpMessageConverters(true, messageConverters);
    }
}

```
### 2.3.2 使用 WebMvcConfigurerAdapter 来配置
另外我们也可以通过配置 WebMvcConfigurerAdapter 的继承类实现一样的功能 ,例如下面的功能，
```java
Configuration
@EnableWebMvc
public class Application extends WebMvcConfigurerAdapter {

    @Override
    public void configureMessageConverters(List<HttpMessageConverter < ? >> converters) {
        GsonHttpMessageConverter gsonHttpMessageConverter = new GsonHttpMessageConverter();
        converters.add(gsonHttpMessageConverter);
    }
}
```

# 3 编写示例代码
本章节目的是实现
- 利用 Gson 替代 jackson 引擎。
- 已知对象（对象可能是多级的），如何转化为 json，在 controller 或 restcontroller 中如何返回 json
- 已知 json 字符串如何转化为对象

 我们采用和 fastjson 、jackson 示例一样的过程，感兴趣的可以看这篇【[Spring Boot Json 之 Jackjson Fastjson](https://www.cnblogs.com/fishpro/p/spring-boot-study-jackjson.html)】.

## 3.1 编写实体类代码

**新建两个实体类，用户类、用户地址类，他们的关系是父子关系**


User(路径 src/main/java/com/fishpro/gsondemo/dto/User.java)
```java
public class User {
    private Integer userId;

    private String username;

    private List<Address> addresses;


    private Calendar created = new GregorianCalendar();


    public User(Integer userId,String username){
        this.userId=userId;
        this.username=username;
    }
    public User(Integer userId,String username,List<Address> addresses){
        this.userId=userId;
        this.username=username;
        this.addresses=addresses;
    }
    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public List<Address> getAddresses() {
        return addresses;
    }

    public void setAddresses(List<Address> addresses) {
        this.addresses = addresses;
    }

    public Calendar getCreated() {
        return created;
    }

    public void setCreated(Calendar created) {
        this.created = created;
    }
}

```

Address (路径 src/main/java/com/fishpro/gsondemo/dto/Address.java)
```java
public class Address {
    private String street;
    private String zipcode;
    private String mobile;

    public Address(String street,String zipcode,String mobile){
        this.street=street;
        this.zipcode=zipcode;
        this.mobile=mobile;
    }
    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getZipcode() {
        return zipcode;
    }

    public void setZipcode(String zipcode) {
        this.zipcode = zipcode;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }
}
``` 

## 3.2 控制层代码

`UserController` (路径 src/main/java/com/fishpro/gsondemo/controller/UserController.java)
```java
@RequestMapping("/user")
@RestController
public class UserController {

    @GetMapping("/get")
    public User get(){
        List<Address> addressList=new ArrayList<>();
        addressList.add(new Address("江苏省南京市玄武大道1000号","201001","1801989098"));
        addressList.add(new Address("江苏省南京市玄武大道1001号","201001","1811989098"));
        User user = new User(1, "fishpro",addressList);
        return  user;
    }
}
```
## 3.3 查看使用 gson 转化的 json 效果
运行 右键点击 `JsonApplication` > `Run JsonApplication` 在浏览器中输入 http://localhost:8089/user/get 系统直接返回了json格式，那么 Spring Boot 中默认就是哟合那个了 Jackson 来处理。

```json
{
	"userId": 1,
	"username": "fishpro",
	"addresses": [{
		"street": "江苏省南京市玄武大道1000号",
		"zipcode": "201001",
		"mobile": "1801989098"
	}, {
		"street": "江苏省南京市玄武大道1001号",
		"zipcode": "201001",
		"mobile": "1811989098"
	}],
	"created": "2019-08-13T14:40:50.901+0000"
}
```

## 3.4 gson 的常用操作
### 3.4.1 基础类型解析
接上面 3.2 代码继续写
```java
@RequestMapping("/user")
@RestController
public class UserController {

    @GetMapping("/get")
    public User get(){

        List<Address> addressList=new ArrayList<>();
        addressList.add(new Address("江苏省南京市玄武大道1000号","201001","1801989098"));
        addressList.add(new Address("江苏省南京市玄武大道1001号","201001","1811989098"));
        User user = new User(1, "fishpro",addressList);


        //01 基本类型解析
        Gson gson = new Gson();
        int i = gson.fromJson("99", int.class); //100
        double d = gson.fromJson("\"99.99\"", double.class);  //99.99
        boolean b = gson.fromJson("true", boolean.class);     // true
        String str = gson.fromJson("String", String.class);   // String
        System.out.println("int 类型："+i);
        System.out.println("double 类型："+d);
        System.out.println("boolean 类型："+b);
        System.out.println("String 类型："+str); 

        return  user;
    }
}
```
### 3.4.2 实体对象转 json 字符串
接上面 3.4.1 代码继续写
```java
@RequestMapping("/user")
@RestController
public class UserController {

    @GetMapping("/get")
    public User get(){

        List<Address> addressList=new ArrayList<>();
        addressList.add(new Address("江苏省南京市玄武大道1000号","201001","1801989098"));
        addressList.add(new Address("江苏省南京市玄武大道1001号","201001","1811989098"));
        User user = new User(1, "fishpro",addressList);


        //01 基本类型解析
        Gson gson = new Gson();
        int i = gson.fromJson("99", int.class); //100
        double d = gson.fromJson("\"99.99\"", double.class);  //99.99
        boolean b = gson.fromJson("true", boolean.class);     // true
        String str = gson.fromJson("String", String.class);   // String
        System.out.println("int 类型："+i);
        System.out.println("double 类型："+d);
        System.out.println("boolean 类型："+b);
        System.out.println("String 类型："+str);

        //02 实体对象转 json str
        String rst=gson.toJson(user);
        System.out.println("实体对象转:");
        System.out.println(rst);
 

        return  user;
    }
}
```

### 3.4.3 实体对象转 json 字符串
接上面 3.4.2 代码继续写
```java
@RequestMapping("/user")
@RestController
public class UserController {

    @GetMapping("/get")
    public User get(){

        List<Address> addressList=new ArrayList<>();
        addressList.add(new Address("江苏省南京市玄武大道1000号","201001","1801989098"));
        addressList.add(new Address("江苏省南京市玄武大道1001号","201001","1811989098"));
        User user = new User(1, "fishpro",addressList);


        //01 基本类型解析
        Gson gson = new Gson();
        int i = gson.fromJson("99", int.class); //100
        double d = gson.fromJson("\"99.99\"", double.class);  //99.99
        boolean b = gson.fromJson("true", boolean.class);     // true
        String str = gson.fromJson("String", String.class);   // String
        System.out.println("int 类型："+i);
        System.out.println("double 类型："+d);
        System.out.println("boolean 类型："+b);
        System.out.println("String 类型："+str);

        //02 实体对象转 json str
        String rst=gson.toJson(user);
        System.out.println("实体对象转:");
        System.out.println(rst);

        //03 对象转实体 json fromJson
        User user2 = gson.fromJson(rst, User.class);
        System.out.println("对象转实体:");
        System.out.println(user2);

        return  user;
    }
}
```

最后输出效果
```bash
类型：99
double 类型：99.99
boolean 类型：true
String 类型：String
实体对象转:
{"userId":1,"username":"fishpro","addresses":[{"street":"江苏省南京市玄武大道1000号","zipcode":"201001","mobile":"1801989098"},{"street":"江苏省南京市玄武大道1001号","zipcode":"201001","mobile":"1811989098"}],"created":{"year":2020,"month":2,"dayOfMonth":17,"hourOfDay":22,"minute":38,"second":30},"updated":{"year":2020,"month":2,"dayOfMonth":17,"hourOfDay":22,"minute":38,"second":30}}
对象转实体:
com.fishpro.gsondemo.dto.User@ca30ffa

```


[本项目源代码下载](https://github.com/fishpro/spring-boot-study/tree/master/spring-boot-study-gsondemo)
 

本文参考
- https://www.leveluplunch.com/java/tutorials/023-configure-integrate-gson-spring-boot/

---
个人亲爱的读者，如果您有问题可以联系我微信号 fishpro 或 qq号 502086 （qq号上的少）