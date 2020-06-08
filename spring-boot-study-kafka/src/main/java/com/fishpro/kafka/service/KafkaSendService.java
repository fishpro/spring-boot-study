package com.fishpro.kafka.service;

import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.util.concurrent.ListenableFutureCallback;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

@Service
public class KafkaSendService {

    @Autowired
    private KafkaTemplate<String,Object> kafkaTemplate;

    /**
     * 异步示例
     * */
    public void sendAnsyc(final String topic,final String message){
        ListenableFuture<SendResult<String, Object>> future = kafkaTemplate.send(topic,message);
        future.addCallback(new ListenableFutureCallback<SendResult<String, Object>>() {
            @Override
            public void onSuccess(SendResult<String, Object> result) {
                System.out.println("发送消息成功：" + result);
            }

            @Override
            public void onFailure(Throwable ex) {
                System.out.println("发送消息失败："+ ex.getMessage());
            }
        });
    }

    /**
     * 同步示例
     * */
    public void sendSync(final String topic,final String message){
        ProducerRecord<String, Object> producerRecord = new ProducerRecord<>(topic, message);
        try {
            kafkaTemplate.send(producerRecord).get(10, TimeUnit.SECONDS);
            System.out.println("发送成功");
        }
        catch (ExecutionException e) {
            System.out.println("发送消息失败："+ e.getMessage());
        }
        catch (TimeoutException | InterruptedException e) {
            System.out.println("发送消息失败："+ e.getMessage());
        }
    }
}
