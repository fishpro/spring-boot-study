测试 Spring Boot Web 的时候，我们需要用到 MockMvc，即系统伪造一个 mvc 环境。本章主要编写一个基于 RESTful API 正删改查操作的测试用例。本章最终测试用例运行结果如下：


[本项目源码下载](https://github.com/fishpro/spring-boot-study/tree/master/spring-boot-study-resttest)

# 1 MockMvc 简介
Spring Boot Web 项目中我们采用 MockMvc 进行模拟测试
 
|    方法   |     说明      |
|----------|:-------------|
| mockMvc.perform| 执行一个请求 | 
| MockMvcRequestBuilders.get("XXX")| 构造一个请求 | 
| ResultActions.param| 添加请求传值 | 
| ResultActions.accept())| 执行一个请求 如MediaType.TEXT_HTML_VALUE | 
| ResultActions.andExpect| 添加执行完成后的断言。 等同于 `Assert.assertEquals`  | 
| ResultActions.andDo| 添加一个结果处理器，表示要对结果做点什么事情，比如此处使用MockMvcResultHandlers.print()输出整个响应结果信息 | 
| ResultActions.andReturn | 表示执行完成后返回相应的结果。 | 

示例,注意注释部分与 addExpect 是等效的，就是断言
```java
 /**
     * 测试 Hello World 方法
     * */
    @Test
    public void hello() throws  Exception{
       MvcResult mvcResult= mockMvc.perform(MockMvcRequestBuilders.get("/api/user/hello")
                .param("name","fishpro")
                .accept(MediaType.TEXT_HTML_VALUE)) //perform 结束
                .andExpect(MockMvcResultMatchers.status().isOk()) //添加断言
                .andExpect(MockMvcResultMatchers.content().string("Hello fishpro"))//添加断言
                .andDo(MockMvcResultHandlers.print()) //添加执行
                .andReturn();//添加返回

        //下面部分等等与 addExcept 部分
        //        int status=mvcResult.getResponse().getStatus();                 //得到返回代码
        //        String content=mvcResult.getResponse().getContentAsString();    //得到返回结果
        //        Assert.assertEquals(200,status);                        //等于 andExpect(MockMvcResultMatchers.status().isOk()) //添加断言
        //        Assert.assertEquals("Hello World",content);            //andExpect(MockMvcResultMatchers.content().string("Hello World"))//添加断言
    }
```
**围绕 MockMvc 其实就是几个核心的问题**
1. 如何初始化对应类的mockMvc
2. 如何建立请求包括请求的 url、请求的c ontentType 
3. 如何发送请求参数，包括如何发送 url 参数、form 参数、 json 参数、xml 参数
4. 如何编写断言判断
5. 如何打印信息
6. MockMvc本身的返回


