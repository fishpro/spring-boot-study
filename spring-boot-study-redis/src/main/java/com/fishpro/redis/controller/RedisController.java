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


//        System.out.println("redis流水线--------------------------");
//        Long start = System.currentTimeMillis();
//        List lineList = redisTemplate.executePipelined((RedisOperations operations) -> {
//            for (int i = 1; i <= 100000; i++) {
//                operations.opsForValue().set("pipeline_" + i, "value" + i);
//                String value = (String)operations.opsForValue().get("pipeline_" + i);
//                if (i == 100000) {
//                    System.out.println("命令在队列中，所以值为null【" + value + "】");
//                }
//            }
//            return null;
//        });
//        Long end = System.currentTimeMillis();
//        System.out.println("耗时： " + (end - start) + "毫秒");

        return "";
    }
}
