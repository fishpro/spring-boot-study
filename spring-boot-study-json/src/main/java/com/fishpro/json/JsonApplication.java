package com.fishpro.json;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fishpro.json.dto.Address;
import com.fishpro.json.dto.User;
import com.fishpro.json.util.Person;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.Serializable;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
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

        //测试 Fastjson 序列化
        System.out.println("测试 Fastjson 序列化");
        System.out.println(JSON.toJSONString(user));

        //测试 Fastjson 反序列化
        System.out.println("测试 Fastjson 反序列化");
        User user3 = JSON.parseObject(json,User.class);
        System.out.println(user3);

        System.out.println("测试 Fastjson person 注解");
        Person person=new Person(100,"dashen",new Date());
        System.out.println(JSON.toJSONString(person));

        Person person2=new Person(98,"dashen2",new Date());
        Person person3=new Person(88,"dashen3",new Date());
        List<Person> personList=new ArrayList<>();
        personList.add(person);
        personList.add(person2);
        personList.add(person3);
        System.out.println(JSON.toJSONString(personList, SerializerFeature.BeanToArray));

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

    }

}
