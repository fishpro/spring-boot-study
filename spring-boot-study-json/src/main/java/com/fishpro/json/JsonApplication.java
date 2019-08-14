package com.fishpro.json;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fishpro.json.dto.Address;
import com.fishpro.json.dto.User;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

@SpringBootApplication
public class JsonApplication {

    public static void main(String[] args) {
        SpringApplication.run(JsonApplication.class, args);
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

        //测试 Jackjson 反序列化
        String json="{\"userId\":2,\"username\":\"程序员\",\"addresses\":[{\"street\":\"江苏省南京市玄武大道1000号\",\"zipcode\":\"201001\",\"mobile\":\"1801989098\"},{\"street\":\"江苏省南京市玄武大道1001号\",\"zipcode\":\"201001\",\"mobile\":\"1811989098\"}],\"created\":1565709784274}";

        try {
            User user2 = mapper.readValue(json, User.class);
            System.out.println(user2);

        }catch (Exception e){
            e.printStackTrace();
        }

    }

}
