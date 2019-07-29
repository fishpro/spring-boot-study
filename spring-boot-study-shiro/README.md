Apache Shiro 已经大名鼎鼎，搞 Java 的没有不知道的，这类似于 .Net 中的身份验证 form 认证。跟 .net core 中的认证授权策略基本是一样的。当然都不知道也没有关系，因为所有的权限都是模拟的人或机构的社会行为。

本系列从简单的权限讲起，主要涉及到 Shiro、Spring Security、Jwt、OAuth2.0及其他自定义权限策略。

本章主要讲解 Shiro 的基本原理与如何使用，本章主要用到以下基础设施：

- jdk1.8+
- spring boot 2.1.6
- idea 2018.1


[本项目源码下载](https://github.com/fishpro/spring-boot-study/tree/master/spring-boot-study-shiro)

# 1 Spring Boot 快速集成 Shiro 示例

首先我们来一段真实的代码演示下 Spring Boot 如何集成 Shiro 。本代码示例暂时没有使用到数据库相关知识，本代码主要使用到：
1. shiro
2. thymeeaf

本示例演示了网站用户 admin 密码 123456 的用户使用用户名密码登录网站，经过 Shiro 认证后，获取了授权权限列表，演示了权限的使用。


[本项目源码下载](https://github.com/fishpro/spring-boot-study/tree/master/spring-boot-study-shiro)

## 1.1 新建 Spring Boot Maven 示例工程项目

1. File > New > Project，如下图选择 `Spring Initializr` 然后点击 【Next】下一步
2. 填写 `GroupId`（包名）、`Artifact`（项目名） 即可。点击 下一步
    groupId=com.fishpro   
    artifactId=shiro
3. 选择依赖 `Spring Web Starter` 前面打钩。
4. 项目名设置为 `spring-boot-study-shiro`.

## 1.2 依赖引入 Pom.xml
本代码主要使用到：
1. shiro
2. thymeeaf

在Pom.xml中加入以下代码
```xml
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
    <modelVersion>4.0.0</modelVersion>
    <parent>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-parent</artifactId>
        <version>2.1.6.RELEASE</version>
        <relativePath/> <!-- lookup parent from repository -->
    </parent>
    <groupId>com.fishpro</groupId>
    <artifactId>shiro</artifactId>
    <version>0.0.1-SNAPSHOT</version>
    <name>shiro</name>
    <description>Demo project for Spring Boot</description>

    <properties>
        <java.version>1.8</java.version>
    </properties>

    <dependencies>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-thymeleaf</artifactId>
        </dependency>
        <!--shiro 1.4.0 thymeleaf-extras-shiro 2.0.0 组合-->
        <dependency>
            <groupId>org.apache.shiro</groupId>
            <artifactId>shiro-core</artifactId>
            <version>1.4.0</version>
        </dependency>
        <dependency>
            <groupId>org.apache.shiro</groupId>
            <artifactId>shiro-spring</artifactId>
            <version>1.4.0</version>
        </dependency>
        <!--shiro for thymeleaf 生效需要加入 spring boot 2.x 请使用 2.0.0 版本 否则使用 1.2.1版本-->
        <dependency>
            <groupId>com.github.theborakompanioni</groupId>
            <artifactId>thymeleaf-extras-shiro</artifactId>
            <version>2.0.0</version>
        </dependency>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-web</artifactId>
        </dependency>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-test</artifactId>
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

</project>

```
## 1.3 配置 application.yml
本示例使用默认配置，暂不需要在 application.yml 中配置 shiro。我们把 application.properties 改成了 application.yml (习惯问题) ，修改了默认端口
```yml
server:
  port: 8086
``` 

## 1.4 自定义 Realm 领域 UserRealm 实现自定义认证与授权
在 src/main/java/com/java/fishpro/shiro/config（config是新增的包名） 新增 UserRealm.java 文件
UserRealm 是一种安全数据源，用户登录认证核心在此类中实现，用户授权也在此类中实现，具体看代码注释
1. 重写了 `doGetAuthenticationInfo` 实现对用户名密码的认证，返回一个 `SimpleAuthenticationInfo` 对象。***注意，因为 shiro 是一个安全框架，具体的身份证明的认证就要交给我们自己去实现，实际上认证是业务逻辑，最好我们自己实现。**
2. 重写了 `doGetAuthorizationInfo` 实现对当前用户的授权，返回一个 `SimpleAuthorizationInfo` 对象，**注意，授权就是从业务系统数据库中查询到当前用户的已知权限列表，写在当前会话中，以便在使用的时候去做匹配，匹配成功表示授权成功，匹配失败表示没授权**
```java
//定义一个实体对象用于存储用户信息
public class UserDO {
    private Integer id;
    private String userName;//就是 shiro 中的身份，系统中唯一的存在
    private String password; //就是 shiro 中的证明

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
 
}
```
在包 config（没有就新建 config） 下建立 UserRealm 
```java
package com.fishpro.shiro.config;

import com.fishpro.shiro.domain.UserDO;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.PrincipalCollection;

import java.security.Principal;
import java.util.*;

/**
 * 用户领域 重写了 AuthorizingRealm ，AuthorizingRealm（授权） 其实是继承了 AuthenticatingRealm（认证）
 * 所在在这里只要继承 AuthorizingRealm（授权），主要实现 授权和认证的方法重写
 * 1.doGetAuthenticationInfo 重写认证
 * 2.doGetAuthorizationInfo 重写授权
 * */
public class UserRealm extends AuthorizingRealm {
    /**
     * doGetAuthenticationInfo 重写认证
     * @param authenticationToken token
     * @return 返回认证信息实体（好看身份和证明） AuthenticationInfo
     * */
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {
        String username=(String)authenticationToken.getPrincipal();//身份 例如 用户名
        Map<String ,Object> map=new HashMap<>(16);
        map.put("username",username);
        String password  =new String((char[]) authenticationToken.getCredentials());//证明 例如 密码
        //对身份+证明的数据认证 这里模拟了一个数据源
        //如果是数据库 那么这里应该调用数据库判断用户名密码是否正确
        if(!"admin".equals(username) || !"123456".equals(password)){
            throw new IncorrectCredentialsException("账号或密码不正确");
        }
        //认证通过
        UserDO user=new UserDO();
        user.setId(1);//假设用户ID=1
        user.setUserName(username);
        user.setPassword(password);
        //建立一个 SimpleAuthenticationInfo 认证模块，包括了身份】证明等信息
        SimpleAuthenticationInfo info = new SimpleAuthenticationInfo(user, password, getName());
        return info;
    }

    /**
     * 重写授权 doGetAuthorizationInfo 返回  授权信息对象 AuthorizationInfo
     * @param  principalCollection 身份信息
     * @return  返回  授权信息对象 AuthorizationInfo
     * */
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
        UserDO userDO  = (UserDO)principalCollection.getPrimaryPrincipal();
        Integer userId= userDO.getId();//转成 user 对象
        //授权 新建一个授权模块 SimpleAuthorizationInfo 把 权限赋值给当前的用户
        SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();

        //设置当前会话拥有的角色 实际场景根据业务来如从数据库获取角色列表
        Set<String> roles=new HashSet<>();
        roles.add("admin");
        roles.add("finance");
        info.setRoles(roles);

        //设置当前会话可以拥有的权限 实际场景根据业务来如从数据库获取角色列表下的权限列表
        Set<String> permissions=new HashSet<>();
        permissions.add("system:article:article");
        permissions.add("system:article:add");
        permissions.add("system:article:edit");
        permissions.add("system:article:remove");
        permissions.add("system:article:batchRemove");
        info.setStringPermissions(permissions);
        return  info;
    }

}

```


## 1.6 shiro 实现登录认证
这里主要是显示 login.html 与 LoginController 


![shiro 登录验证逻辑](https://www.cnblogs.com/images/cnblogs_com/fishpro/1453719/o_shiro1.png)

## 1.6.1 登录 html 页面
新增文件 resources/templates/login.html 表示登录页面，这里使用 jquery 来实现逻辑
```html
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>使用 shiro 登录页面</title>
</head>
<body>
<div>
    <input id="userName" name="userName" value="">
</div>
<div>
    <input id="password" name="password" value="">
</div>
<div>
    <input type="button" id="btnSave"  value="登录">
</div>
<script src="https://cdn.bootcss.com/jquery/1.11.3/jquery.js"></script>
<script>
    $(function() {
        $("#btnSave").click(function () {
            var username=$("#userName").val();
            var password=$("#password").val();
            $.ajax({
                cache: true,
                type: "POST",
                url: "/login",
                data: "userName=" + username + "&password=" + password,
                dataType: "json",
                async: false,
                error: function (request) {
                    console.log("Connection error");
                },
                success: function (data) {
                    if (data.status == 0) {
                        window.location = "/index";
                        return false;

                    } else {
                        alert(data.message);
                    }

                }
            });
        });
    });
</script>
</body>
</html>
```


## 1.6.2 登录逻辑
在 UserController中新增两个方法， 路由都是 /login，一个是get 一个是post，因为登录页面是不需要认证，所有两个路由都是 /login 的页面不需要进行认证就可以访问。
```java
//get /login 方法，对应前端 login.html 页面
    @GetMapping("/login")
    public String login(){
        return "login";
    }

    //post /login 方法，对应登录提交接口
    @PostMapping("/login")
    @ResponseBody
    public Object loginsubmit(@RequestParam String userName,@RequestParam String password){
        Map<String,Object> map=new HashMap<>();
        //把身份 useName 和 证明 password 封装成对象 UsernamePasswordToken
        UsernamePasswordToken token=new UsernamePasswordToken(userName,password);
        //获取当前的 subject
        Subject subject = SecurityUtils.getSubject();
        try{
            subject.login(token);
            map.put("status",0);
            map.put("message","登录成功");
            return map;
        }catch (AuthenticationException e){
            map.put("status",1);
            map.put("message","用户名或密码错误");
            return map;
        }
    }
```



## 1.7 shiro 实现Controller层方法授权
这里需要增加几个页面来实现这个功能
### 1.7.1 resources/templates/index.html 登陆成功后跳转的页面
```html
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>通过登录验证后跳转到此页面</title>
</head>
<body>
    通过登录验证后跳转到此页面
<div>
    <a href="/article">前往文章页面</a>
</div>
    <div>
        <a href="/setting">前往设置页面</a>
    </div>
</body>
</html>
```
### 1.7.2 resources/templates/article.html 已授权访问的页面
   ```html
   <!DOCTYPE html>
    <html lang="en">
    <head>
        <meta charset="UTF-8">
        <title>必须获取 app:article 授权</title>
    </head>
    <body>
    必须获取 app:article 授权 才会显示
    </body>
    </html>
   ```
### 1.7.3  resources/templates/setting.html 未授权访问的页面
   ```html
    <!DOCTYPE html>
    <html lang="en">
    <head>
        <meta charset="UTF-8">
        <title>必须获取 app:setting 授权 </title>
    </head>
    <body>
    必须获取 app:setting 授权 才会显示
    </body>
    </html>
   ```
### 1.7.4 resources/templates/error/403.html 未授权统一吹页面
   ```html
   <!DOCTYPE html>
    <html lang="en">
    <head>
        <meta charset="UTF-8">
        <title>403 没有授权</title>
    </head>
    <body>
    你访问的页面没有授权
    </body>
    </html>
   ```
### 1.7.5 controller/UserController.java Controller层方法

如下源代码
1. 方法 article 需要权限 app:article:article 才能进入
2. 方法 setting 需要权限 app:setting:setting 才能进入

```java

@Controller
public class UserController {
    //shiro 认证成功后默认跳转页面
    @GetMapping("/index")
    public String index(){
        return "index";
    }

    @GetMapping("/403")
    public String err403(){
        return "403";
    }
    /**
     * 根据权限授权使用注解 @RequiresPermissions
     * */
    @GetMapping("/article")
    @RequiresPermissions("app:article:article")
    public String article(){
        return "article";
    }

    /**
     * 根据权限授权使用注解 @RequiresPermissions
     * */
    @GetMapping("/setting")
    @RequiresPermissions("app:setting:setting")
    public String setting(){
        return "setting";
    }
    @GetMapping("/login")
    public String login(){
        return "login";
    }

    @PostMapping("/login")
    @ResponseBody
    public Object loginsubmit(@RequestParam String userName,@RequestParam String password){
        Map<String,Object> map=new HashMap<>();
        //把身份 useName 和 证明 password 封装成对象 UsernamePasswordToken
        UsernamePasswordToken token=new UsernamePasswordToken(userName,password);
        //获取当前的 subject
        Subject subject = SecurityUtils.getSubject();
        try{
            subject.login(token);
            map.put("status",0);
            map.put("message","登录成功");
            return map;
        }catch (AuthenticationException e){
            map.put("status",1);
            map.put("message","用户名或密码错误");
            return map;
        }

    }
}
```

## 1.8 shiro 实现前端页面中授权
我们使用了 Thymeleaf 作为前端的模板引擎，您也可以使用 JSP，FreeMarker 等引擎。Shiro 已经能够很好的在 Thymeleaf 中使用，如下代码我在首页中使用

**如下代码，因为没有 app:setting:setting 权限所有【前往设置页面】不会显示**

```html
<hr/>
    <div>
        <title style="color:red;">注意下面是包括权限的,第二个链接因为没有授权是不可见的</title>
    </div>
    <div shiro:hasPermission="app:article:article">
        <a href="/article">前往文章页面</a>
    </div>
    <div shiro:hasPermission="app:setting:setting">
        <a  href="/setting">前往设置页面</a>
    </div>
```

## 1.9 shiro 在程序代码块中使用授权判断
### 1.9.1 通过角色判断
```java
 Subject subject = SecurityUtils.getSubject();
        String str="";
        if(subject.hasRole("admin")){
            str=str+"您拥有 admin 权限";
        }else{
            str=str+"您没有 admin 权限";
        }
        if(subject.hasRole("sale")){
            str=str+"您拥有 sale 权限";
        }
        else{
            str=str+"您没有 sale 权限";
        }
```

### 1.9.2 通过权限判断
注意这里是直接抛出异常，会被全局异常捕捉
```java
        Subject subject = SecurityUtils.getSubject();
        try{
            subject.checkPermission("app:setting:setting");
            str=str+"您拥有 app:setting:setting 权限";

        }catch (UnauthenticatedException ex){
            str=str+"您没有 app:setting:setting 权限";
        }

```



**为什么我的注解没生效？** 

要使用 shiro 注解来授权 Controller 的方法，那么需要在 ShiroConfig 中加入以下代码
```java
/**
     *  开启shiro aop注解支持 如@RequiresRoles,@RequiresPermissions
     *  使用代理方式;所以需要开启代码支持;
     * @param securityManager
     * @return
     */
    @Bean
    public AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor(SecurityManager securityManager){
        AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor = new AuthorizationAttributeSourceAdvisor();
        authorizationAttributeSourceAdvisor.setSecurityManager(securityManager);
        return authorizationAttributeSourceAdvisor;
    }
```

## 1.10 登出/注销

调用 subject 的logout 方法进行注销

```java
@GetMapping("/logout")
    String logout(HttpSession session, SessionStatus sessionStatus, Model model) {
        //会员中心退出登录 当使用这两属性session属性退出
        session.removeAttribute("userData");
        sessionStatus.setComplete();
        SecurityUtils.getSubject().logout();
        return "redirect:/login";

    }
```
## 1.11 问题
1. `@RequiresPermissions` 注解无效
注解无效，没有走到注解，基本就是AOP拦截问题，需要在 ShiroConfig 配置中增加配置
```
```
2. spring boot shiro  Not authorized to invoke method 当 `@RequiresPermissions` 中的权限没有的时候发生
`@RequiresPermissions` 既然生效了，那为什么又会报错呢，按道理已经登录，但是没有权限的方法体，应该跳转到/403 页面才对。
这里应该也是没有拦截到方法这个错。这个在 [Spring Boot 全局异常处理](https://www.cnblogs.com/fishpro/p/11179688.html#_label2_3) 中讲过，需要使用到异常捕捉机制，捕捉到这个异常 `org.apache.shiro.authz.UnauthorizedException` ，然后做统一处理。

```java
@ControllerAdvice(annotations = Controller.class)
public class MyExceptionController {
    private static final Logger logger= LoggerFactory.getLogger(MyExceptionController.class);
    public static final String DEFAULT_ERROR_VIEW = "error";

    @ExceptionHandler(value = UnauthorizedException.class)//处理访问方法时权限不足问题
    public String defaultErrorHandler(HttpServletRequest req, Exception e)  {
        return "error/403";
    }
}
```

3. shiro:hasPermission 标签在 thymeleaf 中不生效问题
   
shiro:hasPermission 标签应用在 thymeleaf ，由于涉及到两个框架，如果原生不支持，那么比如要引入第三方控件。
```java
/**
     * ShiroDialect，为了在thymeleaf里使用shiro的标签的bean
     * @return
     */
    @Bean
    public ShiroDialect shiroDialect() {
        return new ShiroDialect();
    }
```

4. 有的时候回报错 org.springframework.beans.factory.BeanCreationException: Error creating bean with name 'shiroDialect'
   
这个应该是第三方插件 com.github.theborakompanioni 与 spring boot 版本兼容性问题。我改为了以下版本 shiro for thymeleaf 生效需要加入 spring boot 2.x 请使用 2.0.0 版本
```xml
 <!--shiro 1.4.0 thymeleaf-extras-shiro 2.0.0 组合-->
        <dependency>
            <groupId>org.apache.shiro</groupId>
            <artifactId>shiro-core</artifactId>
            <version>1.4.0</version>
        </dependency>
        <dependency>
            <groupId>org.apache.shiro</groupId>
            <artifactId>shiro-spring</artifactId>
            <version>1.4.0</version>
        </dependency>
        <!--shiro for thymeleaf 生效需要加入 spring boot 2.x 请使用 2.0.0 版本 否则使用 1.2.1版本-->
        <dependency>
            <groupId>com.github.theborakompanioni</groupId>
            <artifactId>thymeleaf-extras-shiro</artifactId>
            <version>2.0.0</version>
        </dependency>
```


[本项目源码下载](https://github.com/fishpro/spring-boot-study/tree/master/spring-boot-study-shiro)