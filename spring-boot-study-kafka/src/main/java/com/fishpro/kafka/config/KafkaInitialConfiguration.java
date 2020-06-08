package com.fishpro.kafka.config;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

//@Configuration
public class KafkaInitialConfiguration {
    @Bean
    public NewTopic initialTopic(){
        //创建TopicName为topic.quick.initial的Topic并设置分区数为8以及副本数为1
        return new NewTopic("sunday",1,(short)1);
    }
}