package com.fishpro.mqtt.config;

import org.springframework.integration.annotation.MessagingGateway;
import org.springframework.integration.mqtt.support.MqttHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Component;

/**
 * 集成发送消息类
 * @MessagingGateway(defaultRequestChannel = "mqttOutboundChannel") 指定输出频道
 * */
@Component
@MessagingGateway(defaultRequestChannel = "mqttOutboundChannel")
public interface CustomMqttGateway {
    void sendToMqtt(String data);
    void sendToMqtt(String payload,@Header(MqttHeaders.TOPIC) String topic);
    void sendToMqtt(@Header(MqttHeaders.TOPIC) String topic, @Header(MqttHeaders.QOS) int qos, String payload);
}