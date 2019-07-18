在 Spring Boot 中 Aop 与 Ioc 可以说是 Spring 的灵魂，其功能也是非常强大。

[本项目源码下载](https://github.com/fishpro/spring-boot-study/tree/master/spring-boot-study-aoplog)

# 1 新建 Spring Boot 项目 

1）File > New > Project，如下图选择 `Spring Initializr` 然后点击 【Next】下一步

2）填写 `GroupId`（包名）、`Artifact`（项目名） 即可。点击 下一步
groupId=com.fishpro   
artifactId=aoplog
  
3)选择依赖 `Spring Web Starter` 前面打钩。

4)项目名设置为 `spring-boot-study-aoplog`

# 2 Pom 中引入 aop 依赖
```xml
<dependencies>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-web</artifactId>
        </dependency>
        <!--aop-->
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-aop</artifactId>
        </dependency>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-test</artifactId>
            <scope>test</scope>
        </dependency>
    </dependencies>
```
# 3 编写处理代码
## 3.1 程序原理及流程
在 Spring Boot 应用程序中使用 @Aspect 注解来实现 aop ，非常的简单。本示例代码主要实现

**本代码供新增了4个文件,文件清单如下，斜杠前是包名，需要新建**
- **annotation/Log.java** 自定义日志拦截注解
- **aspect/LogAspect.java** 自定义日志拦截注解触发条件、环绕增强（在请求前和请求后的执行逻辑）
- **aspect/WebLogAspect.java** 基于 Web 的日志拦截，定义了 Web 的请求前动作、请求后动作、请求生命周期中的动作
- **controller/IndexController.java** 控制层 Controller 类，方便测试，定了 `/log`
  
**本示例使用到的新的注解包括**
- @Aspect 面向切面编程注解，通常应用在类上
- @Pointcut Pointcut是植入Advice的触发条件。每个Pointcut的定义包括2部分，一是表达式，二是方法签名。方法签名必须是 public及void型。可以将Pointcut中的方法看作是一个被Advice引用的助记符，因为表达式不直观，因此我们可以通过方法签名的方式为 此表达式命名。因此Pointcut中的方法只需要方法签名，而不需要在方法体内编写实际代码
- @Around：环绕增强，相当于MethodInterceptor
- @AfterReturning：后置增强，相当于AfterReturningAdvice，方法正常退出时执行
- @Before：标识一个前置增强方法，相当于BeforeAdvice的功能，相似功能的还有
- @AfterThrowing：异常抛出增强，相当于ThrowsAdvice
- @After: final增强，不管是抛出异常或者正常退出都会执行

