如果你要问 Spring Boot 做什么最厉害，我想答案就在本章标题 RESTful API 简称 REST API 。



[本项目源码下载](https://github.com/fishpro/spring-boot-study/tree/master/spring-boot-study-restful)

# 1 RESTful API 概述
## 1.1 什么是 RESTful API
Rest 是一种规范，符合 Rest 的 Api 就是 Rest Api。简单的说就是可联网设备利用 HTTP 协议通过 GET、POST、DELETE、PUT、PATCH 来操作具有URI标识的服务器资源，返回统一格式的资源信息，包括 JSON、XML、CSV、ProtoBuf、其他格式。 
 



## 1.2 RESTful API 设计规范

![RESTful API](https://www.cnblogs.com/images/cnblogs_com/fishpro/1453719/o_restful1.png)



### 1.2.1 协议
建议使用基于 HTTPS 协议

### 1.2.2 域名
建议使用 api.domain.com

### 1.2.3 版本
- 建议把版本放到 URL 中，即显式设计。如 https://api.domain.com/v1
- 建议吧版本放到 HTTP 头信息中。不如放到 URL 中直观。

### 1.2.4 路径
在RESTful架构中，每个网址代表一种资源（resource），所以网址中不能有动词，只能有名词，而且所用的名词往往与数据库的表格名对应。

### 1.2.5 HTTP 动词
常用的动词包括了5个
- GET（SELECT）：从服务器取出资源（一项或多项）。
- POST（CREATE）：在服务器新建一个资源。
- PUT（UPDATE）：在服务器更新资源（客户端提供改变后的完整资源）。
- PATCH（UPDATE）：在服务器更新资源（客户端提供改变的属性）。
- DELETE（DELETE）：从服务器删除资源。
  
### 1.2.6 过滤信息
如果记录数量很多，服务器不可能都将它们返回给用户。API应该提供参数，过滤返回结果。

- api.domain.com/v1/users?limit=10 ：指定返回记录的数量
- api.domain.com/v1/users?offset=10：指定返回记录的开始位置
- api.domain.com/v1/users?page=2&per_page=100：指定第几页，以及每页的记录数
- api.domain.com/v1/users?sortby=name&order=asc：指定返回结果按照哪个属性排序，以及排序顺序
- api.domain.com/v1/users?animal_type_id=1：指定筛选条件


### 1.2.7 状态码
建议使用 HTTP 的状态码
- 200 OK - [GET]：服务器成功返回用户请求的数据，该操作是幂等的（Idempotent）。
- 201 CREATED - [POST/PUT/PATCH]：用户新建或修改数据成功。
- 202 Accepted - [*]：表示一个请求已经进入后台排队（异步任务）
- 204 NO CONTENT - [DELETE]：用户删除数据成功。
- 400 INVALID REQUEST - [POST/PUT/PATCH]：用户发出的请求有错误，服务器没有进行新建或修改数据的操作，该操作是幂等的。
- 401 Unauthorized - [*]：表示用户没有权限（令牌、用户名、密码错误）。
- 403 Forbidden - [*] 表示用户得到授权（与401错误相对），但是访问是被禁止的。
- 404 NOT FOUND - [*]：用户发出的请求针对的是不存在的记录，服务器没有进行操作，该操作是幂等的。
- 406 Not Acceptable - [GET]：用户请求的格式不可得（比如用户请求JSON格式，但是只有XML格式）。
- 410 Gone -[GET]：用户请求的资源被永久删除，且不会再得到的。
- 422 Unprocesable entity - [POST/PUT/PATCH] 当创建一个对象时，发生一个验证错误。
- 500 INTERNAL SERVER ERROR - [*]：服务器发生错误，用户将无法判断发出的请求是否成功。

[详细状态码](https://www.w3.org/Protocols/rfc2616/rfc2616-sec10.html)

### 1.2.8 错误处理
返回指定错误信息内容
```json
{
    "error":"err message"
}
```

### 1.2.9 返回结果
针对不同操作，服务器向用户返回的结果应该符合以下规范。

- GET /collection：返回资源对象的列表（数组）
- GET /collection/resource：返回单个资源对象
- POST /collection：返回新生成的资源对象
- PUT /collection/resource：返回完整的资源对象
- PATCH /collection/resource：返回完整的资源对象
- DELETE /collection/resource：返回一个空文档 

### 1.2.10 超级链接
RESTful API最好做到Hypermedia，即返回结果中提供链接，连向其他API方法，使得用户不查文档，也知道下一步应该做什么。

以上信息主要来自[RESTful API 设计指南](http://www.ruanyifeng.com/blog/2014/05/restful_api.html)

# 2 Spring Boot 中如何使用 RESTful API

本章节需要编写的是对一个用户的增删改查操作，如下表是一个非 RESTful 和 标准 RESTful 的对比表。

| Api Name   |     非RESTful      |  RESTful Api |
|----------|:-------------:|------:|
| 获取用户|  /user/query/1 | /users/1  GET|
| 新增用户|    /user/add   |   /users  POST|
| 更新用户| /user/edit |    /users  PUT|
| 删除用户| /user/delete |    /users  DELETE|

## 2.1 新建 Spring Boot 项目 

1）File > New > Project，如下图选择 `Spring Initializr` 然后点击 【Next】下一步

2）填写 `GroupId`（包名）、`Artifact`（项目名） 即可。点击 下一步
groupId=com.fishpro   
artifactId=restful
  
3)选择依赖 `Spring Web Starter` 前面打钩。

4)项目名设置为 `spring-boot-study-restful`

## 2.2 编写示例代码
本次新增2个文件，其中UserController类中包括了对用户的4个操作增删改查。
1. controller/UserController.java
2. domain/UserDO.java

### 2.2.1 UserDO 实体类代码
```java
public class UserDO {
    private Integer userId;
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

### 2.2.2 UserController 类代码
```java
/**
 * RESTful API 风格示例 对资源 user 进行操作
 * 本示例没有使用数据库，也没有使用 service 类来辅助完成，所有操作在本类中完成
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
     * */
    @DeleteMapping("/users")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public Object deleteUser(@PathVariable("id") String id){
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


### 2.2.3 使用 PostMan 测试

**获取全部资源 获取所有用户**

GET http://localhost:8086/api/user/users/
```json
[
    {
        "userId": 1,
        "userName": "admin"
    },
    {
        "userId": 2,
        "userName": "heike"
    },
    {
        "userId": 3,
        "userName": "tom"
    },
    {
        "userId": 4,
        "userName": "mac"
    }
]
```

**获取单个资源 获取用户**

GET http://localhost:8086/api/user/users/3
```json
{
    "userId": 3,
    "userName": "tom"
}
```

**新增一个资源 新增一个用户**
POST http://localhost:8086/api/user/users
请求参数
```json
{
    "userId": 4,
    "userName": "newname"
}
```
**编辑更新一个资源**

POST http://localhost:8086/api/user/users
请求参数
```json
{
    "userId": 1,
    "userName": "editname"
}
```

**删除一个资源**

DELETE http://localhost:8086/api/user/users
请求参数
```json
{
    "userId": 1
}
```

### 2.2.4 其他辅助设计
RESTful API 可以结合 [@RestControllAdvie 做全局异常处理](https://www.cnblogs.com/fishpro/p/11179688.html)，可以使用[自定义标签做日志拦截，可以做全局日志拦截](https://www.cnblogs.com/fishpro/p/11167469.html)，可以做自动数据验证等等。

# 3 为什么不推荐使用 RESTful API
RESTful API 固然很好但大多数互联网公司都没有按照其规则来设计。因为 REST 本来就是一种风格，并没有什么固定的规则来约束，基于过于理想的 RESTful API 只会付出更多的人力成本和时间成本。

## 3.1 操作方式繁琐，没有效率，且意义不大
使用 HTTP 的 GET\POST\PUT\DELETE 来区分操作资源，HTTP Method 本身就对外部不友好，是隐藏的方法，把动词加入到 url 中，反而清晰可见，简单易懂，为什么一定要用 url 来表示资源而不能加动词呢。

GET\POST\PUT\DELETE 的兼容性有待认证，首先是兼容老的系统，大部分 HTTP 应用是基于 GET/POST 来实现的。

## 3.2 过分强调单一资源
这可能是人的理解问题，就看我们对资源的定义，太多的场景，如果使用 Restful API 规则行事，势必要把一个 API 拆分多个 API，框架多个 API 之间的状态又成了问题。

## 3.3 返回值问题
使用 HTTP Status 状态码是没有问题的，但使用不常见的状态码表示操作层面的含义，对开发不是很友好。
对返回集中定义 status、message 例如下面的 json 返回是很常见的返回，简单易懂。并没有什么不妥，但在 RESTful Api 拥护者眼里就是错误的格式，错误的返回。


```json
{
    "status":1,
    "message":"",
    "data":[]
}
```

## 3.4 更高的成本
统一为 RESTful API 风格，强化要求做的完美，比如带来更多的时间成本、人力成本。沟通成本。

[本项目源码下载](https://github.com/fishpro/spring-boot-study/tree/master/spring-boot-study-restful)

---

关联阅读：

[Spring Boot Log 日志使用教程](https://www.cnblogs.com/fishpro/p/11167469.html)

[Spring Boot 全局异常处理](https://www.cnblogs.com/fishpro/p/11179688.html)

