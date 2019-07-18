上篇讲过 [Spring Boot RESTful api](https://www.cnblogs.com/fishpro/p/spring-boot-study-restful.html) ，这篇简单介绍下 SwaggerUI 在 Spring Boot 中的应用.

Swagger 是一个规范和完整的框架，用于生成、描述、调用和可视化 RESTful 风格的 Web 服务的接口文档。

**本文主要目的**
1. 学会 Spring Boot 中集成 Swagger2
2. 了解 Swagger2 的主要注解并会使用
3. 美化 Swagger2 在 Spring Boot 的界面展示


# 1 创建一个 Spring Boot 项目
本项目主要使用 [Spring Boot RESTful api](https://www.cnblogs.com/fishpro/p/spring-boot-study-restful.html) 代码，

为了方便，直接使用了 [Spring Boot RESTful api](https://www.cnblogs.com/fishpro/p/spring-boot-study-restful.html) 的示例代码，为代码增加 SwaggerUI 组件功能。

1. File > New > Project，如下图选择 `Spring Initializr` 然后点击 【Next】下一步
2. 填写 `GroupId`（包名）、`Artifact`（项目名） 即可。点击 下一步
    groupId=com.fishpro   
    artifactId=swagger2
3. 选择依赖 `Spring Web Starter` 前面打钩,在模板列中勾选 `thymeleaf`。
4. 项目名设置为 `spring-boot-study-swagger2`.

# 2 Pom.xml 依赖管理
swagger2 使用了 io.springfox.springfox-swagger2 包名，[maven仓库地址](https://mvnrepository.com/artifact/io.springfox/springfox-swagger2)，截止目前最新版本为 2.9.2。

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
    <artifactId>swagger2</artifactId>
    <version>0.0.1-SNAPSHOT</version>
    <name>swagger2</name>
    <description>Demo project for Spring Boot</description>

    <properties>
        <java.version>1.8</java.version>
    </properties>

    <dependencies>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-web</artifactId>
        </dependency>

        <dependency>
            <groupId>io.springfox</groupId>
            <artifactId>springfox-swagger2</artifactId>
            <version>2.7.0</version>
        </dependency>
        <dependency>
            <groupId>io.springfox</groupId>
            <artifactId>springfox-swagger-ui</artifactId>
            <version>2.7.0</version>
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
我们修改了 application.yml

```yml
server:
  port: 8086
```

# 3 编写示例代码
回顾下 [Spring Boot RESTful api](https://www.cnblogs.com/fishpro/p/spring-boot-study-restful.html) 我们新增了
- controller/UserController.java
- domain/UserDO.java
进行测试，本章新增

## 3.1 配置 Swagger 
<font color=red> 注意：Swagger 的不同版本，配置其实是不同的。`@EnableSwagger2` 在 2.8.0 以及以上版本是找不到的，不知道网上大神是怎么引用的。所以我们引用 **Swagger 2.7.0** 版本 </font>

新建 `Swagger2Config.java` 包 `com.fishpro.swagger2.Swagger2Config`。
```java

@Configuration
@EnableSwagger2
public class Swagger2Config {
    @Bean
    public Docket createRestApi() {
        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(apiInfo())
                .select()
                .apis(RequestHandlerSelectors.withClassAnnotation(Api.class))//这是注意的代码
                .paths(PathSelectors.any())
                .build();
    }

    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title("xxx接口文档")
                .description("xxx相关接口的文档")
                .termsOfServiceUrl("http://www.xxx.com")
                .version("1.0")
                .build();
    }
}
```

在浏览器中输入 http://localhost:8086/swagger-ui.html

可以看到 swagger-ui 界面，但此时没有任何信息

## 3.2 注解概述
 
|     注解      |  注解说明 | 示例|
-------------|:----------|:-----|
| @Api | 用在类上标注类为 swagger 资源|@Api(value = "用户接口")|
| @ApiOperation | 用在方法上，说明方法的作用|@ApiOperation(value ="获取用户全部信息",tags = "查询用户",notes = "注意这里没有分页")|
| @ApiParam | 用在请求参数前，请求属性|同上第二条|
| @ApiImplicitParam | 用在方法上，在方法上独立表示参数|@ApiImplicitParam(name = "userId", value = "用户标识", required = true, paramType = "query", dataType = "String")|
| @ApiImplicitParams | 用在方法上，多个参数|多个 @ApiImplicitParam 集合|
| @ApiResponse | 用方法上，响应配置|@ApiResponse(code = 200, message = "成功返回用户信息") 对返回状态备注|
| @ApiResponses | 用方法上，响应集配置|用于多个 ApiResponse 配置|
| @ResponseHeader | 用方法上，响应头设置|相应投配置|
| @ApiModel | 用在实体对象的类上|在 UserDO 上加入 @ApiModel(value="user对象",description="用户对象user")|
| @ApiModelProperty | 用在实体对象属性上|在 @ApiModelProperty(value="用户名",name="username",example="fishpro")|
|@ApiIgnore|用在类上或方法上||


## 3.2 示例代码
本示例是对一个用户对象进行增删改查的操作，也是HTTP的 GET\POST\PUT\DELETE 操作。
如下描述，运行起来比较
### 3.2.1 实体类代码 domain/UserDO.java
```java

@ApiModel(value="user对象",description="用户对象user")
public class UserDO {
    @ApiModelProperty(value="用户ID",name="userId",example="1")
    private Integer userId;
    @ApiModelProperty(value="用户名",name="userName",example="fishpro")
    private String userName;

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }
}
```

### 3.3.2 控制层代码 UserController.java

```java
/**
 * RESTful API 风格示例 对资源 user 进行操作
 * 本示例没有使用数据库，也没有使用 service 类来辅助完成，所有操作在本类中完成
 * */
@RestController
@RequestMapping("/api/user")
@Api(value = "用户接口")
public class UserController {
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


    /**
     * SELECT 查询操作，返回一个JSON数组
     * 具有幂等性
     * */
    @GetMapping("/users")
    @ResponseStatus(HttpStatus.OK)
    @ApiOperation(value ="获取用户全部信息",tags = "查询用户",notes = "注意这里没有分页")
    public Object getUsers(){
        List<UserDO> list=new ArrayList<>();

        list=getData();

        return list;
    }

    /**
     * SELECT 查询操作，返回一个新建的JSON对象
     * 具有幂等性
     * */
    @GetMapping("/users/{id}")
    @ResponseStatus(HttpStatus.OK)
    @ApiOperation(value ="获取指定ID用户信息",tags = "查询用户",notes = "注意这里的参数id")
    public Object getUser(@PathVariable("id") @ApiParam(name="id",value="用户ID") String id){

        if(null==id){
            return  null;
        }

        List<UserDO> list= getData();
        UserDO userDO=null;
        for (UserDO user:list
             ) {
            if(id.equals(user.getUserId().toString())){
                userDO=user;
                break;
            }
        }

        return userDO;
    }

    /**
     * 新增一个用户对象
     * 非幂等
     * */
    @PostMapping("/users")
    @ResponseStatus(HttpStatus.CREATED)
    @ApiOperation(value ="新增一个用户",tags = "操作用户",notes = "客户端提供用户实体Json")
    @ApiImplicitParams({
            @ApiImplicitParam(name="user",value = "用户实体",required = true,dataType = "UserDO")
    })
    public Object addUser(@RequestBody UserDO user){

        List<UserDO> list= getData();
        list.add(user);//模拟向列表中增加数据
        return user;
    }

    /**
     * 编辑一个用户对象
     * 幂等性
     * */
    @PutMapping("/users/{id}")
    @ResponseStatus(HttpStatus.CREATED)
    @ApiOperation(value ="更新一个用户",tags = "操作用户",notes = "客户端提供用户实体Json")
    public Object editUser(@PathVariable("id") @ApiParam(name="id",value="用户ID") String id,@RequestBody UserDO user){
        List<UserDO> list= getData();
        for (UserDO userDO1:list
                ) {
            if(id.equals(userDO1.getUserId().toString())){
                userDO1=user;
                break;
            }
        }

        return user;
    }

    /**
     * 删除一个用户对象
     * 幂等性
     * */
    @DeleteMapping("/users")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @ApiOperation(value ="删除用户",tags = "操作用户",notes = "客户端提供用户实体Json")
    public Object deleteUser(@PathVariable("id") @ApiParam(name="id",value="用户ID") String id){
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
        return  userDO;//返回被删除的对象
    }
}
```

### 3.2.3 测试最终效果

![Swagger2效果图](https://www.cnblogs.com/images/cnblogs_com/fishpro/1453719/o_swagger2.png)




# 4 使用 swagger-bootstrap-ui 美化 SwaggerUI 界面
如上图，我认为 SwaggerUI 还是不错的，不算很丑，只是不符合当下互联网简约的审美观点。

在 pom 中加入 
```xml
        <dependency>
            <groupId>com.github.xiaoymin</groupId>
            <artifactId>swagger-bootstrap-ui</artifactId>
            <version>1.9.3</version>
        </dependency>
```
在浏览器中访问 http://localhost:8086/doc.html


![Swagger2-Bootstrap效果图](https://www.cnblogs.com/images/cnblogs_com/fishpro/1453719/o_swagger3.png)