![使用注解和Aop进行全局日志拦截](https://www.cnblogs.com/images/cnblogs_com/fishpro/1453719/o_throwable9.png)

**流程说明**

如图所示，用户访问 `IndexController` 的 `/log` 路由的时候，被 `WebLogAspect` 拦截到了带有 `@log` 注解方法的信息，根据 `WebLogAspect` 中的定义处理了请求前 请求后 请求中的信息 并存于日志中。

## 3.2 自定义日志注解 @log
本文不打算详细说明什么是自定义注解，如何使用，可以知道的是，
- 注解是一种元数据形式。即注解是属于java的一种数据类型，和类、接口、数组、枚举类似
- 注解用来修饰，类、方法、变量、参数、包。
- 注解不会对所修饰的代码产生直接的影响。
  
定义注解 @Log，在 anotation 包下 Log.java
```java
/**
 * 使用@interface将定义一个注解 这里是log
 * 用于日志aop编程
 * */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Log {
    String value() default "";
}
```

如何使用，我没有在 IndexController 的方法中使用 @Log 注解

## 3.3 定义测试用 IndexController
```java
@Controller
public class IndexController {

    @Log("日志注解，配合WebAspect记录请求前、请求后、请求过程")
    @RequestMapping("/log")
    @ResponseBody
    public String log(String name){
        return "log";
    }
}
```


## 3.4 定义 aop 类 LogAspect 与 WebLogAspect
```java
@Aspect
@Component
public class LogAspect {
    private static final Logger logger=LoggerFactory.getLogger(LogAspect.class);

    /**
     * 这里指定使用 @annotation 指定com.fishpro.aoplog.annotation.Log log注解
     * */
    @Pointcut("@annotation(com.fishpro.aoplog.annotation.Log)")
    public void logPointCut(){

    }

    public  Object around(ProceedingJoinPoint point) throws Throwable{
        long beginTime = System.currentTimeMillis();
        // 执行方法
        Object result = point.proceed();
        // 执行时长(毫秒)
        long time = System.currentTimeMillis() - beginTime;
        //异步保存日志 这里是文本日志

        return result;
    }

    void saveLog(ProceedingJoinPoint joinPoint, long time) throws InterruptedException{

    }
}
```

```java
@Aspect
@Component
public class WebLogAspect {
    private static final Logger logger = LoggerFactory.getLogger(WebLogAspect.class);

    /**
     * 指定 controller 包下的注解
     * */
    @Pointcut("execution( * com.fishpro.aoplog.controller.*.*(..))")//两个..代表所有子目录，最后括号里的两个..代表所有参数
    public void logPointCut() {

    }

    /**
     * 指定当前执行方法在logPointCut之前执行
     * */
    @Before("logPointCut()")
    public void doBefore(JoinPoint joinPoint) throws Throwable{
        // 接收到请求，记录请求内容
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = attributes.getRequest();

        // 记录下请求内容
        logger.info("请求地址 : " + request.getRequestURL().toString());
        logger.info("HTTP METHOD : " + request.getMethod());
        // 获取真实的ip地址
        //logger.info("IP : " + IPAddressUtil.getClientIpAddress(request));
        logger.info("CLASS_METHOD : " + joinPoint.getSignature().getDeclaringTypeName() + "."
                + joinPoint.getSignature().getName());
        logger.info("参数 : " + Arrays.toString(joinPoint.getArgs()));
        //loggger.info("参数 : " + joinPoint.getArgs());
    }
    /**
     * 指定在方法之后返回
     * */
    @AfterReturning(returning = "ret", pointcut = "logPointCut()")// returning的值和doAfterReturning的参数名一致
    public void doAfterReturning(Object ret) throws Throwable {
        // 处理完请求，返回内容(返回值太复杂时，打印的是物理存储空间的地址)
        logger.info("返回值 : " + ret);
    }

    @Around("logPointCut()")
    public Object doAround(ProceedingJoinPoint pjp) throws Throwable {
        long startTime = System.currentTimeMillis();
        Object ob = pjp.proceed();// ob 为方法的返回值
        logger.info("耗时 : " + (System.currentTimeMillis() - startTime));
        return ob;
    }
}
```

# 3.5 测试
在浏览器输入,注意端口在 application.yml 中已经改为了8085 

http://localhost:8085/log?name=fishpro
在控制台输出
```cmd
2019-07-13 17:12:15.388  INFO 10055 --- [nio-8085-exec-1] com.fishpro.aoplog.aspect.WebLogAspect   : 请求地址 : http://localhost:8085/log
2019-07-13 17:12:15.388  INFO 10055 --- [nio-8085-exec-1] com.fishpro.aoplog.aspect.WebLogAspect   : HTTP METHOD : GET
2019-07-13 17:12:15.389  INFO 10055 --- [nio-8085-exec-1] com.fishpro.aoplog.aspect.WebLogAspect   : CLASS_METHOD : com.fishpro.aoplog.controller.IndexController.log
2019-07-13 17:12:15.389  INFO 10055 --- [nio-8085-exec-1] com.fishpro.aoplog.aspect.WebLogAspect   : 参数 : [fishpro]
2019-07-13 17:12:17.219  INFO 10055 --- [nio-8085-exec-1] com.fishpro.aoplog.aspect.WebLogAspect   : 耗时 : 4934
2019-07-13 17:12:17.997  INFO 10055 --- [nio-8085-exec-1] com.fishpro.aoplog.aspect.WebLogAspect   : 返回值 : log
```


[本项目源码下载](https://github.com/fishpro/spring-boot-study/tree/master/spring-boot-study-aoplog)

---

关联阅读：

[Spring Boot Log 日志使用教程](https://www.cnblogs.com/fishpro/p/11167469.html)

[Spring Boot 全局异常处理](https://www.cnblogs.com/fishpro/p/11179688.html)


---

欢迎关注我的微信公众号,我们一起编程聊天看世界
 
<div align="center"><img width="192px" height="192px" src="https://images.cnblogs.com/cnblogs_com/fishpro/1453719/o_qrcode_for_gh_8caafe0dbeac_430.jpg"/></div>