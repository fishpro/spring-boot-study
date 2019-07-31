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
