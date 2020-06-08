package com.fishpro.kafka.listener;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class CustomerListener {

    @KafkaListener(topics="testTopic")
    public void onMessage(String message){
        System.out.println(message);
    }
}