# 2 代码实例
本项目主要使用 [SpringBoot RESTful API 架构风格实践 ](https://www.cnblogs.com/fishpro/p/spring-boot-study-restful.html) 代码。你可以下载此代码，也可以重新新建一个 Spring Boot 项目用于测试。



[本项目源码下载](https://github.com/fishpro/spring-boot-study/tree/master/spring-boot-study-resttest)



## 2.1 创建一个 Spring Boot 项目
## 2.2 pom.xml 依赖管理
除了 web 引入，其他不需要增加额外的依赖

## 2.2 编写 Restful 接口部分
新建包 controller 在 com.fishpro.resttest.controller 下新建 UserController.java ,本示例中安装 Restful API 标准编写了 增删改查接口。
```java
package com.fishpro.resttest.controller;

import com.fishpro.resttest.domain.UserDO;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import java.util.ArrayList;
import java.util.List;

/**
 * RESTful API 风格示例 对资源 user 进行操作
 * 本示例没有使用数据库，也没有使用 service 类来辅助完成，所有操作在本类中完成
 * 请注意几天
 *    1.RESTful 风格使用 HttpStatus 状态返回 GET PUT PATCH DELETE 通常返回 201 Create ，DELETE 还有时候返回 204 No Content
 *    2.使用 RESTful 一定是要求具有幂等性，GET PUT PATCH DELETE 本身具有幂等性，但 POST 不具备，无论规则如何定义幂等性，需要根据业务来设计幂等性
 *    3.RESTful 不是神丹妙药，实际应根据实际情况来设计接口
 * */
@RestController
@RequestMapping("/api/user")
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
     * 测试用 参数为 name
     * */
    @RequestMapping("/hello")
    public String hello(String name){
        return "Hello "+name;
    }

    /**
     * SELECT 查询操作，返回一个JSON数组
     * 具有幂等性
     * */
    @GetMapping("/users")
    @ResponseStatus(HttpStatus.OK)
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
    public Object getUser(@PathVariable("id") String id){

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
     * 返回 201 HttpStatus.CREATED 对创建新资源的 POST 操作进行响应。应该带着指向新资源地址的 Location 头
     * */
    @PostMapping("/users")
    @ResponseStatus(HttpStatus.CREATED)
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
    public Object editUser(@PathVariable("id") String id,@RequestBody UserDO user){
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
     * 返回 HttpStatus.NO_CONTENT 表示无返回内容
     * */
    @DeleteMapping("/users/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteUser(@PathVariable("id") String id){
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
    }
}

```

## 2.3 编写测试部分
1. 新建类 com.fishpro.resttest.UserControllerTests
2. 给测试类增加测试注解 @RunWith(SpringRunner.class) @SpringBootTest，增加mockMvc私有变量
3. 在 @Before方法体初始化mocMvc
    ```java
    /**
     * 初始化 MockMvc
     * */
    @Before
    public void setUp(){
        
        mockMvc = MockMvcBuilders.standaloneSetup(new UserController()).build();
    }
    ```
4. 编写测试方法，在方法名上增加 @Test注解，在方法体内 使用 mockmvc 进行测试
   - mockMvc.perform 执行一个请求
   - MockMvcRequestBuilders.get("") 构造一个请求
   - **ResultActions.param 添加请求传值，注意这里的param只能传递 url中的值，@RequestBody 是需要 contentType().content(json二进制)传递的**

UserControllerTests.java 代码
```java
/**
 * 本示例针对 Restful API 风格接口做全面的测试用例
 * fishpro at 2019-07-20
 * 注意事项
 *    1.param(name,value) 只能用于 url 参数传递，form 表单传递
 *    2. @RequestBody 方法，对应使用 .contentType(MediaType.APPLICATION_JSON).content(json 字符串)
 *    3.大部分测试用例失败的原因是传递参数对应的contentType不正确
 * */
@RunWith(SpringRunner.class)
@SpringBootTest
public class UserControllerTests {

    private MockMvc mockMvc;//定义一个 MockMvc

    /**
     * 初始化 MockMvc 通过MockMvcBuilders.standaloneSetup 模拟一个 UserController 测试环境，通过build得到一个MockMvc
     * */
    @Before
    public void setUp(){

        mockMvc = MockMvcBuilders.standaloneSetup(new UserController()).build();
    }
    /**
     * 测试 Hello World 方法
     * hello 方法是一个 get 方法，使用了 url 参数传递参数 所以使用了 .param 来传递参数
     * accept(MediaType.TEXT_HTML_VALUE) 来设置传递值接收类型
     * */
    @Test
    public void hello() throws  Exception{
       MvcResult mvcResult= mockMvc.perform(MockMvcRequestBuilders.get("/api/user/hello")
                .param("name","fishpro")
                .accept(MediaType.TEXT_HTML_VALUE)) //perform 结束
                .andExpect(MockMvcResultMatchers.status().isOk()) //添加断言
                .andExpect(MockMvcResultMatchers.content().string("Hello fishpro"))//添加断言
                .andDo(MockMvcResultHandlers.print()) //添加执行
                .andReturn();//添加返回

        //下面部分等等与 addExcept 部分
        //int status=mvcResult.getResponse().getStatus();                 //得到返回代码
        //String content=mvcResult.getResponse().getContentAsString();    //得到返回结果
        //Assert.assertEquals(200,status);                        //等于 andExpect(MockMvcResultMatchers.status().isOk()) //添加断言
        //Assert.assertEquals("Hello World",content);            //andExpect(MockMvcResultMatchers.content().string("Hello World"))//添加断言
    }

    /**
     * 测试用户列表获取 /users GET
     * */
    @Test
    public void getUsers() throws  Exception{
        mockMvc.perform(MockMvcRequestBuilders.get("/api/user/users")
                .accept(MediaType.APPLICATION_JSON)) //perform 结束
                .andExpect(MockMvcResultMatchers.status().isOk()) //andExpect
                .andDo(MockMvcResultHandlers.print()) //andDo
                .andReturn();//andReturn
    }

    /**
     * 获取单个用户信息 /users/3 GET
     * */
    @Test
    public void getUser() throws  Exception{
        mockMvc.perform(MockMvcRequestBuilders.get("/api/user/users/3")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(MockMvcResultHandlers.print())
                .andReturn();
    }

    /**
     * 新增单个用户信息 /users/ POST
     * 注意 addUser 使用了 @RequestBody 方法，对应使用 .contentType(MediaType.APPLICATION_JSON).content(json 字符串)
     * */
    @Test
    public void addUser() throws  Exception{
        mockMvc.perform(MockMvcRequestBuilders.post("/api/user/users")
                .contentType(MediaType.APPLICATION_JSON).content("{ \"userId\": 3,\"userName\": \"tom\"}"))
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andDo(MockMvcResultHandlers.print())
                .andReturn();
    }


    /**
     * 编辑一个用户 /users/ PUT
     * */
    @Test
    public void editUser() throws  Exception{
        mockMvc.perform(MockMvcRequestBuilders.put("/api/user/users/3")
                .contentType(MediaType.APPLICATION_JSON).content("{ \"userId\": 3,\"userName\": \"tom\"}"))
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andDo(MockMvcResultHandlers.print())
                .andReturn();
    }


    /**
     * 删除一个用户 /users/ DELETE
     * */
    @Test
    public void deleteUser() throws  Exception{
        mockMvc.perform(MockMvcRequestBuilders.delete("/api/user/users/3")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isNoContent())
                .andDo(MockMvcResultHandlers.print())
                .andReturn();
    }
}
```

## 2.4 运行实例
右键测试类 选择 **Run UserControllerTests with Coverage**

![mockmvc测试图](https://www.cnblogs.com/images/cnblogs_com/fishpro/1453719/o_restfultest1.png)

## 2.4 值得注意的几个问题
1.请求结果为400 406 （httpstatus）
这个原因通常是 请求参数设置不正确，如 json 应该使用 

```java
.contentType(MediaType.APPLICATION_JSON).content("{ \"userId\": 3,\"userName\": \"tom\"}"))
```