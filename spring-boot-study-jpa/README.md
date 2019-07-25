JPA 是 Spring Boot 官方推荐的数据库访问组件，其充分体现了面向对象编程思想，有点像 asp.net 的 EFCore。JPA 也是众多 ORM 的抽象。


从本系列开始，都需要用到 mysql 数据库 和其他一些参考的数据库。请准备相关环节。本章需要以下环境支撑：

- mysql 5.6+
- jdk1.8+
- spring boot 2.1.6
- idea 2018.1
  

[本项目源码下载](https://github.com/fishpro/spring-boot-study/tree/master/spring-boot-study-jpa)

# 1 数据准备
数据库教程系列都是使用相同的数据，如在 [Spring Boot JDBC 使用教程]()使用的一样

|字段|类型|主键|说明|
|--|--|--|---|
|id|int|是|自动编号|
|user_name|varchar(100)|否|用户名|
|password|varchar(255)|否|密码|
|last_login_time|date|否|最近登录时间|
|sex|tinyint|否|性别 0男 1女 2其他|
```sql

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for t_user
-- ----------------------------
DROP TABLE IF EXISTS `t_user`;
CREATE TABLE `t_user` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `user_name` varchar(255) DEFAULT NULL,
  `password` varchar(255) DEFAULT NULL,
  `last_login_time` datetime DEFAULT NULL,
  `sex` tinyint(4) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=armscii8;

-- ----------------------------
-- Records of t_user
-- ----------------------------
BEGIN;
INSERT INTO `t_user` VALUES (1, 'json', '123', '2019-07-27 16:01:21', 1);
INSERT INTO `t_user` VALUES (2, 'jack jo', '123', '2019-07-24 16:01:37', 1);
INSERT INTO `t_user` VALUES (3, 'manistal', '123', '2019-07-24 16:01:37', 1);
INSERT INTO `t_user` VALUES (4, 'landengdeng', '123', '2019-07-24 16:01:37', 1);
INSERT INTO `t_user` VALUES (5, 'max', '123', '2019-07-24 16:01:37', 1);
COMMIT;

SET FOREIGN_KEY_CHECKS = 1;
```

# 2 新建 Spring Boot 工程项

1. File > New > Project，如下图选择 `Spring Initializr` 然后点击 【Next】下一步
2. 填写 `GroupId`（包名）、`Artifact`（项目名） 即可。点击 下一步
    groupId=com.fishpro   
    artifactId=jpa
3. 选择依赖 `Spring Web Starter` 前面打钩，勾选SQL选项的 Spring Data JPA , MySQL
4. 项目名设置为 `spring-boot-study-jpa`.


# 3 依赖引入 Pom.xml 配置
如果您已经勾选了 勾选SQL选项的 Spring Data JPA , MySQL，那么无须手动增加依赖。

```xml
<dependencies>
		<dependency>
			<groupId>org.springframework.boot</groupId>
			<artifactId>spring-boot-starter-data-jpa</artifactId>
		</dependency>
		<dependency>
			<groupId>org.springframework.boot</groupId>
			<artifactId>spring-boot-starter-webflux</artifactId>
		</dependency>

		<dependency>
			<groupId>mysql</groupId>
			<artifactId>mysql-connector-java</artifactId>
			<scope>runtime</scope>
		</dependency>
		<dependency>
			<groupId>org.springframework.boot</groupId>
			<artifactId>spring-boot-starter-test</artifactId>
			<scope>test</scope>
		</dependency>
		<dependency>
			<groupId>io.projectreactor</groupId>
			<artifactId>reactor-test</artifactId>
			<scope>test</scope>
		</dependency>
	</dependencies>
```
# 4 JPA 的工程配置 application.yml
```yml
server:
  port: 8086
spring:
  #通用的数据源配置
  datasource:
    driverClassName: com.mysql.jdbc.Driver
    url: jdbc:mysql://localhost:3306/demo_test?useSSL=false&useUnicode=true&characterEncoding=utf8
    username: root
    password: 123
  jpa:
    #这个参数是在建表的时候，将默认的存储引擎切换为 InnoDB 用的
    database-platform: org.hibernate.dialect.MySQL5InnoDBDialect
    #配置在日志中打印出执行的 SQL 语句信息。
    show-sql: true
    hibernate:
      #配置指明在程序启动的时候要删除并且创建实体类对应的表
      ddl-auto: create
```
- spring.jpa.database-platform 设置为 `org.hibernate.dialect.MySQL5InnoDBDialect` 这个参数是在建表的时候，将默认的存储引擎切换为 InnoDB 用的
- spring.jpa.show-sql 设置为true 配置在日志中打印出执行的 SQL 语句信息。
- spring.jpa.hibernate.ddl-auto 设置为 create 配置指明在程序启动的时候要删除并且创建实体类对应的表


# 5 编写示例代码
本示例代码结构跟 [Spring Boot Mybatis 使用教程](https://www.cnblogs.com/fishpro/p/spring-boot-study-mybatis.html) 是一样的。

代码方便不同的是，在命名方便，JPA 把 Dao 改成了 Repository，继承了 CrudRepository。



**本示例包括新增的页面**

1. src/main/java/com/fishpro/jpa/controller/UserController.java 控制层 rest api
2. src/main/java/com/fishpro/jpa/domain/UserDO.java 实体对象
3. src/main/java/com/fishpro/jpa/dao/UserRepository.java  Repository 数据库访问层
4. src/test/java/fishpro/com/UserRepositoryTest.java 测试类

## 5.1 新建实体对象 UserDao.java
建立基于 POJO 的实体对象，需要注意的是 JPA 与 Mybatis 是有区别的
1. 实体类需要使用 @Entity 注解标注
2. 需要对实体类的属性进行标注，使用 @Id 标注主键
3. 使用 @Column 标注非主键
```java

/**
 * 用户实体类
 * */
@Entity
@Table(name="t_user")
public class UserDO {
    @Id
    private Integer id;
    @Column(name="user_name",length = 200)
    private String userName;
    @Column(name="password",length = 200)
    private String password;
    @Column(name="sex")
    private Integer sex;
    @Column(name="last_login_time")
    private Date lastLoginTime;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
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

    public Date getLastLoginTime() {
        return lastLoginTime;
    }

    public void setLastLoginTime(Date lastLoginTime) {
        this.lastLoginTime = lastLoginTime;
    }
}
```

## 5.2 新建仓库接口类 UserRepository
仓库接口类 `UserRepository` 就是我们常用的 Dao 接口，需要注意的是 JPA 的仓储接口需要
1. 使用 `@Repository` 注解
2. 继承 `JPARepository`
3. `UserRepository` 不需要编写任何代码，就可实现增删改查

```java
@Repository
public interface UserRepository extends JPARepository<UserDO,Integer> {

}
```


# 6 编写 UserRepository 的测试用例

关于测试可参看 [Spring Boot RestApi 测试教程 Mock 的使用 ](https://www.cnblogs.com/fishpro/p/spring-boot-study-resttest-mock.html)

在 `src/test/java/com/fishpro/jpa/` 下新增 `UserRepositoryTest.java` 使用 `@RunWith(SpringRunner.class)` 和 `@SpringBootTest` 注解标注类。

```java
@RunWith(SpringRunner.class)
@SpringBootTest
public class UserRepositoryTest{

}
```


## 6.1 新增用户数据
初始化一个对象 UserDO 测试Insert过程
```java
/**
     * 初始化一个对象 UserDO 测试Insert过程
     * */
    @Before
    public void before(){
        UserDO userDO=new UserDO();
        userDO.setId(1);
        userDO.setUserName("fishpro");
        userDO.setSex(1);
        userDO.setLastLoginTime(new Date());
        userDO.setPassword("passWord");
        userRepository.save(userDO);
    }
```
## 6.2 查询单个数据
```java
@Test
    public void testFind(){
        Optional<UserDO> optionalUserDO=userRepository.findById(1);
        if(optionalUserDO.isPresent()){
            UserDO userDO=optionalUserDO.get();
            System.out.println("testFind user"+userDO.getUserName());
        }

    }

```
## 6.3 查询多个数据
```java
@Test
    public void testFindAll(){
        List<UserDO> list=userRepository.findAll();
        for (UserDO user:list
             ) {
            System.out.println("user_name:"+user.getUserName());
        }
    }
```
## 6.4 更新数据
```java
@Test
    public void testUpdate(){
        Optional<UserDO> optionalUserDO=userRepository.findById(1);
        if(optionalUserDO.isPresent()){
            UserDO userDO=optionalUserDO.get();
            userDO.setUserName("fishpro001");
            userRepository.save(userDO);
            System.out.println("testFind user"+userDO.getUserName());
        }

    }
```
## 6.5 删除数据
```java
@After
    public void after(){
        userRepository.deleteById(1);
        userRepository.deleteById(2);
        userRepository.deleteById(3);
    }
```


# 7 问题思考
## 7.1 联合主键设置
假设我们定义了用户角色表包括了 userId，roleId 两个都是主键。
1. 定义一个主键类
```java
public class UserRoleKey implements Serializable {
    private Integer userId;
    private Integer roleId;
}
```
2. 定义实体类
注意实体类上 是用来 `@IdClass` 注解来实现复合主键定义
```java
@Entity
@Table(name="t_user_role")
@IdClass(UserRoleKey.class) //注意这里是引入了 定义的符合主键类
public class UserRoleDO {
    @Id
    private Integer userId;
    @Id
    private Integer roleId;

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Integer getRoleId() {
        return roleId;
    }

    public void setRoleId(Integer roleId) {
        this.roleId = roleId;
    }
}
```
## 7.2 自定义查询


