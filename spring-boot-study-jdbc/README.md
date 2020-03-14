总是要用到数据库的嘛，曾经我一度以为，写代码，编程就是搞数据库增删改查，甚至你设计一个系统，大部分时候在为如何设计关系型数据库努力，究其原因，是因为关系型数据库是逻辑的主要呈现。

这个系列，主要是对 Spring Boot 的数据库操作做一些示例程序展示。包括 mybatis、jpa操作、不同数据库的链接方式、多数据源切换、分库分表、自动编号问题、数据库优化问题。

从本系列开始，都需要用到 mysql 数据库 和其他一些参考的数据库。请准备相关环节。
- mysql 5.6+
- jdk1.8+
- spring boot 2.1.6
- idea 2018.1


[本项目源码下载](https://github.com/fishpro/spring-boot-study/tree/master/spring-boot-study-jdbc)

# 1 准备数据库
mysql 5.6+ 数据库

字段 | 类型 | 主键 | 说明
---|--- | ---|---
id|int|是|自动编号
user_name|varchar(100)|否|用户名
password|varchar(255)|否|密码
last_login_time|date|否|最近登录时间
sex|tinyint|否|性别 0男 1女 2其他

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


# 2 新建 Spring Boot 项目工程

1. File > New > Project，如下图选择 `Spring Initializr` 然后点击 【Next】下一步
2. 填写 `GroupId`（包名）、`Artifact`（项目名） 即可。点击 下一步
    groupId=com.fishpro   
    artifactId=jdbc
3. 选择依赖 `Spring Web Starter` 前面打钩，勾选SQL选项的 JDBC、MySql。
4. 项目名设置为 `spring-boot-study-jdbc`.


# 3 依赖引入 Pom.xml
```xml
    <dependencies>
		<dependency>
			<groupId>org.springframework.boot</groupId>
			<artifactId>spring-boot-starter-jdbc</artifactId>
		</dependency>
		<dependency>
			<groupId>org.springframework.boot</groupId>
			<artifactId>spring-boot-starter-web</artifactId>
		</dependency>
		<dependency>
			<groupId>mysql</groupId>
			<artifactId>mysql-connector-java</artifactId>
		</dependency>
		<dependency>
			<groupId>org.springframework.boot</groupId>
			<artifactId>spring-boot-starter-test</artifactId>
			<scope>test</scope>
		</dependency>
	</dependencies>
```

# 4 Jdbc 配置
application.yml 配置（.properties文件类似）
```bash
server:
  port: 8086

spring:
  datasource:
    driverClassName: com.mysql.jdbc.Driver
    url: jdbc:mysql://localhost:3306/demo_test?useSSL=false&useUnicode=true&characterEncoding=utf8
    username: root
    password: 123
```

# 5 编写示例代码
增加代码 controller/UserController.java
```java
@RestController
@RequestMapping("/api/user")
public class UserController {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    /**
     * 获取示例数据库 t_user 的全部信息 
     * @return 返回 json 数据
     * */
    @GetMapping("/users")
    public Object   getUsers(){
        List<Map<String,Object>> list=jdbcTemplate.queryForList("select * from t_user ");
        return  list;
    }

}
```

# 6 运行示例
运行 JdbcApplication 后在浏览器输入 http://localhost:8086/api/user/users 输出如下结果：

```json
[{
	"id": 1,
	"user_name": "json",
	"password": "123",
	"last_login_time": "2019-07-27T21:01:21.000+0000",
	"sex": 1
}, {
	"id": 2,
	"user_name": "jack jo",
	"password": "123",
	"last_login_time": "2019-07-24T21:01:37.000+0000",
	"sex": 1
}, {
	"id": 3,
	"user_name": "manistal",
	"password": "123",
	"last_login_time": "2019-07-24T21:01:37.000+0000",
	"sex": 1
}, {
	"id": 4,
	"user_name": "landengdeng",
	"password": "123",
	"last_login_time": "2019-07-24T21:01:37.000+0000",
	"sex": 1
}, {
	"id": 5,
	"user_name": "max",
	"password": "123",
	"last_login_time": "2019-07-24T21:01:37.000+0000",
	"sex": 1
}]
```

# 7 问题思考
1. JdbcTemplate 如何动态获取数据库
2. JdbcTemplate 如何实现 POJO 操作
3. JdbcTemplate 如何分表分库
4. JdbcTemplate 如何操作 Oracle 和 MSSQL Server


[本项目源码下载](https://github.com/fishpro/spring-boot-study/tree/master/spring-boot-study-jdbc)