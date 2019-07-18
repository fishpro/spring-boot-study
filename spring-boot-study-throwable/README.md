说到异常处理，我们都知道使用 `try-catch` 可以捕捉异常，可以 `throws` 抛出异常。那么在 Spring Boot 中我们如何处理异常，如何是的处理更加优雅，如何全局处理异常。是本章讨论解决的问题。


**目录**

* TOC
{:toc}



首先让我们简单了解或重新学习下 Java 的异常机制。

[本项目源码下载](https://github.com/fishpro/spring-boot-study/tree/master/spring-boot-study-throwable)

# 1 Java 异常机制概述
Spring Boot 的所有异常处理都基于 java 的。
## 1.1 Java 异常类图


![Java 异常类图](https://www.cnblogs.com/images/cnblogs_com/fishpro/1453719/o_throwable1.png)

1. Java 内部的异常类 `Throwable` 包括了 `Exception` 和 `Error` 两大类，所有的异常类都是 `Object` 对象。
2. `Error` 是不可捕捉的异常，通俗的理解就是由于 java 内部 jvm 引起的不可预见的异常，比如 java 虚拟机运行错误，当内存资源错误，将会出现 `OutOfMemoryError`。此时 java 虚拟机会选择终止线程。
3. `Excetpion` 异常是程序本身引起的，它又分为运行时异常 `RuntimeException`，和非运行时（编译时）`IOException` 等异常。
4. 运行时异常 `RuntimeException` 例如：除数为零，将引发 `ArrayIndexOutOfBoundException` 异常。
5. 非运行异常都是可查可捕捉的。Java 编译器会告诉程序他错了，错在哪里，正确的建议什么。我们可以通过 throws 配合 `try-catch` 来处理。

## 1.2 Exception 运行时异常和编译异常
1. 运行时异常 即 `RuntimeException` 类型下的异常
2. 编译异常 即 `Exception` 类型下除了 `RuntimeException` 类型的异常，例如 `IOException`

## 1.3 可查异常与不可查异常
1. 可查异常 即 `Exception` 类型下除了 `RuntimeException` 类型的异常，都是可查的，具有可查性。
2. 不可查异常 错误类 `Error`  和 `RuntimeException` 类型的异常都是不可查的，具有不可查性。

# 2 Java 异常处理机制
## 2.1 异常处理机制的分类
在 Java 应用程序中，异常处理机制为：**抛出异常**，**捕捉异常**。

- **抛出异常**：当一个方法出现错误引发异常时，方法创建异常对象并交付运行时系统，异常对象中包含了异常类型和异常出现时的程序状态等异常信息。运行时系统负责寻找处置异常的代码并执行。

- **捕获异常**：在方法抛出异常之后，运行时系统将转为寻找合适的异常处理器（exception handler）。潜在的异常处理器是异常发生时依次存留在调用栈中的方法的集合。当异常处理器所能处理的异常类型与方法抛出的异常类型相符时，即为合适 的异常处理器。运行时系统从发生异常的方法开始，依次回查调用栈中的方法，直至找到含有合适异常处理器的方法并执行。当运行时系统遍历调用栈而未找到合适 的异常处理器，则运行时系统终止。同时，意味着Java程序的终止。

针对不同的异常类型，Java 对处理的要求不一样
1. Error 错误，由于不可捕捉，不可查询，Java 允许不做任何处理。
2. 对于运行时异常 RuntimeException 不可查询异常，Java 允许程序忽略运行时异常，Java 系统会自动记录并处理。
3. 对于所有可查异常都可捕捉，Java 自己。

## 2.2 捕获异常 try、catch、finally
**注意 finally 不论程序如何执行都会执行到**
```java
try{
    //可能出现异常的业务代码
}catch(Exception1 e1){
    //异常处理1
}catch(Exception2 e2){
    //异常处理2
}catch(Exceptionn en){
    //异常处理n...
}
finally{
    //无论是否是否异常都会执行的地方
}
```
### 2.2.1 try、catch 流程规则
`try`、`catch` 语句，try只有一个，`catch` 可以有多个，也就是当有多个异常的时候，不需要编写多个 `try-catch` 模块，只要写一个 `try` 多个 `catch` 就可以。
```java
try{
    //可能出现异常的业务代码
}catch(Exception1 e1){
    //异常处理1
}catch(Exception2 e2){
    //异常处理2
}catch(Exceptionn en){
    //异常处理n...
}
```

### 2.2.2 try、catch 、finally
`try`、`catch`、`finally` 语句中，`finally` 并不是必须的，但在有的场景确是非常实用的。
```java
try{
    //可能出现异常的业务代码
}catch(Exception1 e1){
    //异常处理1
}catch(Exception2 e2){
    //异常处理2
}catch(Exceptionn en){
    //异常处理n...
}
finally{
    //无论是否是否异常都会执行的地方
}
```
### 2.2.3 try、catch、finally 执行顺序
执行顺序通常分两种，有异常发生执行程序、无异常执行程序。

例如当我们有示例
```java
try{
    语句1；
    语句2
    语句n；
}catch(Exception1 e1){
    异常处理;
}
finally{
    finally语句；
}
正常语句;
```

![try-catch-finally的执行顺序](https://www.cnblogs.com/images/cnblogs_com/fishpro/1453719/o_throwable2.png)

1. 有异常发生，假设语句1发生了异常，那么程序执行顺序 语句1、异常狐狸、finally语句、正常语句。
2. 如果没有异常发生，那么程序执行顺序 语句1、语句2、语句n、finally语句、正常语句。

# 3 Spring Boot 中的异常处理示例
在 Spring Boot 应用程序中，通常统一处理异常的方法有 
使用注解处理 `@ControllerAdvice`
本示例主要目的处理我们日常 Spring Boot 中的异常处理
1. **在 Web 项目中通过 `@ControllerAdvice` `@RestControllerAdvice` 实现全局异常处理**
   `@ControllerAdvice` 和 `@RestControllerAdvice` 的区别 相当于 `Controller` 和 `RestController` 的区别。
2. **在 Web 项目中实现 404、500 等状态的页面单独渲染**
3. **在 Spring Boot 项目中使用 Aop 切面编程实现全局异常处理**

## 3.1 创建时示例 Spring Boot 项目

1）File > New > Project，如下图选择 `Spring Initializr` 然后点击 【Next】下一步

2）填写 `GroupId`（包名）、`Artifact`（项目名） 即可。点击 下一步
groupId=com.fishpro   
artifactId=thymeleaf
  
3)选择依赖 `Spring Web Starter` 前面打钩。

4)项目名设置为 `spring-boot-study-throwable`

至此项目已经建好了，访问 http://localhost:8084/ （注意，配置文件已经修改为了 server.port=8084）,会直接抛出异常如下图：

![默认处理](https://www.cnblogs.com/images/cnblogs_com/fishpro/1453719/o_throwable3.png)

如图所示，表明 Spring Boot 具有默认的 出错处理机制，指向了 `/error` 目录。

## 3.2 引入依赖编辑Pom.xml
本章节用到 web 和 thymeleaf 两个依赖，**注意只有引入了 `thymeleaf` 后在 templates 目录下增加 `error.html` 系统才能自动与 `/error` 路由匹配** 否则会出现 `Whitelabel Error Page` 页面
```xml
<dependencies>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-web</artifactId>
        </dependency>

        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-thymeleaf</artifactId>
        </dependency>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-test</artifactId>
            <scope>test</scope>
        </dependency>
    </dependencies>
```

## 3.3 创建基于 @RestControllerAdvice 全局异常类示例
`@RestControllerAdvice` 注解是 Spring Boot 用于捕获 `@Controller` 和  `@RestController` 层系统抛出的异常（注意，如果已经编写了 `try-catch` 且在 catch 模块中没有使用 throw 抛出异常， 则 `@RestControllerAdvice` 捕获不到异常）。


`@ExceptionHandler` 注解用于指定方法处理的 Exception 的类型
 

![MyRestExceptionController](https://www.cnblogs.com/images/cnblogs_com/fishpro/1453719/o_throwable7.png)

如上图所示，控制层 `IndexRestController` 编写了4个 api 方法，
- /index 是正常的方法；
- /err 是人为抛出异常；
- /matcherr 除数为0的异常 ；
- /nocatch 用了 try-catch 但没有抛出异常，不会被捕捉。
- 四个 api 其中 /err、/matcherr 会被 `MyRestExceptionController` 捕捉。

**本示例需要新增的文件为 2 个，分别为：**

- controller 下的 `IndexRestController.java`
- exception 下的 `MyRestExceptionHandler.java` 

 
[本项目源码下载](https://github.com/fishpro/spring-boot-study/tree/master/spring-boot-study-throwable)

具体代码清单如下：

### 3.3.1 创建用于测试的 RestController 接口类 IndexRestController
```java

@RestController
@RequestMapping("/api")
public class IndexRestController {
    @RequestMapping("/index")
    public Map index(){
        Map<String,Object> map=new HashMap<>();
        map.put("status","0");
        map.put("msg","正常的输出");
        return map;
    }

    /**
     * 这里人为手动抛出一个异常
     * */
    @RequestMapping("/err")
    public Map err(){

        throw new RuntimeException("抛出一个异常");
    }

    /**
     * 这里抛出的是 RuntimeException 不可查异常，虽然没有使用 try-catch 来捕捉 但系统以及帮助我们抛出了一次
     * */
    @RequestMapping("/matcherr")
    public Map matcherr(){

        Map<String,Object> map=new HashMap<>();
        map.put("status","0");
        map.put("msg","正常的输出");
        int j=0;
        Integer i=9/j;
        return map;
    }

    /**
     * 这里抛出的是 RuntimeException 不可查异 注意这里使用了 try-catch 来捕捉异常，但没有抛出异常
     * */
    @RequestMapping("/nocatch")
    public Map nocatch(){

        Map<String,Object> map=new HashMap<>();
        map.put("status","0");
        map.put("msg","正常的输出 注意这里使用了 try-catch 来捕捉异常，但没有抛出异常，所以没有异常，因为这里抛出的是 RuntimeException 不可查异常，系统也不会报错。");
        int j=0;
        try{
            Integer i=9/j;
        }catch (Exception ex){

        }
        return map;
    }
}
```
### 3.3.2 创建自定义的全局异常处理类 MyRestExceptionController
```java

/**
 * 基于@ControllerAdvice注解的全局异常统一处理只能针对于Controller层的异常
 * 为了和Controller 区分 ，我们可以指定 annotations = RestController.class，那么在Controller中抛出的异常 这里就不会被捕捉
 * */
@RestControllerAdvice(annotations = RestController.class)
public class MyRestExceptionController {
    private static final Logger logger= LoggerFactory.getLogger(MyRestExceptionController.class);

    /**
     * 处理所有的Controller层面的异常
     * */
    @ExceptionHandler(Exception.class)
    @ResponseBody
    public final Map handleAllExceptions(Exception ex, WebRequest request){
        logger.error(ex.getMessage());
        Map<String,Object> map=new HashMap<>();
        map.put("status",-1);
        map.put("msg",ex.getLocalizedMessage());

        return map;
    }
}
```
 
### 3.3.3 使用 Postman进行测试

http://localhost:8084/api/index

```json
{
    "msg": "正常的输出",
    "status": "0"
}
```

http://localhost:8084/api/err

```json
{
    "msg": "抛出一个异常",
    "status": -1
}
```

http://localhost:8084/api/matcherr

```json
{
    "msg": "/ by zero",
    "status": -1
}
```
http://localhost:8084/api/nocatch

```json
{
    "msg": "正常的输出 注意这里使用了 try-catch 来捕捉异常，但没有抛出异常",
    "status": "0"
}
```

http://localhost:8084/api/noname 如果路由不存在，那么系统就会走404路由程序，如何统一格式，则不再此详细阐述。

```json
{
    "timestamp": "2019-07-12T14:53:26.513+0000",
    "status": 404,
    "error": "Not Found",
    "message": "No message available",
    "path": "/api/noname"
}
```


## 3.4 创建基于 @ControllerAdvice 全局异常类示例
 @ControllerAdvice 和 @RestControllerAdvice  其实就是  @Controller 和 @RestController 的区别。直观上就是会不会返回到前台界面的区别。

其实，无论是 @ControllerAdvice 还是 @RestControllerAdvice 都是可以捕捉 @Controller 和 @RestController 抛出的异常。

不同的是，@Controller 异常，我们往往需要更加友好的界面。下面我们使用了 thymeleaf 模板来重新定义 /error 默认路由。

![MyExceptionController](https://www.cnblogs.com/images/cnblogs_com/fishpro/1453719/o_throwable4.png)

如上图所示，控制层 `IndexController` 编写了2个方法，
- error.html 在 rerouces/templates/ 目录下，必须引入 thymeleaf 组件
- /index 是正常的方法；
- /index/err 是人为抛出异常,会被 `MyExceptionController` 捕捉；
- /index/matcherr 除数为0的异常 会被 `MyExceptionController` 捕捉；

### 3.4.1 创建用于测试的 @Controller 文件IndexController
```java
@Controller
public class IndexController {

    /**
     * 正常的页面 对应 /templates/index.html 页面
     * */
    @RequestMapping("/index")
    public String index(Model model){

        model.addAttribute("msg","这是一个index页面的正常消息");
        return "index";
    }

    /**
     * 抛出一个 RuntimeException 异常
     * */
    @RequestMapping("/index/err")
    public String err(){

        throw new RuntimeException("抛出一个 RuntimeException 异常");
    }

    /**
     * 抛出一个 RuntimeException 异常
     * */
    @RequestMapping("/index/matherr")
    public String matherr(Model model){

        int j=0;
        int i=0;
        i=100/j;

        return "index";
    }
}
```


### 3.4.2 创建用于捕捉 @Controller 异常的全局文件MyExceptionController
```java
public class MyExceptionController {
    private static final Logger logger= LoggerFactory.getLogger(MyRestExceptionController.class);
    public static final String DEFAULT_ERROR_VIEW = "error";
    /**
     * 处理所有的Controller层面的异常
     * 如果这里添加 @ResponseBody 注解 表示抛出的异常以 Rest 的方式返回，这时就系统就不会指向到错误页面 /error
     * */
    @ExceptionHandler(Exception.class)
    public final ModelAndView handleAllExceptions(Exception ex, HttpServletRequest request){
        logger.error(ex.getMessage());
        ModelAndView modelAndView = new ModelAndView();

        //将异常信息设置如modelAndView
        modelAndView.addObject("msg", ex);
        modelAndView.addObject("url", request.getRequestURL());
        modelAndView.setViewName(DEFAULT_ERROR_VIEW);

        //返回ModelAndView
        return modelAndView;
    }
 
}
```

### 3.4.3 创建/error对应的出错页面 error.html  
```html
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>异常统一处理页面</title>
</head>
<body>
this is error.html
<p th:text="${msg}"></p>
</body>
</html>
```

### 3.4.4 创建控制层对应的前端文件 index.html  
```html
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>IndexController-index</title>
</head>
<body>
<p th:text="${msg}"></p>
</body>
</html>
```


### 3.4.5 使用浏览器测试 

http://localhost:8084/index

```
这是一个index页面的正常消息
```

http://localhost:8084/index/err

```
this is error.html
java.lang.RuntimeException: 抛出一个 RuntimeException 异常
```
http://localhost:8084/index/matherr
```
this is error.html
java.lang.ArithmeticException: / by zero
```

# 4 Spring Boot 自定义错误页面
在第3章节，我们知道可以通过 建立全局异常处理类来实现 基于 `@Controller` 的异常统一处理。我们也可以把统一异常展示到自定义错误页面。

在 Spring Boot 中使用了 `ErrorController` 来处理出错请求。在 Java 8 上又提供了 `BasicErrorController` 他继承与 `AbstractErrorController`,`AbstractErrorController` 又继承于 `ErrorController`。

## 4.1 基于 ErrorController 实现自定义错误页面
在本章节中 需要新增3个页面，自定义处理类、404、500、error 等页面。其原理是根据 `HttpServletResponse` 的返回状态 `response.getStatus()` 来判断如果是 404 就跳转到对应 404 路由。

![基于 ErrorController 自定义错误页面](https://www.cnblogs.com/images/cnblogs_com/fishpro/1453719/o_throwable8.png)

- 增加controller 下的 CustomerErrorController 页面
- 增加 templates/error/404.html
- 增加 templates/error/500.html
- 增加 templates/error/error.html
CustomerErrorController 主要代码
```java
public class CustomErrorController implements ErrorController {

    private static final String ERROR_PATH = "/error";

    @RequestMapping(
            value = {ERROR_PATH},
            produces = {"text/html"}
    )

    /**
     * 用户 Controller 带返回的
     * */
    public ModelAndView errorHtml(HttpServletRequest request, HttpServletResponse response) {
        int code = response.getStatus();
        if (404 == code) {
            return new ModelAndView("error/404");
        } else if (403 == code) {
            return new ModelAndView("error/403");
        } else {
            return new ModelAndView("error/500");
        }

    }

    @RequestMapping(value = ERROR_PATH)
    public Map handleError(HttpServletRequest request, HttpServletResponse response) {
        Map<String,Object> map=new HashMap<>();
        int code = response.getStatus();
        if (404 == code) {
            map.put("status",404);
            map.put("msg","未找到资源文件");
        } else if (403 == code) {
            map.put("status",403);
            map.put("msg","没有访问权限");
        } else if (401 == code) {
            map.put("status",401);
            map.put("msg","登录过期");
        } else {
            map.put("status",500);
            map.put("msg","服务器错误");
        }
        return  map;
    }

    @Override
    public String getErrorPath(){return ERROR_PATH;}
}

```

测试效果 浏览器输入任意不存在的网站 http://localhost:8084/23/23  查看输出

```
404页面
```


## 4.2 实现自定义错误页面整合到全局异常处理类中
实际上这里是可以跟上面的全局异常处理合起来的，在我们定义的 `MyExceptionController` 。
我们需要在 `MyExceptionController` 类中增加判定即可。

注意因为 404 异常并不是我们的异常捕捉类可以捕捉的，所以 404 页面不在其中。




**结束语**

这篇文章前前后后，写了两天，找的参考资料很多都是不全，要么没有交代 默认的/error 问题，要么就是没有说明 @Controller @RestController 问题。总之我总结来有几个问题需要解决：

1. 如何解决默认的 /error 路由映射问题，在有 thymeleaf 与没有的情况有什么区别
2. 如何解决 404、505不同状态不同映射问题
3. @Controller @RestController是否都能拦截,有人说只能拦截 @Controller 这是不正确的， @RestController 本来就是 @Controller 演变而来，同样是可以拦截的。
4. @Controller 如何友好的返回
5. @RestController 如何给远程调用方返回错误信息

问题：
1. 没有捕捉的异常

这种情况一般是使用 try-catch 但没有 throw 出异常导致的

---

关联阅读：

[Spring Boot Log 日志使用教程](https://www.cnblogs.com/fishpro/p/11167469.html)

[Spring Boot Thymeleaf 模板引擎的使用](https://www.cnblogs.com/fishpro/p/11175391.html)