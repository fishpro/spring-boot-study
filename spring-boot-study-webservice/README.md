WebService 虽然现在大部分互联网企业不太提倡使用，但在以第三方接口为主导的市场，对方来什么接口你还得用什么接口，不可能把接口重写了。例如大部分传统的大型企业都在用 WebService，并且版本还不一样。

本章主要介绍在 Spring Boot 下有常用的整合 WebService 的方法并给出示例。为了方便测试，本章有两个独立的项目
1. 用户的获取、增加、更新、删除 webservice 服务
2. 用于调用 1 的webservice 服务的客户端



# 1 新建 Spring Boot Maven 示例工程项目
注意：是用来 IDEA 开发工具
1. File > New > Project，如下图选择 `Spring Initializr` 然后点击 【Next】下一步
2. 填写 `GroupId`（包名）、`Artifact`（项目名） 即可。点击 下一步
    groupId=com.fishpro   
    artifactId=webserviceclient
3. 选择依赖 `Spring Web Starter` 前面打钩。
4. 项目名设置为 `spring-boot-study-webserviceclient`.
   
# 2 引入依赖 Pom
这里主要是引入 org.apache.cxf
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

# 3 编写一个用户获取、新增、修改、删除服务
## 3.1 传输对象 UserDto
通常我们把展示层与服务层之间传输的对象使用Dto后缀来标识。

UserDto（路径 src/main/java/com/fishpro/webservice/dto/UserDto.java）
```java

/**
 * 用户传输实体对象 通常我们把展示层与服务层之间传输的对象使用Dto后缀来标识。
 * */
public class UserDto {

    private Integer userId;//用户id
    private String userName;//用户名称
    private String password;//用户密码
    private Integer sex;//用户性别 0女 1男

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

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Integer getSex() {
        return sex;
    }

    public void setSex(Integer sex) {
        this.sex = sex;
    }
}
```

## 3.2 用户服务类
UserService 接口类（路径 src/main/java/com/fishpro/webservice/service/UserService.java）
```java
/**
 * 用户服务类 必须使用 @WebService
 * */
@WebService(targetNamespace = WsConst.NAMESPACE_URI ,name = "userPortType")
public interface UserService {
    /**
     * 根据用户id获取用户信息
     * */
    @WebMethod(operationName="getUserById")
    UserDto get(@WebParam(name = "id")  Integer id);

    /**
     * 获取全部用户信息
     * */
    @WebMethod(operationName="getUsers")
    List<UserDto> list();

    /**
     * 获取用户数
     * */
    @WebMethod(operationName="count")
    int count(@);

    /**
     * 新增用户
     * */
    @WebMethod(operationName="save")
    int save(@WebParam(name = "user")  UserDto user);

    /**
     * 更新用户
     * */
    @WebMethod(operationName="update")
    int update(@WebParam(name = "user")  UserDto user);

    /**
     * 删除用户
     * */
    @WebMethod(operationName="remove")
    int remove(@WebParam(name = "id")  Integer id);

    /**
     * 批量删除用户
     * */
    @WebMethod(operationName="batchRemove")
    int batchRemove(@WebParam(name = "ids")  Integer[] ids);
}
```

UserServiceImpl 接口类（路径 src/main/java/com/fishpro/webservice/service/UserServiceImpl.java）
```java

@WebService(
        targetNamespace = WsConst.NAMESPACE_URI, //wsdl命名空间
        name = "userPortType",                 //portType名称 客户端生成代码时 为接口名称
        serviceName = "userService",           //服务name名称
        portName = "userPortName",             //port名称
        endpointInterface = "com.fishpro.webservice.service.UserService")//指定发布webservcie的接口类，此类也需要接入@WebService注解
public class UserServiceImpl implements UserService {

    @Override
    public UserDto get(Integer id){
        if(null==id){
            return  null;
        }

        List<UserDto> list= getData();
        UserDto UserDto=null;
        for (UserDto user:list
                ) {
            if(id.equals(user.getUserId().toString())){
                UserDto=user;
                break;
            }
        }

        return UserDto;
    }

    @Override
    public List<UserDto> list(){
        List<UserDto> list=new ArrayList<>();

        list=getData();

        return list;
    }

    @Override
    public int count(){
        return getData().size();
    }

    @Override
    public int save(UserDto user){
        return 1;
    }

    @Override
    public int update(UserDto user){
        return 1;
    }

    @Override
    public int remove(Integer id){
        return 1;
    }

    @Override
    public int batchRemove(Integer[] ids){
        return 1;
    }

    /**
     * 模拟一组数据
     * */
    private List<UserDto> getData(){
        List<UserDto> list=new ArrayList<>();

        UserDto UserDto=new UserDto();
        UserDto.setUserId(1);
        UserDto.setUserName("admin");
        list.add(UserDto);

        UserDto=new UserDto();
        UserDto.setUserId(2);
        UserDto.setUserName("heike");
        list.add(UserDto);

        UserDto=new UserDto();
        UserDto.setUserId(3);
        UserDto.setUserName("tom");
        list.add(UserDto);

        UserDto=new UserDto();
        UserDto.setUserId(4);
        UserDto.setUserName("mac");
        list.add(UserDto);

        return  list;
    }

}
```

# 4 服务发布
 编写 CxfWebServiceConfig（路径 src/main/java/com/fishpro/webservice/config/CxfWebServiceConfig.java）
 ```java
 import com.fishpro.websevice.service.UserService;
import com.fishpro.websevice.service.impl.UserServiceImpl;
import org.apache.cxf.Bus;
import org.apache.cxf.bus.spring.SpringBus;
import org.apache.cxf.jaxws.EndpointImpl;
import org.apache.cxf.transport.servlet.CXFServlet;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.xml.ws.Endpoint;

@Configuration
public class CxfWebServiceConfig {

    /**
     *
     * */
    @Bean("cxfServletRegistration")
    public ServletRegistrationBean dispatcherServlet(){

        return new ServletRegistrationBean(new CXFServlet(),"/ws/*");
    }
    /**
     * 申明业务处理类 当然也可以直接 在实现类上标注 @Service
     */
    @Bean
    public UserService userService() {
        return new UserServiceImpl();
    }

    /*
     * 非必要项
     */
    @Bean(name = Bus.DEFAULT_BUS_ID)
    public SpringBus springBus() {
        SpringBus springBus = new SpringBus();
        return springBus;
    }

    /*
     * 发布endpoint
     */
    @Bean
    public Endpoint endpoint( ) {
        EndpointImpl endpoint = new EndpointImpl(springBus(), userService());
        endpoint.publish("/user");//发布地址
        return endpoint;
    }
}

 ```

 打开浏览器输入 http://localhost:8080/ws/user?wsdl 可以见到发布的效果


# 5 问题
1. cxf 的服务方法中，是不能使用java.util.Map作为参数的，因为本身不支持转换
cxf对很多复杂类型支持并不友好，建议参数能使用简单的类型，就使用简单的类型



---
参考：

[https://github.com/apache/cxf](https://github.com/apache/cxf)
[https://blog.lqdev.cn/2018/11/12/springboot/chapter-thirty-four/]https://blog.lqdev.cn/2018/11/12/springboot/chapter-thirty-four/