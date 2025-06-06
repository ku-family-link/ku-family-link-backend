package com.kufamilylinkbackend.infrastructure.mqtt;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
@Slf4j
public class MqttReceiver implements MqttCallback {

  private final MqttClient mqttClient;

  @PostConstruct
  public void init() {
    mqttClient.setCallback(this);
  }

  @Override
  public void connectionLost(Throwable throwable) {
    System.out.println("연결이 끊어졌을 때 처리 로직");

  }

  @Override
  public void messageArrived(String s, MqttMessage mqttMessage) throws Exception {
    log.info("메세지 도착 : topic: {} / message : {}", s, mqttMessage);

  }

  @Override
  public void deliveryComplete(IMqttDeliveryToken iMqttDeliveryToken) {
    System.out.println("메시지 전송이 완료되었을 때 처리 로직");

  }
}
