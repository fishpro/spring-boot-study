Redis 在 Spring Boot 2.x 中相比 1.5.x 版本，有一些改变。redis 默认链接池，1.5.x 使用了 jedis，而2.x 使用了 lettuce

Redis 接入 Spring Boot 缓存，使用的注解跟 Ehcache 接入缓存的注解是一样的，[Spring Boot 缓存应用 Ehcache 入门教程](https://www.cnblogs.com/fishpro/p/spring-boot-study-ehcache.html#_label0_0)

安装 Redis 请参见 如何在 Mac 下安装 Redis 和 如何在 Window 下安装 Redis


本文仅仅适用
- spring boot 2.x
- redis
- jdk 1.8+



[本项目源码下载](https://github.com/fishpro/spring-boot-study/tree/master/spring-boot-study-redis)

# 0 注意
本章代码与 [Spring Boot 缓存应用 Ehcache 入门教程](https://www.cnblogs.com/fishpro/p/spring-boot-study-ehcache.html#_label0_0) 基本是相同的，不同的是 Ehcache 换成了 Redis。**有几点需要注意**

- 本示例代码是基于 Spring Boot 2.1.6，Redis for Spring Boot 2.x 的配置与 Spring Boot 1.5.x 配置有细微的差别，主要是 Redis 默认连接池区别。
- 实体类 UserDO 必须要求支持序列化，继承与  implements Serializable 
- Redis 没有进行 xml 配置文件配置，使用了 @Configuration 进行配置

# 1 新建 Spring Boot Maven 示例工程项目
注意：是用来 IDEA 开发工具
1. File > New > Project，如下图选择 `Spring Initializr` 然后点击 【Next】下一步
2. 填写 `GroupId`（包名）、`Artifact`（项目名） 即可。点击 下一步
    groupId=com.fishpro   
    artifactId=redis
3. 选择依赖 `Spring Web Starter` 前面打钩。
4. 项目名设置为 `spring-boot-study-redis`.
   
# 2 引入依赖 Pom

- fastjson
- redis

```xml
 <dependencies>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-web</artifactId>
        </dependency>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-data-redis</artifactId>
        </dependency>
        <dependency>
            <groupId>com.alibaba</groupId>
            <artifactId>fastjson</artifactId>
            <version>1.2.44</version>
        </dependency>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-test</artifactId>
            <scope>test</scope>
        </dependency>
    </dependencies>
```

同时配置 application.yml 的 redis 服务器地址等信息，本示例安装在本机的 redis 密码为空
```yml
#2.x版本中由于引入了不同客户端，需要指定配置哪种连接池
#jedis客户端
spring:
  cache:
    type: redis
  redis:
    host: 127.0.0.1
    port: 6379
    password:
    database: 0
    jedis:
      pool:
        max-active: 8
        max-wait: -1ms
        max-idle: 8
        min-idle: 0

```

# 3 Redis 使用注解实现缓存

使用注解方式，不需要做配置也可以运行示例，我们这里不用编写配置文件，只要在启动类上加上 `@EnableCaching` 注解就可以使用

## 3.1  示例代码
整个业务代码包括了
模拟一个增删改查来设置缓存示例，包括了
- UserService 用户服务类
- UserServiceImpl 用户服务实现类
- UserController 用户控制层类使用了 RestController
  
UserService 用户接口（路径 src/main/java/com/fishpro/redis/service/UserService.java）
```java
public interface UserService {

    List<UserDO> list();
    UserDO get(Integer id);
    UserDO save(UserDO user);
    UserDO update(UserDO user);
    void delete(Integer id);

}
```

UserServiceImpl（路径 src/main/java/com/fishpro/redis/service/impl/UserServiceImpl.java）
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

UserController（路径 src/main/java/com/fishpro/redis/controller/UserController.java）
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

## 3.2 运行

右键 RedisApplication 选择 Run RedisApplication 在浏览器中输入 http://localhost:8080/test

![spring boot 2.x 使用 redis 注解运行效果](https://www.cnblogs.com/images/cnblogs_com/fishpro/1453719/o_redis1.jpg)


# 4 Redis 使用 RedisTemplate 方式实现 
与使用注解方式不同，注解方式可以零配置，只需引入依赖并在启动类上加上 @EnableCaching 注解就可以使用；而使用 RedisTemplate 方式麻烦些，需要做一些配置

本示例中设置 redis 为 StringRedisSerializer
## 4.1 配置
```yml
#2.x版本中由于引入了不同客户端，需要指定配置哪种连接池
#jedis客户端
spring:
  cache:
    type: redis
  redis:
    host: 127.0.0.1
    port: 6379
    password:
    database: 0
    jedis:
      pool:
        max-active: 8
        max-wait: -1ms
        max-idle: 8
        min-idle: 0

```

然后写个 RedisConfig.java 配置类，本示例中设置 redis 为 StringRedisSerializer

```java
@Configuration
public class RedisConfig extends CachingConfigurerSupport {

    @Bean
    public RedisTemplate<Object,Object> redisTemplate(RedisConnectionFactory redisConnectionFactory){
        RedisTemplate<Object,Object> template = new RedisTemplate<>();
        //设置连接池
        template.setConnectionFactory(redisConnectionFactory);
        StringRedisSerializer stringRedisSerializer=new StringRedisSerializer();
         
        template.setValueSerializer(serializer);
        //hash value的序列化问题
        template.setHashValueSerializer(serializer);
        //key 的序列化
        template.setKeySerializer(new StringRedisSerializer());
        template.setHashKeySerializer(new StringRedisSerializer());

        return  template;
    }

    @Bean
    public CacheManager cacheManager(RedisConnectionFactory factory){

        // 生成一个默认配置，通过config对象即可对缓存进行自定义配置
        RedisCacheConfiguration config = RedisCacheConfiguration.defaultCacheConfig();
        // 设置缓存的默认过期时间，也是使用Duration设置
        config = config.entryTtl(Duration.ofMinutes(1))
                .disableCachingNullValues();     // 不缓存空值

        // 设置一个初始化的缓存空间set集合
        Set<String> cacheNames =  new HashSet<>();
        cacheNames.add("my-redis-cache1");
        cacheNames.add("my-redis-cache2");

        // 对每个缓存空间应用不同的配置
        Map<String, RedisCacheConfiguration> configMap = new HashMap<>();
        configMap.put("my-redis-cache1", config);
        configMap.put("my-redis-cache2", config.entryTtl(Duration.ofSeconds(120)));

        // 使用自定义的缓存配置初始化一个cacheManager
        RedisCacheManager cacheManager = RedisCacheManager.builder(factory)
                .initialCacheNames(cacheNames)  // 注意这两句的调用顺序，一定要先调用该方法设置初始化的缓存名，再初始化相关的配置
                .withInitialCacheConfigurations(configMap)
                .build();
        return cacheManager;
    }
}
```

## 4.3 Redis 数据结构简介
Redis 可以存储键与5种不同数据结构类型之间的映射，这5种数据结构类型分别为String（字符串）、List（列表）、Set（集合）、Hash（散列）和 Zset（有序集合）。

|结构|结构存储的值范围|
|---|---|
|String|可以是字符串、整数或者浮点数|
|List|一个链表，链表上的每个节点都包含了一个字符串|
|Set|包含字符串的无序收集器(unorderedcollection)，并且被包含的每个字符串都是独一无二的、各不相同|
|Hash|包含键值对的无序散列表|
|Zset|字符串成员(member)与浮点数分值(score)之间的有序映射，元素的排列顺序由分值的大小决定|


## 4.4 StringRedisTemplate 与 RedisTemplate
StringRedisTemplate 与 RedisTemplate 区别
- StringRedisTemplate 继承 RedisTemplate
- RedisTemplate 是一个泛型类，而 StringRedisTemplate 不是
- StringRedisTemplate 只能对 key=String，value=String 的键值操作。
- 他们各自序列化的方式不同，但是都会得到一个字节数组。StringRedisTemplate 使用的是 StringRedisSerializer 类；RedisTemplate 使用的是 JdkSerializationRedisSerializer 类。反序列化，则是一个得到 String，一个得到 Object
- 两者的数据是不共通的，StringRedisTemplate 只能管理 StringRedisTemplate 里面的数据，RedisTemplate 只能管理 RedisTemplate中 的数据。

## 4.5 代码实例

RedisController（路径 src/main/java/com/fishpro/redis/controller/RedisController（路径.java）

```java
package com.fishpro.redis.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.data.redis.RedisProperties;
import org.springframework.data.redis.connection.RedisZSetCommands;
import org.springframework.data.redis.core.*;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;
import java.util.function.Consumer;

@RestController
public class RedisController {

    @Autowired
    RedisTemplate redisTemplate;

    @Autowired
    StringRedisTemplate stringRedisTemplate;

    public static final String CACHE_PREFIX = "FP:";

    @RequestMapping("/redis")
    public String testRedis(){

        System.out.println("字符串和散列Value和Hash-------------------------------------------------------");
        //set字符串
        redisTemplate.opsForValue().set("string_key", "string value");
        System.out.println("-------------set字符串-------------------: " + redisTemplate.opsForValue().get("string_key"));

        //注意这里使用了JDK的序列化器，所以redis保存时不是整数，不能运算
        redisTemplate.opsForValue().set("int_key", "1");
        System.out.println("-------------set int_key-------------------: " + redisTemplate.opsForValue().get("int_key"));

        stringRedisTemplate.opsForValue().set("int", "1");
        System.out.println("-------------stringRedisTemplate set  int-------------------: " + redisTemplate.opsForValue().get("int"));

        //使用运算
        stringRedisTemplate.opsForValue().increment("int", 1);
        System.out.println("-------------使用运算+1-------------------: " + redisTemplate.opsForValue().get("int"));
 
        //定义一个hashmap散列
        Map<String, String> hash = new HashMap<String, String>();
        hash.put("field1", "value1");
        hash.put("field2", "value2");
        //存入一个散列数据类型
        stringRedisTemplate.opsForHash().putAll("hash", hash);
        System.out.println("-------------存入一个散列数据类型-------------------: ");
        System.out.println("-------------map 遍历-------------------: ");
        redisTemplate.opsForHash().entries("hash").forEach((k, v) -> {
            System.out.println(k + ": " + v);
        });
        System.out.println("-------------map->set 遍历-------------------: ");
        redisTemplate.opsForHash().keys("hash").forEach(key -> {
            System.out.println(key + ": " + redisTemplate.opsForHash().get("hash", key));
        });
        //新增一个字段
        stringRedisTemplate.opsForHash().put("hash", "field3", "value3");
        System.out.println("-------------新增一个字段 field3-------------------: ");
        redisTemplate.opsForHash().entries("hash").forEach((k, v) -> {
            System.out.println(k + ": " + v);
        });
        //绑定散列操作的key，这样可以连续对同一个散列数据进行操作
        BoundHashOperations hashOps = stringRedisTemplate.boundHashOps("hash");
        //删除两个字段
        hashOps.delete("field1", "field2");
        System.out.println("-------------删除两个字段 field1 field2-------------------: ");
        hashOps.entries().forEach((k, v) -> {
            System.out.println(k + ": " + v);
        });
        //新增一个字段
        hashOps.put("field5", "value5");
        System.out.println("-------------新增一个字段 field5-------------------: ");
        hashOps.entries().forEach((k, v) -> {
            System.out.println(k + ": " + v);
        });


        System.out.println("列表（链表）List-------------------------------------------------------");
        //list 链表
        //插入两个链表，注意它们在链表中的顺序
        //链表从左到右的顺序为v10,v8,v6,v4,v2
        stringRedisTemplate.opsForList().leftPushAll("list1", "v2", "v4", "v6", "v8", "v10");
        System.out.println("----------链表从左到右的顺序为v10,v8,v6,v4,v2----------------:");
        stringRedisTemplate.opsForList().range("list1", 0, stringRedisTemplate.opsForList().size("list1") - 1).forEach(s -> System.out.println(s));

        //从左到右顺序为v1,v2,v3,v4,v5,v6
        stringRedisTemplate.opsForList().rightPushAll("list2", "v1", "v2", "v3", "v4", "v5", "v6");
        System.out.println("----------从左到右顺序为v1,v2,v3,v4,v5,v6----------------:");
        stringRedisTemplate.opsForList().range("list2", 0, stringRedisTemplate.opsForList().size("list2") - 1).forEach(s -> System.out.println(s));
        // 绑定list2链表操作
        BoundListOperations listOps = stringRedisTemplate.boundListOps("list2");
        //从右边弹出一个成员
        String result1 = (String)listOps.rightPop();
        System.out.println("----------从右边弹出一个成员----------------: " + result1);
        listOps.range(0, listOps.size() - 1).forEach(s -> System.out.println(s));
        //获取定位元素,redis从0开始运算
        String result2 = (String)listOps.index(1);
        System.out.println("----------获取定位元素,redis从0开始运算----------------: " + result2);
        listOps.range(0, listOps.size() - 1).forEach(s -> System.out.println(s));
        //从左边插入链表
        listOps.leftPush("v0");
        System.out.println("----------从左边插入链表----------------: " + "v0");
        listOps.range(0, listOps.size() - 1).forEach(s -> System.out.println(s));
        //链表长度
        Long size = listOps.size();
        //求链表下标区间成员，整个链表下标范围为0到size-1，这里不取最后一个元素
        List elements = listOps.range(0, size - 2);
        System.out.println("----------求链表下标区间成员0->size-2 ----------------: " + "v0");
        elements.forEach(s -> System.out.println(s));





        System.out.println("集合Set示例-------------------------------------------------------");
        //set
        //请注意，这里v1重复两次，因为集合不允许重复，所以只是插入5个成员到集合中
        stringRedisTemplate.opsForSet().add("set1", "v1", "v1", "v2", "v3", "v4", "v5");
        stringRedisTemplate.opsForSet().add("set2", "v2", "v4", "v6", "v8");
        //绑定set1集合操作
        BoundSetOperations setOps = stringRedisTemplate.boundSetOps("set1");
        //增加两个元素
        setOps.add("v6", "v7");
        //删除两个元素
        setOps.remove("v1", "v7");
        //返回所有元素
        Set set1 = setOps.members();
        // 成员数
        size = setOps.size();
        //求交集
        Set inner = setOps.intersect("set2");
        //求交集并且用新集合inter保存
        setOps.intersectAndStore("set2", "inner");
        //求差集
        Set diff = setOps.diff("set2");
        //求差集，并且用新集合diff保存
        setOps.diffAndStore("set2", "diff");
        //求并集
        Set union = setOps.union("set2");
        //求并集并且用新集合union保存
        setOps.unionAndStore("set2", "union");


        //ZSET
        Set<ZSetOperations.TypedTuple<String>> typedTupleSet = new HashSet<>();
        for (int i = 1; i <= 9; i++) {
            //分数
            double score = i * 0.1;
            //创建一个TypedTuple对象，存入值和分数
            ZSetOperations.TypedTuple<String> typedTuple = new DefaultTypedTuple<String>("value" + i, score);
            typedTupleSet.add(typedTuple);
        }
        //往有序集合插入元素
        stringRedisTemplate.opsForZSet().add("zset1", typedTupleSet);
        //绑定zset1有序集合操作
        BoundZSetOperations zsetOps = stringRedisTemplate.boundZSetOps("zset1");
        System.out.println("------------init----------------");
        zsetOps.rangeWithScores(0, zsetOps.size() - 1).forEach(new Consumer() {

            @Override
            public void accept(Object t) {
                ZSetOperations.TypedTuple<String> s = (ZSetOperations.TypedTuple)t;
                System.out.println(s.getValue() + " : " + s.getScore());
            }

        });
        //增加一个元素
        zsetOps.add("value10", 0.26);
        System.out.println("------------增加一个元素 value10----------------");
        zsetOps.rangeWithScores(0, zsetOps.size() - 1).forEach(new Consumer() {

            @Override
            public void accept(Object t) {
                ZSetOperations.TypedTuple<String> s = (ZSetOperations.TypedTuple)t;
                System.out.println(s.getValue() + " : " + s.getScore());
            }

        });
        // 获得range 1---6
        Set<String> setRange = zsetOps.range(1, 6);
        System.out.println("------------获得range 1---6---------------");
        Iterator itor = setRange.iterator();
        if (itor.hasNext()) {
            String s = (String)itor.next();
            System.out.println(s);
        }
        //按分数排序获得有序集合
        Set<String> setScore = zsetOps.rangeByScore(0.2, 0.6);
        System.out.println("------------按分数排序获得有序集合 (0.2, 0.6)---------------");
        Iterator itor2 = setScore.iterator();
        if (itor.hasNext()) {
            String s = (String)itor.next();
            System.out.println(s);
        }
        //自定义范围
        RedisZSetCommands.Range range = new RedisZSetCommands.Range();
        range.gt("value3");//大于value3
        //        range.gte("value3");//大于等于value3
        //        range.lt("value8");//小于value8
        range.lte("value8");//小于等于value8
        //按值排序，请注意这个排序是按字符串排序
        Set<String> setLex = zsetOps.rangeByLex(range);
        System.out.println("------------自定义范围 (value3, value8)---------------");
        Iterator itor3 = setLex.iterator();
        if (itor.hasNext()) {
            String s = (String)itor.next();
            System.out.println(s);
        }

        //删除元素
        zsetOps.remove("value9", "value2");
        System.out.println("------------删除元素 value9, value2---------------");
        zsetOps.rangeWithScores(0, zsetOps.size() - 1).forEach(new Consumer() {

            @Override
            public void accept(Object t) {
                ZSetOperations.TypedTuple<String> s = (ZSetOperations.TypedTuple)t;
                System.out.println(s.getValue() + " : " + s.getScore());
            }

        });

        //求分数
        Double score = zsetOps.score("value8");
        System.out.println("-----------求分数 value8---------------：" + score);
        //在下标区间下，按分数排序，同时返回value和score
        Set<ZSetOperations.TypedTuple<String>> rangeSet = zsetOps.rangeWithScores(1, 6);
        System.out.println("-----------在下标区间下，按分数排序 (1, 6)---------------");
        rangeSet.forEach(new Consumer() {

            @Override
            public void accept(Object t) {
                ZSetOperations.TypedTuple<String> s = (ZSetOperations.TypedTuple)t;
                System.out.println(s.getValue() + " : " + s.getScore());
            }

        });
        //在分数区间下，按分数排序，同时返回value和score
        Set<ZSetOperations.TypedTuple<String>> scoreSet = zsetOps.rangeByScoreWithScores(0.1, 0.6);
        System.out.println("-----------在分数区间下，按分数排序 (0.1, 0.6)---------------");
        scoreSet.forEach(new Consumer() {

            @Override
            public void accept(Object t) {
                ZSetOperations.TypedTuple<String> s = (ZSetOperations.TypedTuple)t;
                System.out.println(s.getValue() + " : " + s.getScore());
            }

        });
        //按从大到小排序
        Set<String> reverseSet = zsetOps.reverseRange(0, zsetOps.size() - 1);
        System.out.println("-----------按从大到小排序---------------");
        reverseSet.forEach(s -> System.out.println(s));

        System.out.println("redis开启事务--------------------------");
        redisTemplate.opsForValue().set("key1", "value1");
        List list = (List)redisTemplate.execute((RedisOperations operations) -> {
            //设置要监控的Key
            operations.watch("key1");
            //开启事务。在exec命令执行前，全部都只是进入队列
            operations.multi();
            operations.opsForValue().set("key2", "value2");
            //获取值为null，因为redis只是把命令放入队列
            Object value2 = operations.opsForValue().get("key2");
            System.out.println("命令在队列中，所以key2为null【" + value2 + "】");
            operations.opsForValue().set("key3", "value3");
            System.out.println("命令在队列中，所以key3为null【" + value2 + "】");
            //执行exec命令，将先判断key1是否在监控后被修改过，如果是则不执行事务，否则就执行事务
            return operations.exec();
        });


        System.out.println("redis流水线--------------------------");
        Long start = System.currentTimeMillis();
        List list = redisTemplate.executePipelined((RedisOperations operations) -> {
            for (int i = 1; i <= 100000; i++) {
                operations.opsForValue().set("pipeline_" + i, "value" + i);
                String value = (String)operations.opsForValue().get("pipeline_" + i);
                if (i == 100000) {
                    System.out.println("命令在队列中，所以值为null【" + value + "】");
                }
            }
            return null;
        });
        Long end = System.currentTimeMillis();
        System.out.println("耗时： " + (end - start) + "毫秒");

        return "";
    }
}

```

## 4.6 运行示例

右键 RedisApplication 选择 Run RedisApplication 在浏览器中输入 http://localhost:8080/redis

![spring boot 2.x 使用 redisTemplate](https://www.cnblogs.com/images/cnblogs_com/fishpro/1453719/o_redis2.jpg)



[本项目源码下载](https://github.com/fishpro/spring-boot-study/tree/master/spring-boot-study-redis)

----
参考

https://www.cnblogs.com/maria-ld/p/10010219.html
https://blog.csdn.net/Sadlay/article/details/83821629