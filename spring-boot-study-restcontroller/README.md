在 Spring Boot 中，@Controller 注解是专门用于处理 Http 请求处理的，是以 MVC 为核心的设计思想的控制层。@RestController 则是 @Controller 的衍生注解。

[本项目源码下载](https://github.com/fishpro/spring-boot-study/tree/master/spring-boot-study-restcontroller)

# 1 Spring Boot Controller
## 1.1 原理
Spring Boot 本身就 Spring MVC 的简化版本。是在 Spring MVC 的基础上实现了自动配置，简化了开发人员开发过程。

Spring MVC 是通过一个叫 `DispatcherServlet` 前端控制器的来拦截请求的。而在 Spring Boot 中 使用自动配置把 `DispatcherServlet` 前端控制器自动配置到框架中。

**例如，我们来解析 /users 这个请求**

![/users请求流程](https://www.cnblogs.com/images/cnblogs_com/fishpro/1453719/o_restful2.png)


1. `DispatcherServlet` 前端控制器拦截请求 /users
2. `servlet` 决定使用哪个 `handler` 处理
3. Spring 检测哪个控制器匹配 `/users`，Spring 从 @RquestMapping 中查找出需要的信息
4. Spring 找到正确的 Controller 方法后，开始执行 Controller 方法
5. 返回 users 对象列表
6. 根据与客户端交互需要返回 Json 或者 Xml 格式



## 1.2 相关注解
在 Spring Boot 中使用到 @Controller 及相关的注解如下，主要分为三个层面进行，请求前，处理中，返回。
 
|    应用场景   |     注解      |  注解说明 |
|----------|:-------------|:----------|
| 处理请求| @Controller | 处理 Http 请求|
| 处理请求| @RestController   | @Controller 的衍生注解|
| 路由请求| @RequestMapping   | 路由请求 可以设置各种操作方法|
| 路由请求| @GetMapping   | GET 方法的路由|
| 路由请求| @PostMapping   | POST 方法的路由|
| 路由请求| @PutMapping   |  PUT 方法的路由 |
| 路由请求| @DeleteMapping   |  DELETE 方法的路由 | 
| 请求参数| @PathVariable |处理请求 url 路径中的参数 /user/{id}  |
| 请求参数| @RequestParam | 处理问号后面的参数 |
| 请求参数| @RequestBody | 请求参数以json格式提交 |
| 返回参数| @ResponseBody | 返回 json 格式 |

 注意以上注解需要强调的是 @RestController 是 @Controller 的子集。

 @GetMapping、@PostMapping、@PutMapping、@DeleteMapping 是 @RequestMapping 的子集。所以实际上我们只需要掌握 @Controller 和 @RequestMapping 就可以了。


## 1.3 @Controller 与 @RestController 区别
@Controller 包括了 @RestController。@RestController 是 Spring4 后新加的注解，从 RestController 类源码可以看出 @RestController 是 @Controller 和 @ResponseBody 两个注解的结合体。
>@Controller=@RestController+@ResponseBody
如下 @RestController 的源码可以看出他们的关系
```java
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Controller
@ResponseBody
public @interface RestController {
    @AliasFor(
        annotation = Controller.class
    )
    String value() default "";
}

```

## 1.4 @Controller 与 @RestController应用场景
- @Controller 一般应用在有返回界面的应用场景下.

    例如，管理后台使用了 thymeleaf 作为模板开发，需要从后台直接返回 Model 对象到前台，那么这时候就需要使用 @Controller 来注解。

- @RestController 如果只是接口，那么就用 RestController 来注解.

    例如前端页面全部使用了 Html、Jquery来开发，通过 Ajax 请求服务端接口，那么接口就使用 @RestController 统一注解。

## 1.5 @RequestMapping 说明
首先我们来看看 @RequestMapping 的源码,我在上面加了注释
```java
@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Mapping
public @interface RequestMapping {
    String name() default "";

    //指定请求的实际地址
    @AliasFor("path")
    String[] value() default {};
    @AliasFor("value")
    String[] path() default {};
    //指定请求的method类型， GET、POST、PUT、DELETE等
    RequestMethod[] method() default {};
    //指定request中必须包含某些参数值是，才让该方法处理。
    String[] params() default {};
    //指定request中必须包含某些指定的header值，才能让该方法处理请求。
    String[] headers() default {};
    //指定处理请求的提交内容类型（Content-Type），例如application/json, text/html;
    String[] consumes() default {};
    //指定返回的内容类型，仅当request请求头中的(Accept)类型中包含该指定类型才返回；
    String[] produces() default {};
}
```
**示例说明：**

|    示例   |     说明      |
|----------|:-------------|
| @RequestMapping("/index")| 默认为 GET 方法的路由 /index | 
| @RequestMapping(value="/index",method = RequestMethod.GET)| 同上面一条 | 
| @RequestMapping(value="/add",method = RequestMethod.POST)| 路由为 /add 的 POST 请求 | 
| @RequestMapping(value="/add",method = RequestMethod.POST),consumes="application/json"| 路由为 /add 的 POST 请求，但仅仅处理 application/json 的请求 | 
| @RequestMapping(value="/add",method = RequestMethod.POST),produces="application/json"| 路由为 /add 的 POST 请求，强调返回为 JSON 格式 | 
| @RequestMapping(value="/add",method = RequestMethod.POST),params="myParam=xyz"| 路由为 /add 的 POST 请求，但仅仅处理头部包括 myParam=xyz 的请求 | 
| @RequestMapping(value="/add",method = RequestMethod.POST),headers="Referer=http://www.xyz.com/"| 路由为 /add 的 POST 请求，但仅仅处理 来源为 www.xyz.com 的请求 | 

# 2 @Controller 和 @RestController 示例
本章节，将对两个注解配合其他注解编写一系列示例，为了演示 @Controller 返回对应页面功能，我们在示例中引入了 thymeleaf 模板。具体在 pom.xml 中有说明。

|    编号   |     路由      |  Http方法 |  方法说明 |
|----------|:-------------|:----------|:----------|
| 1| /user/index |GET| 获取用户列表并返回列表页面 | 
| 1| /user/add | GET|用户新增页面 | 
| 1| /user/save |POST| 新增用户的api | 
| 1| /user/edit |GET| 用户编辑的页面 | 
| 1| /user/update |POST| 编辑用户的api | 
| 1| /user/del |GET| 删除用户页面 | 
| 1| /user/deleted |POST| 删除用户页面的api | 

## 2.1 新建 Spring Boot 项目
1. File > New > Project，如下图选择 `Spring Initializr` 然后点击 【Next】下一步
2. 填写 `GroupId`（包名）、`Artifact`（项目名） 即可。点击 下一步
    groupId=com.fishpro   
    artifactId=restcontroller
3. 选择依赖 `Spring Web Starter` 前面打钩,在模板列中勾选 `thymeleaf`。
4. 项目名设置为 `spring-boot-study-restcontroller`.

## 2.2 依赖 Pom.xml 配置
本项目引入了 web 和 thymeleaf ，具体一人如下代码：
```xml
<dependencies>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-thymeleaf</artifactId>
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

```
并把配置文件重命名为 application.yml ，修改默认测试端口
```
server:
  port: 8087
```

## 2.3 基于 @Controller 的示例代码
本代码实例中新增了如下页面，注意 `templates` 表示 `/resources/tempalates/`，`controller` 表示  `com.fishpro.restcontroller.controller` 包。
- controller/UserController.java 控制层-用户类
- domain/UserDO.java 用户控实体类
- templates/user/index.html 视图-用户列表页面
- templates/user/add.html 视图-新增用户页面
- templates/user/edit.html 视图-编辑用户页面

### 2.3.1 返回用户列表信息 /user/index

[本项目源码下载](https://github.com/fishpro/spring-boot-study/tree/master/spring-boot-study-restcontroller)

**首先构件一个虚拟的用户数据**
```java
  /**
     * 模拟一组数据
     * */
    private List<UserDO> getData(){
        List<UserDO> list=new ArrayList<>();

        UserDO userDO=new UserDO();
        userDO.setUserId(1);
        userDO.setUserName("admin");
        list.add(userDO);

        userDO=new UserDO();
        userDO.setUserId(2);
        userDO.setUserName("heike");
        list.add(userDO);

        userDO=new UserDO();
        userDO.setUserId(3);
        userDO.setUserName("tom");
        list.add(userDO);

        userDO=new UserDO();
        userDO.setUserId(4);
        userDO.setUserName("mac");
        list.add(userDO);

        return  list;
    }
```

**1 创建一个用户列表页面 /user/index**

UserController 增加方法如下图所示，使用 @RequestMapping 注解，*<font color=red>注意 </font> `@RequestMapping(method = RequestMethod.GET,value = "/index")` 等于 `@RequestMapping(value = "/index")` 也等于 `@RequestMapping("/index")` 也等于 `@GetMapping("/index")`*
```java
 /**
     * GET  返回用户列表信息
     * */
    @RequestMapping(method = RequestMethod.GET,value = "/index")
    public String index(Model model){
        List<UserDO> list =getData();
        model.addAttribute("list",list);//返回 用户 list
        return "user/index";
    }

    
```
**2 创建 templates/index.html 对应上面的路由 /user/index**

```html
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Title</title>
</head>
<body>
<div class="title">

</div>
<div th:each="user :${list}">
    用户ID：<span th:text="${user.userId}"></span>
    用户名：<span th:text="${user.userName}"></span>
</div>
</body>
</html>
```
**3 在浏览器中查看**

右键 RestControllerApplication > Run  在浏览器输入 http://localhost:8087/user/index
```html
用户ID：1 用户名：admin
用户ID：2 用户名：heike
用户ID：3 用户名：tom
用户ID：4 用户名：mac
```


### 2.3.3 新增用户页面 /user/add

[本项目源码下载](https://github.com/fishpro/spring-boot-study/tree/master/spring-boot-study-restcontroller)

**在 UserController 中增加 add 路由 /user/add**

UserController 增加方法如下图所示，使用 @RequestMapping 注解，并创建了两个路由
- /user/add 对应前端页面
- /user/save 对应前端页面提交按钮的接口

```java
 /**
     * GET 返回add页面
     * @GetMapping("/add") = @RequestMapping(method = RequestMethod.GET,value = "/add")
     * */
    @GetMapping("/add")
    public String add(){
        return "user/add";
    }
    /**
     * POST 新增用户api
     * @return 返回 map对象
     * */
    @RequestMapping(method = RequestMethod.POST,value = "/save")
    @ResponseBody
    public Object save(UserDO user){
        List<UserDO> list= getData();
        list.add(user);//模拟向列表中增加数据
        Map<String,Object> map=new HashMap<>();
        if(null==user){

            map.put("status",3);
            map.put("message","没有传任何对象");
            return  map;
        }
        map.put("status",0);
        map.put("data",user);
        return  map;
    }

```

**创建 templates/add.htm 文件对应路由 /user/add**

在对应的 templates/add.html中增加代码
```html
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>用户新增页面</title>
    <script src="https://cdn.bootcss.com/jquery/1.11.3/jquery.js"></script>
</head>
<body>
<form id="form1">
    <div> <input name="userId" id="userId"  placeholder="请输入userid"></div>
    <div> <input name="userName" id="userName"   placeholder="请输入username"></div>
    <div> <input  type="button" value="新增" id="btnSave"/></div>
</form>
<script>
    $(function () {

        $("#btnSave").click(function () {
            $.ajax({
                cache: true,
                type: "POST",
                url: "/user/save",
                data:$('#form1').serialize(),
                dataType:"json",
                async: false,
                error: function (request) {
                    console.log("Connection error");
                },
                success: function (data) {
                    if (data.code == 0) {
                        console.log("成功");
                    } else {
                        console.log("失败");
                    }

                }
            });
        });

    });
</script>
</body>
</html>
```
**在浏览器中测试效果**
右键 RestControllerApplication > Run  在浏览器输入 http://localhost:8087/user/add
 
 
### 2.3.3 编辑用户页面 /user/edit
这部分代码原理同 2.3.2 代码原理 通过构件一个编辑页面，点击编辑页面的【保存】提交到后端的 api 中。

本示例中创建了2个接口 1个文件
- /user/edit 对应前端页面
- /user/update 对应前端页面提交按钮的接口
- /templates/user/edit.html

```java
 /**
     * GET 返回编辑页面
     * @GetMapping("/edit/{id}") = @RequestMapping(method = RequestMethod.GET,value = "/edit/{id}")
     * @PathVariable("id") 表示路由中的动态参数部分
     * @param  id 表示要编辑的用户id
     * @param model 表示将要输出到页面的 Model 对象
     * @return 返回到 user/edit页面
     * */
    @GetMapping("/edit/{id}")
    public String edit(@PathVariable("id") String id, Model model){
        UserDO user =new UserDO();
        user.setUserId(3);
        user.setUserName("fishpro");
        model.addAttribute("user",user);
        return "user/edit";
    }

    /**
     * POST 修改用户api
     * @RequestBody 表示参数使用 json 对象传输
     * @return 返回 map对象
     * */
    @PostMapping("/update")
    @ResponseBody
    public Object update(@RequestBody UserDO user){
        Map<String,Object> map=new HashMap<>();
        if(null==user){

            map.put("status",3);
            map.put("message","没有传任何对象");
            return  map;
        }
        //更新逻辑
        map.put("status",0);
        return  map;
    }
```

edit.html

```html
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>用户编辑页面</title>
    <script src="https://cdn.bootcss.com/jquery/1.11.3/jquery.js"></script>
</head>
<body>
<form id="form1">
    <div> <input name="userId" id="userId"  placeholder="请输入userid" th:value=${user.userId}> </div>
    <div> <input name="userName" id="userName"   placeholder="请输入username" th:value=${user.userName}></div>
    <div> <input  type="button" value="新增" id="btnSave"/></div>
</form>
<script>
    $(function () {

        $("#btnSave").click(function () {
            $.ajax({
                cache: true,
                type: "POST",
                url: "/user/update",
                data:$('#form1').serialize(),
                dataType:"json",
                async: false,
                error: function (request) {
                    console.log("Connection error");
                },
                success: function (data) {
                    if (data.code == 0) {
                        console.log("成功");
                    } else {
                        console.log("失败");
                    }
                }
            });
        });

    });
</script>
</body>
</html>
```

[本项目源码下载](https://github.com/fishpro/spring-boot-study/tree/master/spring-boot-study-restcontroller)


### 2.3.3 删除用户 /user/delete


[本项目源码下载](https://github.com/fishpro/spring-boot-study/tree/master/spring-boot-study-restcontroller)

```java
/**
     * POST 修改用户api 
     * @return 返回 map对象
     * */
    @PostMapping("/delete/{id}")
    @ResponseBody
    public Object delete(@PathVariable("id") Integer id){
        List<UserDO> list= getData();
        UserDO userDO=null;
        for (UserDO user:list
                ) {
            if(id.equals(user.getUserId().toString())){
                //删除用户
                userDO=user;
                break;
            }
        }

        Map<String,Object> map=new HashMap<>();
        map.put("status",0);
        map.put("data",userDO);
        return  map;
    }
```



## 2.4 基于 @RestController 的示例代码

本章节中是基于 2.3版本中的接口，其实是一样的功能，详细见下面的代码，为了完成代码功能示例，我们新建了 UserRestController.java，不同的是 我们给类加了 @RestController 修饰符。
```java
@RestController
@RequestMapping("user2")
public class UserRestController {
    //从 UserController 搬过来代码即可
}
```



[本项目源码下载](https://github.com/fishpro/spring-boot-study/tree/master/spring-boot-study-restcontroller)


---

关联阅读：

[Spring Boot Thymeleaf 模板引擎的使用](https://www.cnblogs.com/fishpro/p/spring-boot-study-thymeleaf2.html)

[Spring Boot RESTful api](https://www.cnblogs.com/fishpro/p/spring-boot-study-restful.html)

[Spring Boot Log 日志使用教程](https://www.cnblogs.com/fishpro/p/spring-boot-study-log.html)

[Spring Boot 全局异常处理](https://www.cnblogs.com/fishpro/p/spring-boot-study-throwable.html)
