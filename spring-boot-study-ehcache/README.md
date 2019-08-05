Ehcache 小巧轻便、具备持久化机制，不用担心JVM和服务器重启的数据丢失。经典案例就是著名的Hibernate的默认缓存策略就是用Ehcache，Liferay的缓存也是依赖Ehcache。

本章讲解 Ehcache 在 Spring Boot 中的使用，


[本项目源码下载](https://github.com/fishpro/spring-boot-study/tree/master/spring-boot-study-ehcache)


# 1 Ehcache 简介

ehcache 在 spring boot 中使用了一些注解，是哟合那个注解模式，有别于我们传统的写法，通常比如我们 javascript 中的
```js
localStorage.setItem("key","value");
localStorage.getItem("key");
```
而在 Spring Boot 中则没这些。



## 1.1 @Cacheable
@Cacheable 可以标记在一个方法上，也可以标记在一个类上。
- 当标记在一个方法上时表示该方法是支持缓存的，
- 当标记在一个类上时则表示该类所有的方法都是支持缓存的。
@Cacheable可以指定三个属性，value、key和condition。

1. **参数 value**
    缓存的名称，在 spring 配置文件中定义，必须指定至少一个

    如下代码实例。在 UserServiceImpl 实现类中指定了类的缓存名称 mycachename ，mycachename 必须与 ehcache.xml 中的缓存名称一致。
    ```java
    @Cacheable(value="mycachename")|
    public class UserServiceImpl{
        @Cacheable("cache1","cache2")
        public User get(Integer id){
            return null;
        }
    }
    ```
2. **参数 key 属性 自定义 key**
    
    key属性是用来指定Spring缓存方法的返回结果时对应的key的，支持SpringEL表达式
    ```java
    /**
    * key 是指传入时的参数
    *
    */
    @Cacheable(value="users", key="#id")
    public User find(Integer id) {
        returnnull;
    }
    // 表示第一个参数
    @Cacheable(value="users", key="#p0")
    public User find(Integer id) {
        returnnull;
    }
    // 表示User中的id值
    @Cacheable(value="users", key="#user.id")
    public User find(User user) {
        returnnull;
    }
    // 表示第一个参数里的id属性值
    @Cacheable(value="users", key="#p0.id")
    public User find(User user) {
        returnnull;
    }
    ```
    除了上述使用方法参数作为key之外，Spring还为我们提供了一个root对象可以用来生成key。通过该root对象我们可以获取到以下信息


3. **condition属性指定发生的条件**

    condition属性默认为空，表示将缓存所有的调用情形。其值是通过SpringEL表达式来指定的，当为true时表示进行缓存处理；当为false时表示不进行缓存处理

    如下实例，根据条件判断是否缓存 返回值存在 user.id中，但只有当 user.id 是 2 的倍数才会缓存

    ```java
    // 根据条件判断是否缓存 返回值存在 user.id中，但只有当 user.id 是 2 的倍数才会缓存
    @Cacheable(value={"users"}, key="#user.id", condition="#user.id%2==0")
    public User find(User user) {
        System.out.println("find user by user " + user);
        return user;
    }
    ```


## 1.2 @CachePut
@CachePut 可以标注在类上或方法上，只要标注了 @CachePut ，表示每次都会自行此方法，而不管是否已经有缓存存在。@CachePut 的使用是跟 @Cacheable 一样的。
```java
 //@CachePut也可以标注在类上和方法上。使用@CachePut时我们可以指定的属性跟@Cacheable是一样的。
   @CachePut("users")//每次都会执行方法，并将结果存入指定的缓存中
   public User find(Integer id) {
      returnnull;
   }
```
 
## 1.3 @CacheEvict
当需要清除缓存的时候，我们使用 `@CacheEvict` 注解来标注。@CacheEvict 可以指定的属性有value、key、condition、allEntries 和b eforeInvocation。

1. **allEntries属性**
   如下实例，allEntries=true 表示清除所有的对象，不管有没有指定 key 属性。
   ```java
   @CacheEvict(value="users", allEntries=true)
   public void delete(Integer id) {
      System.out.println("delete user by id: " + id);
    }
   ```

2. **beforeInvocation属性**
    如下实例，在调用方法前清除所有缓存 beforeInvocation=true
    ```java
    @CacheEvict(value="users", beforeInvocation=true)
    public void delete(Integer id) {
      System.out.println("delete user by id: " + id);
    }
    ```

## 1.4 @Caching
@Caching注解可以让我们在一个方法或者类上同时指定多个Spring Cache相关的注解,其拥有三个属性：cacheable、put和evict，分别用于指定@Cacheable、@CachePut和@CacheEvict。
```java
   @Caching(cacheable = @Cacheable("users"), evict = { @CacheEvict("cache2"),
   @CacheEvict(value = "cache3", allEntries = true) })
   public User find(Integer id) {
      returnnull;
   }
```
    

## 1.4 自定义注解
除了使用指定的注解外，我们还可以使用自定义注解
```java
   @MyCacheable
   public User findById(Integer id) {
      System.out.println("find user by id: " + id);
      User user = new User();
      user.setId(id);
      user.setName("Name" + id);
      return user;
   }
```

## 1.5 非注解方式使用方法


# 2 Spring Boot 下的 Ehcache 示例

上面介绍了 Ehcache 在 Spring Boot 中的注解的应用，这里我们相信编写一个示例说明具体的应用。


[本项目源码下载](https://github.com/fishpro/spring-boot-study/tree/master/spring-boot-study-ehcache)

## 2.1 新建 Spring Boot Maven 示例工程项目
注意：是用来 IDEA 开发工具
1. File > New > Project，如下图选择 `Spring Initializr` 然后点击 【Next】下一步
2. 填写 `GroupId`（包名）、`Artifact`（项目名） 即可。点击 下一步
    groupId=com.fishpro   
    artifactId=ehcache
3. 选择依赖 `Spring Web Starter` 前面打钩。
4. 项目名设置为 `spring-boot-study-ehcache`.
   
## 2.2 引入依赖
```xml
    <dependencies>
        <!-- spring boot cache starter-->
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-cache</artifactId>
        </dependency>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-web</artifactId>
        </dependency>

        <dependency>
            <groupId>net.sf.ehcache</groupId>
            <artifactId>ehcache</artifactId>
        </dependency>

        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-test</artifactId>
            <scope>test</scope>
        </dependency>
    </dependencies>
```
## 2.3 配置 ehcache.xml
在 src/main/resources/ehcache.xml 默认 spring boot 就会在 resources 目录下扫描到 ehcache 所以也不需要在 application.yml 中单独配置。

```xml
<?xml version="1.0" encoding="UTF-8"?>
<ehcache xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:noNamespaceSchemaLocation="ehcache.xsd">
    <!--timeToIdleSeconds 当缓存闲置n秒后销毁 -->
    <!--timeToLiveSeconds 当缓存存活n秒后销毁 -->
    <!-- 缓存配置
        name:缓存名称。
        maxElementsInMemory：缓存最大个数。
        eternal:对象是否永久有效，一但设置了，timeout将不起作用。
        timeToIdleSeconds：设置对象在失效前的允许闲置时间（单位：秒）。仅当eternal=false对象不是永久有效时使用，可选属性，默认值是0，也就是可闲置时间无穷大。
        timeToLiveSeconds：设置对象在失效前允许存活时间（单位：秒）。最大时间介于创建时间和失效时间之间。仅当eternal=false对象不是永久有效时使用，默认是0.，也就是对象存活时间无穷大。
        overflowToDisk：当内存中对象数量达到maxElementsInMemory时，Ehcache将会对象写到磁盘中。 diskSpoolBufferSizeMB：这个参数设置DiskStore（磁盘缓存）的缓存区大小。默认是30MB。每个Cache都应该有自己的一个缓冲区。
        maxElementsOnDisk：硬盘最大缓存个数。
        diskPersistent：是否缓存虚拟机重启期数据 Whether the disk
        store persists between restarts of the Virtual Machine. The default value
        is false.
        diskExpiryThreadIntervalSeconds：磁盘失效线程运行时间间隔，默认是120秒。  memoryStoreEvictionPolicy：当达到maxElementsInMemory限制时，Ehcache将会根据指定的策略去清理内存。默认策略是
LRU（最近最少使用）。你可以设置为FIFO（先进先出）或是LFU（较少使用）。
        clearOnFlush：内存数量最大时是否清除。 -->
    <!-- 磁盘缓存位置 -->
    <diskStore path="java.io.tmpdir" />
    <!-- 默认缓存 -->
    <defaultCache
            maxElementsInMemory="10000"
            eternal="false"
            timeToIdleSeconds="120"
            timeToLiveSeconds="120"
            maxElementsOnDisk="10000000"
            diskExpiryThreadIntervalSeconds="120"
            memoryStoreEvictionPolicy="LRU">

        <persistence strategy="localTempSwap" />
    </defaultCache>

    <!-- 测试 -->
    <cache name="fpcache"
           eternal="false"
           timeToIdleSeconds="2400"
           timeToLiveSeconds="2400"
           maxEntriesLocalHeap="10000"
           maxEntriesLocalDisk="10000000"
           diskExpiryThreadIntervalSeconds="120"
           overflowToDisk="false"
           memoryStoreEvictionPolicy="LRU">
    </cache>
</ehcache>
```

## 2.4 开启缓存
在 application 中开启缓存
```java
// @EnableCaching 开启缓存
@SpringBootApplication
@EnableCaching
public class EhcacheApplication {

    public static void main(String[] args) {
        SpringApplication.run(EhcacheApplication.class, args);
    }

}
```
## 2.5 示例代码
模拟一个增删改查来设置缓存示例，包括了
- UserService 用户服务类
- UserServiceImpl 用户服务实现类
- UserController 用户控制层类使用了 RestController
UserService 用户接口（路径 src/main/java/com/fishpro/ehcache/service/UserService.java）
```java
public interface UserService {

    List<UserDO> list();
    UserDO get(Integer id);
    UserDO save(UserDO user);
    UserDO update(UserDO user);
    void delete(Integer id);

}
```

UserServiceImpl（路径 src/main/java/com/fishpro/ehcache/service/impl/UserServiceImpl.java）
```java
// fpcache 对应 ehcache.xml 中的 fpcache 节点
@CacheConfig(cacheNames = {"fpcache"})
@Service
public class UserServiceImpl implements UserService {
    @Override
    @Cacheable("fpcache")
    public List<UserDO> list() {
        List<UserDO> list=new ArrayList<>();
        list.add( new UserDO(1,"fishpro","123456",1));
        list.add( new UserDO(2,"fishpro2","123456",1));
        list.add( new UserDO(3,"fishpro3","123456",1));
        System.out.println("获取用户列表使用 @Cacheable 注意执行第二次的时候不会有本语句输出了，部分删除掉缓存");
        return list;
    }

    @Override
    @Cacheable(value = "fpcache",key = "#id")
    public UserDO get(Integer id) {
        System.out.println("获取单个用户 get user by"+id);
        return new UserDO(1,"fishpro","123456",1);

    }

    @Override
    @CachePut(value = "fpcache",key = "#user.id")
    public UserDO save(UserDO user) {
        System.out.println("保存用户使用 @CachePut 每次都会执行语句并缓存 save user by "+user.getUserName());
        return user;

    }

    @Override
    @CachePut(value = "fpcache",key = "#user.id")
    public UserDO update(UserDO user) {
        System.out.println("更新用户使用 @CachePut 每次都会执行语句并缓存 update user by "+user.getUserName());
        return user;

    }

    @Override
    @CacheEvict(allEntries = true)
    public void delete(Integer id) {
        System.out.println("删除用户根据用户ID，如果 allEntries = true 则不论 key 是啥都全部删除缓存"+id);
    }
}

```

UserController（路径 src/main/java/com/fishpro/ehcache/controller/UserController.java）
```java

@RestController
public class UserController {
    @Autowired
    private UserService userService;
    @GetMapping("/test")
    public String testCache(){
        System.out.println("============以下第一次调用 ================");
        userService.list();
        userService.get(1);
        userService.save(new UserDO(1,"fishpro","123456",1));
        userService.update(new UserDO(1,"fishpro","123456434",1));

        System.out.println("============以下第二次调用 观察 list 和 get 方法 ================");

        userService.list();
        userService.get(1);
        userService.save(new UserDO(1,"fishpro","123456",1));
        userService.update(new UserDO(1,"fishpro","123456434",1));


        System.out.println("============以下第三次调用 先删除 观察 list 和 get 方法 ================");
        userService.delete(1);
        userService.list();
        userService.get(1);
        userService.save(new UserDO(1,"fishpro","123456",1));
        userService.update(new UserDO(1,"fishpro","123456434",1));
        return  "";
    }
}

```


## 2.6 运行效果
右键 EhcacheApplication 选择 Run EhcacheApplication

![ehcache 运行示例](https://www.cnblogs.com/images/cnblogs_com/fishpro/1453719/o_ehcache1.jpg)



## 2.7 遇到的问题
Cannot find cache named 'users' 
>java.lang.IllegalArgumentException: Cannot find cache named 'users' for Builder[public java.util.List com.fishpro.ehcache.service.impl.UserServiceImpl.list()] caches=[users] | key='' | keyGenerator='' | cacheManager='' | cacheResolver='' | condition='' | unless='' | sync='false'

以上问题说明注解中的 value 值没有对应的缓存名称。解决方案就是 value 的名称设置为 ehcache.xml 中的一样。



参考
------
https://blog.csdn.net/dreamhai/article/details/80642010


[本项目源码下载](https://github.com/fishpro/spring-boot-study/tree/master/spring-boot-study-ehcache)