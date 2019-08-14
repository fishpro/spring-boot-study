Json 是目前互联网应用使用最为广泛的信息交换格式之一。Spring Boot 内置了 Jackson 。Json 在应用中主要体现在以下功能：

1. 序列化
2. 反序列化
3. 字段格式化
4. 验证自动化

目前长用的 Json 组件包括

1. Jackson
2. Gson
3. FastJson
4. JSON-B

本章主要讨论以上 4 中 Json 组件的 4 中功能。


[本项目源码 github 下载 ](https://github.com/fishpro/spring-boot-study/tree/master/spring-boot-study-json)


# 1 新建 Spring Boot Maven 示例工程项目

注意：是用来 IDEA 开发工具
1. File > New > Project，如下图选择 `Spring Initializr` 然后点击 【Next】下一步
2. 填写 `GroupId`（包名）、`Artifact`（项目名） 即可。点击 下一步
    groupId=com.fishpro   
    artifactId=json
3. 选择依赖 `Spring Web Starter` 前面打钩。
4. 项目名设置为 `spring-boot-study-json`.


# 2 编写代码用于测试示例

## 2.1 用户实体类

**新建两个实体类，用户类、用户地址类，他们的关系是父子关系**


User(路径 src/main/java/com/fishpro/json/dto/User.java)
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

Address (路径 src/main/java/com/fishpro/json/dto/Address.java)
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

## 2.2 控制层代码

UserController (路径 src/main/java/com/fishpro/json/controller/UserController.java)
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

运行 右键点击 JsonApplication > Run JsonApplication 在浏览器中输入 http://localhost:8086/user/get 系统直接返回了json格式，那么 Spring Boot 中默认就是哟合那个了 Jackson 来处理。

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
# 3 Jackson 

## 3.1 依赖引入 Jackson
**无须引入，Spring Boot 默认采用了 Jackson来处理诸如 @RequestBody @ResponseBody**

## 3.2 配置 Jackson
如 2。2 代码示例，默认采用了 Jackson ，但返回的日期格式没有展示人们常用的格式，就要我们从 applicaiton中配置他的展示格式。

```bash
server.port = 8086
#jackson
#日期格式化
spring.jackson.date-format=yyyy-MM-dd HH:mm:ss
#spring.jackson.date-format=yyyy-MM-dd
#格式化输出
spring.jackson.serialization.indent_output=true
#忽略无法转换的对象
spring.jackson.serialization.fail_on_empty_beans=false
#设置空如何序列化
spring.jackson.defaultPropertyInclusion=NON_EMPTY
#允许对象忽略json中不存在的属性
spring.jackson.deserialization.fail_on_unknown_properties=false
#允许出现特殊字符和转义符
spring.jackson.parser.allow_unquoted_control_chars=true
#允许出现单引号
spring.jackson.parser.allow_single_quotes=true
```

运行 右键点击 JsonApplication > Run JsonApplication 在浏览器中输入 http://localhost:8086/user/get 系统直接返回了json格式， 现在 created 以 yyy-MM-dd HH:mm:ss 展示给我们了。

```json
{
  "userId" : 1,
  "username" : "fishpro",
  "addresses" : [ {
    "street" : "江苏省南京市玄武大道1000号",
    "zipcode" : "201001",
    "mobile" : "1801989098"
  }, {
    "street" : "江苏省南京市玄武大道1001号",
    "zipcode" : "201001",
    "mobile" : "1811989098"
  } ],
  "created" : "2019-08-13 14:51:48"
}
```


## 3.3 Jackson 序列化
如何单独的使用序列化和反序列化功能呢
```java
 //测试 Jackson 序列化
        ObjectMapper mapper=new ObjectMapper();//定义一个转化对象
        List<Address> addressList=new ArrayList<>();
        addressList.add(new Address("江苏省南京市玄武大道1000号","201001","1801989098"));
        addressList.add(new Address("江苏省南京市玄武大道1001号","201001","1811989098"));
        User user = new User(1, "fishpro",addressList);
        try {
            String json = mapper.writeValueAsString(user);
            System.out.println(json);

        }catch (Exception e){
            e.printStackTrace();
        }
```

## 3.4 Jackson 反序列化 
```java
 //测试 Jackson 序列化
        ObjectMapper mapper=new ObjectMapper();//定义一个转化对象
        //测试 Jackjson 反序列化
        String json="{\"userId\":2,\"username\":\"程序员\",\"addresses\":[{\"street\":\"江苏省南京市玄武大道1000号\",\"zipcode\":\"201001\",\"mobile\":\"1801989098\"},{\"street\":\"江苏省南京市玄武大道1001号\",\"zipcode\":\"201001\",\"mobile\":\"1811989098\"}],\"created\":1565709784274}";

        try {
            User user2 = mapper.readValue(json, User.class);
            System.out.println(user2);

        }catch (Exception e){
            e.printStackTrace();
        }
```

## 3.5 常用注解
Jackson提供了一系列注解，方便对JSON序列化和反序列化进行控制，下面介绍一些常用的注解。
### @JsonIgnore
`@JsonIgnore` 此注解用于属性上，作用是进行JSON操作时忽略该属性。


### @JsonFormat
`@JsonFormat` 此注解用于属性上，作用是把Date类型直接转化为想要的格式，如@JsonFormat(pattern = "yyyy-MM-dd HH-mm-ss")。

例如为字段单独指定时间格式
```java
    /**更新时间 用户可以点击更新，保存最新更新的时间。**/
    @JsonFormat(pattern="yyyy-MM-dd")
    private Calendar updated = new GregorianCalendar();
```

### @JsonProperty
`@JsonProperty` 此注解用于属性上，作用是把该属性的名称序列化为另外一个名称，如把trueName属性序列化为name，@JsonProperty("name")。

## 3.6 问题：

>com.fasterxml.jackson.databind.exc.InvalidDefinitionException: Cannot construct instance of `com.fishpro.json.dto.User` (no Creators, like default construct, exist): cannot deserialize from Object value (no delegate- or property-based Creator)

因为实体类没有显示的 无参数构造函数，所有报错了,解决方案就是在实体类中增加无参数构造函数，例如本示例中
```java
public User(){}
```

```java
public Address(){}
```
 
# 4 fastjson
fastjson 是 阿里巴巴的开源项目。在国内使用非常广泛。

# 4.1 依赖引入

```xml
<!-- https://mvnrepository.com/artifact/com.alibaba/fastjson -->
<dependency>
    <groupId>com.alibaba</groupId>
    <artifactId>fastjson</artifactId>
    <version>1.2.58</version>
</dependency>
```

## 4.2 fastjson 序列化
```json
List<Address> addressList=new ArrayList<>();
        addressList.add(new Address("江苏省南京市玄武大道1000号","201001","1801989098"));
        addressList.add(new Address("江苏省南京市玄武大道1001号","201001","1811989098"));
        User user = new User(1, "fishpro",addressList);
        //测试 Fastjson 序列化
        System.out.println("测试 Fastjson 序列化");
        System.out.println(JSON.toJSONString(user));
```
输出结果
```json
{"addresses":[{"mobile":"1801989098","street":"江苏省南京市玄武大道1000号","zipcode":"201001"},{"mobile":"1811989098","street":"江苏省南京市玄武大道1001号","zipcode":"201001"}],"created":1565787589602,"userId":1,"username":"fishpro"}
```

## 4.3 fastjson 反序列化

```java 
        String json="{\"userId\":2,\"username\":\"程序员\",\"addresses\":[{\"street\":\"江苏省南京市玄武大道1000号\",\"zipcode\":\"201001\",\"mobile\":\"1801989098\"},{\"street\":\"江苏省南京市玄武大道1001号\",\"zipcode\":\"201001\",\"mobile\":\"1811989098\"}],\"created\":1565709784274}";

        //测试 Fastjson 反序列化
        System.out.println("测试 Fastjson 反序列化");
        User user3 = JSON.parseObject(json,User.class);
        System.out.println(user3);
```

## 4.4 fastjson 的注解
上面序列化、反序列化使用的实体类的注解还是 jackjson 中使用的。为了说明注解问题，我们新疆一个类 Person


### @JSONField
@JSONField 作用在类的属性上，是类的属性与输出的 Json 的映射。JSONField 的几个属性

|名称|用途|
|---|---|
|name|指定那么的时候表示 json 输出使用此 name @JSONField(name="ID") |
|format|格式化日期  @JSONField(format="yyyyMMdd")|
|serialize|false 表示不序列化 @JSONField(serialize=false)|
|deserialize|false 表示不反序列化 @JSONField(deserialize=false)|
|ordinal|f使用 ordinal 指定字段的顺序 @JSONField(ordinal = 3)|

```java

public class Person {

    @JSONField(name = "age")
    private int age;

    @JSONField(name = "full_name",ordinal = 1)
    private String fullName;

    @JSONField(name = "date_of_birth",format = "yyyy-MM-dd")
    private Date dateOfBirth;

    @JSONField(serialize = false)
    private String alias;

    public Person(int age, String fullName, Date dateOfBirth) {
        super();
        this.age = age;
        this.fullName= fullName;
        this.dateOfBirth = dateOfBirth;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public Date getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(Date dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }
}
```
## 4.5 BeanToArray 功能
```java
System.out.println("测试 Fastjson person 注解");
        Person person=new Person(100,"dashen",new Date()); 
        Person person2=new Person(98,"dashen2",new Date());
        Person person3=new Person(88,"dashen3",new Date());
        List<Person> personList=new ArrayList<>();
        personList.add(person);
        personList.add(person2);
        personList.add(person3);
        System.out.println(JSON.toJSONString(personList, SerializerFeature.BeanToArray));
```

输出
```json
{"age":100,"date_of_birth":"2019-08-14","full_name":"dashen"}
```

## 4.6 直接创建 JSON 对象

```java
 System.out.println("测试 Fastjson 生成 json");
        JSONArray jsonArray = new JSONArray();
        for (int i = 0; i < 2; i++) {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("AGE", 10);
            jsonObject.put("FULL NAME", "Doe " + i);
            jsonObject.put("DATE OF BIRTH", "2019/08/12 12:12:12");
            jsonArray.add(jsonObject);
        }
        System.out.println(jsonArray.toJSONString());
```

生成

```json
[{"DATE OF BIRTH":"2019/08/12 12:12:12","FULL NAME":"Doe 0","AGE":10},{"DATE OF BIRTH":"2019/08/12 12:12:12","FULL NAME":"Doe 1","AGE":10}]
```

## 4.7 过滤器 ValueFilter

|名称|说明|
|---|---|
|PropertyPreFilter| 根据PropertyName判断是否序列化 |
|PropertyFilter| 根据PropertyName和PropertyValue来判断是否序列化 |
|NameFilter| 修改Key，如果需要修改Key,process返回值则可 |
|ValueFilter| 修改Value |
|BeforeFilter| 序列化时在最前添加内容 |
|AfterFilter| 序列化时在最后添加内容|

过滤器一般用于特定场景用途。


-----
参考：

https://www.cnblogs.com/jian-xiao/p/6009435.html?utm_source=itdadao&utm_medium=referral
https://www.cnblogs.com/yuanmo396022993/p/9118308.html
https://www.runoob.com/w3cnote/fastjson-intro.html
http://kimmking.github.io/2017/06/06/json-best-practice/