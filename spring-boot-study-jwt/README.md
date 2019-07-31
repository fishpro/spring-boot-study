本章节讨论 jwt 在 spring boot 中的应用。意在快速入门 jwt。

- java jdk1.8
- maven 3.2+
- spring boot 2.0+

JSON Web Token（JWT） 他是一个用于 Web 身份验证的令牌。

# 1 JWT 概述
## 1.1 什么是JWT 
直观的理解 JWT 就是一串字符串，如下（来自于 [JWT.IO](https://jwt.io/)）：
```
eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIxMjM0NTY3ODkwIiwibmFtZSI6IkpvaG4gRG9lIiwiaWF0IjoxNTE2MjM5MDIyfQ.SflKxwRJSMeKKF2QT4fwpMeJf36POk6yJV_adQssw5c
```
细心的你可能会发现字符串是有3个独立的字符串使用` . `号组合而成，前两个字符串是 Base64 编码，最后一个字符串是一个加密后的字符串。

|序号|字符串|
|---|---|
|1|eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9|
|2|eyJzdWIiOiIxMjM0NTY3ODkwIiwibmFtZSI6IkpvaG4gRG9lIiwiaWF0IjoxNTE2MjM5MDIyfQ|
|3|SflKxwRJSMeKKF2QT4fwpMeJf36POk6yJV_adQssw5c|

**JWT 使用以上3个字符串组成一个字符串，以 `xxxxx.yyyyyy.zzzzz`  的形式传输给认证服务器**

`xxxxx.yyyyyy.zzzzz` 表示为 `Header.Payload.Signature ` 
Header=eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9
Payload=eyJzdWIiOiIxMjM0NTY3ODkwIiwibmFtZSI6IkpvaG4gRG9lIiwiaWF0IjoxNTE2MjM5MDIyfQ
Signature=SflKxwRJSMeKKF2QT4fwpMeJf36POk6yJV_adQssw5c

我们使用 [JWT Debuger ](https://jwt.io/) 把信息解码可以看出： 

### 1.1.1 Header

**Header 头信息，以 json 字符串实现，并压缩成 `Base64URL` 编码字符串传输**
```json
{
  "alg": "HS256",
  "typ": "JWT"
}
```
上面代码中，alg属性表示签名的算法（algorithm），默认是 HMAC SHA256（写成 HS256）；typ属性表示这个令牌（token）的类型（type），JWT 令牌统一写为JWT。
### 1.1.2 Payload

**Payload 负载，以 json 字符串实现，并压缩成 Base64编码字符串传输**
```json
{
  "sub": "1234567890",
  "name": "John Doe",
  "iat": 1516239022
}
```
官方对 json 内容节点给出了一些建议，我个人觉得意义不大，况且那个建议字段完全是看不出任何含义的缩写。比如 `iss、exp、sub、aud、nbf、iat、jti`。当然如果大家都能共同遵守，也不存在这些问题。
```
iss (issuer)：签发人
exp (expiration time)：过期时间
sub (subject)：主题
aud (audience)：受众
nbf (Not Before)：生效时间
iat (Issued At)：签发时间
jti (JWT ID)：编号
```
### 1.1.3 Signature


**VERIFY SIGNATURE 签名，在服务端指定一个秘钥，把签名两个 json 字符串使用下面方法加密而成的**
```
HMACSHA256(
  base64UrlEncode(header) + "." +
  base64UrlEncode(payload),
  only-save-in-server-secret)
```

**<font color=red>值得注意的是，使用JWT 推荐使用 HTTPS 加密协议</font>**

### 1.1.4 Base64URL
BASE64URL是一种在BASE64的基础上编码形成新的加密方式，为了编码能在网络中安全顺畅传输，需要对BASE64进行的编码，特别是互联网中。
 

## 1.2 JWT 是如何工作的

为了能够支持跨域，通常 JWT 的字符串 `xxxxx.yyyyyy.zzzzz` 通过 Http 的 Head 发送
```
Authorization: Bearer <token>
```
也可以放在 Post 数据体重。


# 2 Spring Boot JWT 示例
在 JWT 官方可以看到，maven: `io.jsonwebtoken / jjwt / 0.9.0 ` 这个库是支持最全的。

## 2.1 新建 Spring Boot Maven 示例工程项目

1. File > New > Project，如下图选择 `Spring Initializr` 然后点击 【Next】下一步
2. 填写 `GroupId`（包名）、`Artifact`（项目名） 即可。点击 下一步
    groupId=com.fishpro   
    artifactId=jwt
3. 选择依赖 `Spring Web Starter` 前面打钩。
4. 项目名设置为 `spring-boot-study-jwt`.

## 2.2 依赖引入 Pom.xml
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
    <artifactId>jwt</artifactId>
    <version>0.0.1-SNAPSHOT</version>
    <name>jwt</name>
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
            <groupId>com.auth0</groupId>
            <artifactId>java-jwt</artifactId>
            <version>3.4.1</version>
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

## 2.4 编写 JWT 生成与验证代码
本章节知识学习 JWT，不涉及其他知识点，故我们之间在 JwtApplication.java 中来编写代码

```java
package com.fishpro.jwt;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.*;

@SpringBootApplication
public class JwtApplication {

    public static void main(String[] args) {

        SpringApplication.run(JwtApplication.class, args);
        String token=createJWTToken();//获取生成的Token
        verifyJWTToken(token);//验证生成的Token
    }

    /**
     * 生成 JWT Token
     * */
    private static String createJWTToken() throws JWTCreationException {
        String secret="secret";//假设服务端秘钥
        Algorithm algorithm = Algorithm.HMAC256(secret);
        //jwt 头部信息
        Map<String,Object> map=new HashMap<>();
        map.put("alg","HS256");
        map.put("typ","JWT");

        Date nowDate = new Date();
        Date expireDate = AddDate(nowDate,2*60);//120 分钟过期

        String token= JWT.create()
                .withHeader(map)
                .withIssuer("SERVICE") //对应 paylaod iss 节点：签发人
                .withClaim("loginName","fishpro")
                .withSubject("this is a token demo")//对应 paylaod sub 节点：主题
                .withAudience("Client")//对应 paylaod aud 节点：受众
                .withIssuedAt(nowDate)//对应 paylaod iat 节点：生效时间
                .withExpiresAt(expireDate) //对应 paylaod  exp 签发人 节点：过期时间
                .sign(algorithm);
        return  token;
    }


    /**
     * 验证 token
     * */
    private static void verifyJWTToken(String token) throws JWTVerificationException {
        Algorithm algorithm=Algorithm.HMAC256("secret");
        JWTVerifier verifier = JWT.require(algorithm)
                .withIssuer("SERVICE")
                .build();

        DecodedJWT jwt =verifier.verify(token);
        String subject=jwt.getSubject();
        Map<String,Claim> claims=jwt.getClaims();
        Claim claim = claims.get("loginName");
        System.out.println("自定义 claim："+claim.asString());

        List<String> audience = jwt.getAudience();
        System.out.println("subject 值："+subject);
        System.out.println("audience 值："+audience.get(0));
    }

    /**
     * 时间加减法
     * */
    private static Date AddDate(Date date,Integer minute){
        if(null==date)
            date=new Date();
        Calendar cal=new GregorianCalendar();
        cal.setTime(date);
        cal.add(Calendar.MINUTE, minute);
        return cal.getTime();

    }

}

```

## 2.5 演示效果

右键 JwtApplication 选择 Run JwtApplication ，观察 console 窗口

![演示效果](https://www.cnblogs.com/images/cnblogs_com/fishpro/1453719/o_jwt1.png)