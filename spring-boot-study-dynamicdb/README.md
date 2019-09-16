本章是一个完整的 Spring Boot 动态数据源切换示例，例如主数据库使用 lionsea 从数据库 lionsea_slave1、lionsea_slave2。只需要在对应的代码上使用 DataSource("slave1") 注解来实现数据库切换。

想要实现数据源动态切换，需要用到以下知识
1. spring boot 中自定义注解
2. spring boot 中的 aop 拦截
3. mybatis 的增删改查操作




[本项目源码 github 下载 ](https://github.com/fishpro/spring-boot-study/tree/master/spring-boot-study-dynamicdb)


# 1 新建 Spring Boot Maven 示例工程项目

注意：是用来 IDEA 开发工具
1. File > New > Project，如下图选择 `Spring Initializr` 然后点击 【Next】下一步
2. 填写 `GroupId`（包名）、`Artifact`（项目名） 即可。点击 下一步
    groupId=com.fishpro   
    artifactId=json
3. 选择依赖 `Spring Web Starter` 前面打钩。
4. 项目名设置为 `spring-boot-study-dynamicdb`.

# 2 依赖引入 Pom

# 3 动态数据源切换

## 3.1 新建多数据源注解 DataSource
文件路径（spring-boot-study/spring-boot-study-dynamicdb/src/main/java/com/fishpro/dynamicdb/datasource/annotation/DataSource.java）
```java

/**
 * 多数据源注解
 * 在方法名上加入 DataSource('名称')
 *
 * @author fishpro
 * */
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
public @interface DataSource {
    String value() default "";
}
```

## 3.2 新建一个多数据源上下文切换 DynamicContextHolder
```java
/**
 * 多数据源上下文
 * 
 */
public class DynamicContextHolder {
    @SuppressWarnings("unchecked")
    private static final ThreadLocal<Deque<String>> CONTEXT_HOLDER = new ThreadLocal() {
        @Override
        protected Object initialValue() {
            return new ArrayDeque();
        }
    };

    /**
     * 获得当前线程数据源
     *
     * @return 数据源名称
     */
    public static String getDataSource() {
        return CONTEXT_HOLDER.get().peek();
    }

    /**
     * 设置当前线程数据源
     *
     * @param dataSource 数据源名称
     */
    public static void setDataSource(String dataSource) {
        CONTEXT_HOLDER.get().push(dataSource);
    }

    /**
     * 清空当前线程数据源
     */
    public static void clearDataSource() {
        Deque<String> deque = CONTEXT_HOLDER.get();
        deque.poll();
        if (deque.isEmpty()) {
            CONTEXT_HOLDER.remove();
        }
    }

}
```

## 3.3 新建一个多数据源切面处理类
新建一个多数据源切面处理类指定Aop处理的注解点，和处理的事件（切换数据源），至此多数据源切换的主要工作就完成了。
```java

/**
 * 多数据源，切面处理类
 * 
 */
@Aspect
@Component
@Order(Ordered.HIGHEST_PRECEDENCE)
public class DataSourceAspect {
    protected Logger logger = LoggerFactory.getLogger(getClass());

    /**
     * 切面点 指定注解
     * */
    @Pointcut("@annotation(com.fishpro.dynamicdb.datasource.annotation.DataSource) " +
            "|| @within(com.fishpro.dynamicdb.datasource.annotation.DataSource)")
    public void dataSourcePointCut() {

    }

    /**
     * 拦截方法指定为 dataSourcePointCut
     * */
    @Around("dataSourcePointCut()")
    public Object around(ProceedingJoinPoint point) throws Throwable {
        MethodSignature signature = (MethodSignature) point.getSignature();
        Class targetClass = point.getTarget().getClass();
        Method method = signature.getMethod();

        DataSource targetDataSource = (DataSource)targetClass.getAnnotation(DataSource.class);
        DataSource methodDataSource = method.getAnnotation(DataSource.class);
        if(targetDataSource != null || methodDataSource != null){
            String value;
            if(methodDataSource != null){
                value = methodDataSource.value();
            }else {
                value = targetDataSource.value();
            }

            DynamicContextHolder.setDataSource(value);
            logger.debug("set datasource is {}", value);
        }

        try {
            return point.proceed();
        } finally {
            DynamicContextHolder.clearDataSource();
            logger.debug("clean datasource");
        }
    }
}
```


## 3.4 切换数据源
当Aop方法拦截到了带有注解 @DataSource 的方法的是，需要去执行指定的数据源，那么如何执行呢，这里我们使用阿里的 druid 链接池作为数据源连接池。这就要求我们需要对连接池进行一个可定制化的开发。程序安装Aop拦截到的信息去重新设定数据库路由，实现动态切换数据源的目标。


### 3.4.1 定义链接池的属性
在application.yml中的配置节点
```yml
spring:
    datasource:
        type: com.alibaba.druid.pool.DruidDataSource
        druid:
            driver-class-name: com.mysql.cj.jdbc.Driver
            url: jdbc:mysql://localhost:3306/ry?useUnicode=true&characterEncoding=UTF-8&serverTimezone=Asia/Shanghai
            username: root
            password: 123
            initial-size: 10
            max-active: 100
            min-idle: 10
            max-wait: 60000
            pool-prepared-statements: true
            max-pool-prepared-statement-per-connection-size: 20
            time-between-eviction-runs-millis: 60000
            min-evictable-idle-time-millis: 300000
            #Oracle需要打开注释
            #validation-query: SELECT 1 FROM DUAL
            test-while-idle: true
            test-on-borrow: false
            test-on-return: false
            stat-view-servlet:
                enabled: true
                url-pattern: /druid/*
                #login-username: admin
                #login-password: admin
            filter:
                stat:
                    log-slow-sql: true
                    slow-sql-millis: 1000
                    merge-sql: false
                wall:
                    config:
                        multi-statement-allow: true
```

```java

/**
 * 多数据源主数据源属性
 * 
 */
public class DataSourceProperties {
    private String driverClassName;
    private String url;
    private String username;
    private String password;

    /**
     * Druid默认参数
     */
    private int initialSize = 2;
    private int maxActive = 10;
    private int minIdle = -1;
    private long maxWait = 60 * 1000L;
    private long timeBetweenEvictionRunsMillis = 60 * 1000L;
    private long minEvictableIdleTimeMillis = 1000L * 60L * 30L;
    private long maxEvictableIdleTimeMillis = 1000L * 60L * 60L * 7;
    private String validationQuery = "select 1";
    private int validationQueryTimeout = -1;
    private boolean testOnBorrow = false;
    private boolean testOnReturn = false;
    private boolean testWhileIdle = true;
    private boolean poolPreparedStatements = false;
    private int maxOpenPreparedStatements = -1;
    private boolean sharePreparedStatements = false;
    private String filters = "stat,wall";

    /* 省略自动化生成部分 */
}

```
### 3.4.2 多数据源从数据源属性类 
在application.xml表示为,支持多数据库
```yml
dynamic:
  datasource:
  #slave1 slave2 数据源已测试
    slave1:
      driver-class-name: com.mysql.cj.jdbc.Driver
      url: jdbc:mysql://localhost:3306/lionsea_slave1?useUnicode=true&characterEncoding=UTF-8&serverTimezone=Asia/Shanghai
      username: root
      password: 123456
    slave2:
      driver-class-name: com.mysql.cj.jdbc.Driver
      url: jdbc:mysql://localhost:3306/lionsea_slave2?useUnicode=true&characterEncoding=UTF-8&serverTimezone=Asia/Shanghai
      username: root
      password: 123456
    slave3:
      driver-class-name: com.microsoft.sqlserver.jdbc.SQLServerDriver
      url: jdbc:sqlserver://localhost:1433;DatabaseName=renren_security
      username: sa
      password: 123456
    slave4:
      driver-class-name: org.postgresql.Driver
      url: jdbc:postgresql://localhost:5432/renren_security
      username: renren
      password: 123456
```

多数据源从数据源属性类，在application中表示为以 dynamic 为节点的配置
```java
/**
 * 多数据源属性 在application中表示为以 dynamic 为节点的配置
 *
 */
@ConfigurationProperties(prefix = "dynamic")
public class DynamicDataSourceProperties {
    private Map<String, DataSourceProperties> datasource = new LinkedHashMap<>();

    public Map<String, DataSourceProperties> getDatasource() {
        return datasource;
    }

    public void setDatasource(Map<String, DataSourceProperties> datasource) {
        this.datasource = datasource;
    }
}
```

### 3.4.3 建立动态数据源工厂类
建立动态数据源工厂类用于创建动态数据源连接池 Druid
```java
/**
 * DruidDataSource
 *
 */
public class DynamicDataSourceFactory {

    /**
     * 通过自定义建立 Druid的数据源
     * */
    public static DruidDataSource buildDruidDataSource(DataSourceProperties properties) {
        DruidDataSource druidDataSource = new DruidDataSource();
        druidDataSource.setDriverClassName(properties.getDriverClassName());
        druidDataSource.setUrl(properties.getUrl());
        druidDataSource.setUsername(properties.getUsername());
        druidDataSource.setPassword(properties.getPassword());

        druidDataSource.setInitialSize(properties.getInitialSize());
        druidDataSource.setMaxActive(properties.getMaxActive());
        druidDataSource.setMinIdle(properties.getMinIdle());
        druidDataSource.setMaxWait(properties.getMaxWait());
        druidDataSource.setTimeBetweenEvictionRunsMillis(properties.getTimeBetweenEvictionRunsMillis());
        druidDataSource.setMinEvictableIdleTimeMillis(properties.getMinEvictableIdleTimeMillis());
        druidDataSource.setMaxEvictableIdleTimeMillis(properties.getMaxEvictableIdleTimeMillis());
        druidDataSource.setValidationQuery(properties.getValidationQuery());
        druidDataSource.setValidationQueryTimeout(properties.getValidationQueryTimeout());
        druidDataSource.setTestOnBorrow(properties.isTestOnBorrow());
        druidDataSource.setTestOnReturn(properties.isTestOnReturn());
        druidDataSource.setPoolPreparedStatements(properties.isPoolPreparedStatements());
        druidDataSource.setMaxOpenPreparedStatements(properties.getMaxOpenPreparedStatements());
        druidDataSource.setSharePreparedStatements(properties.isSharePreparedStatements());

        try {
            druidDataSource.setFilters(properties.getFilters());
            druidDataSource.init();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return druidDataSource;
    }
}
```


### 3.4.3 配置多数据源配置类
```java
/**
 * 配置多数据源
 * 
 */
@Configuration
@EnableConfigurationProperties(DynamicDataSourceProperties.class)
public class DynamicDataSourceConfig {
    @Autowired
    private DynamicDataSourceProperties properties;

    @Bean
    @ConfigurationProperties(prefix = "spring.datasource.druid")
    public DataSourceProperties dataSourceProperties() {
        return new DataSourceProperties();
    }

    @Bean
    public DynamicDataSource dynamicDataSource(DataSourceProperties dataSourceProperties) {
        DynamicDataSource dynamicDataSource = new DynamicDataSource();
        dynamicDataSource.setTargetDataSources(getDynamicDataSource());

        //默认数据源
        DruidDataSource defaultDataSource = DynamicDataSourceFactory.buildDruidDataSource(dataSourceProperties);
        dynamicDataSource.setDefaultTargetDataSource(defaultDataSource);

        return dynamicDataSource;
    }

    private Map<Object, Object> getDynamicDataSource(){
        Map<String, DataSourceProperties> dataSourcePropertiesMap = properties.getDatasource();
        Map<Object, Object> targetDataSources = new HashMap<>(dataSourcePropertiesMap.size());
        dataSourcePropertiesMap.forEach((k, v) -> {
            DruidDataSource druidDataSource = DynamicDataSourceFactory.buildDruidDataSource(v);
            targetDataSources.put(k, druidDataSource);
        });

        return targetDataSources;
    }

}
```

## 3.5 测试多数据源
使用 Spring Boot 的测试类进行测试，建立一个基于 Mybatis 的 CRUD。

### 3.5.1 新建三个数据一个主库，2个从库

新建三个数据一个主库，2个从库,每个数据库都新建下面的表

```sql
DROP TABLE IF EXISTS `demo_test`;
CREATE TABLE `demo_test` (
  `id` bigint(20) NOT NULL,
  `name` varchar(255) NOT NULL,
  `status` tinyint(4) DEFAULT NULL,
  `is_deleted` tinyint(4) DEFAULT NULL,
  `create_time` datetime DEFAULT NULL,
  `create_user_id` bigint(20) DEFAULT NULL,
  `age` bigint(20) DEFAULT NULL,
  `content` text,
  `body` longtext,
  `title` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_name` (`name`) USING BTREE,
  KEY `idx_title` (`title`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
```


### 3.5.2 建立一个基于 Mybatis 的 CRUD
这里主要使用代码生成器生产一个增删改查（也可以手动）主要包括了 dao/domain/service/impl

/domain/DemoTestDO.java

```java
public class DemoTestDO implements Serializable {
	private static final long serialVersionUID = 1L;
	
	//
	private Long id;
	//
	private String name;
	//
	private Integer status;
	//
	private Integer isDeleted;
	//
	private Date createTime;
	//
	private Long createUserId;
	//
	private Long age;
	//
	private String content;
	//
	private String body;
	//
	private String title;
    //省略自动生成的部分

}

```

/dao/DemoTestDao.java

```java
@Mapper
public interface DemoTestDao {

	DemoTestDO get(Long id);
	
	List<DemoTestDO> list(Map<String, Object> map);
	
	int count(Map<String, Object> map);
	
	int save(DemoTestDO demoTest);
	
	int update(DemoTestDO demoTest);
	
	int remove(Long id);
	
	int batchRemove(Long[] ids);
}
```

具体见 [本项目源码 github 下载 ](https://github.com/fishpro/spring-boot-study/tree/master/spring-boot-study-dynamicdb)


### 3.5.3 编写测试代码
#### 动态代码测试方法类
```java
@Service
//@DataSource("slave1")
public class DynamicDataSourceTestService {
    @Autowired
    private DemoTestDao demoTestDao;

    @Transactional
    public void updateDemoTest(Long id){
        DemoTestDO user = new DemoTestDO();
        user.setId(id);
        user.setTitle("13500000000");
        demoTestDao.update(user);
    }

    @Transactional
    @DataSource("slave1")
    public void updateDemoTestBySlave1(Long id){
        DemoTestDO user = new DemoTestDO();
        user.setId(id);
        user.setTitle("13500000001");
        demoTestDao.update(user);
    }

    @DataSource("slave2")
    @Transactional
    public void updateDemoTestBySlave2(Long id){
        DemoTestDO user = new DemoTestDO();
        user.setId(id);
        user.setTitle("13500000002");
        demoTestDao.update(user);

        //测试事物
//        int i = 1/0;
    }
}
```

#### 动态测试代码

```java
@RunWith(SpringRunner.class)
@SpringBootTest
public class DynamicdbApplicationTests {

    @Autowired
    private DynamicDataSourceTestService dynamicDataSourceTestService;

    /**
     * 观察三个数据源中的数据是否正确
     * */
    @Test
    public void testDaynamicDataSource(){
        Long id = 1L;

        dynamicDataSourceTestService.updateDemoTest(id);
        dynamicDataSourceTestService.updateDemoTestBySlave1(id);
        dynamicDataSourceTestService.updateDemoTestBySlave2(id);
    }

}
```

## 3.6 测试
