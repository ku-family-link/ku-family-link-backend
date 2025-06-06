package com.kufamilylinkbackend.infrastructure.mqtt;

import java.nio.charset.StandardCharsets;
import lombok.RequiredArgsConstructor;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class MqttPublisher {

  private final MqttClient mqttClient;

  public void publish(String topic, String message) throws MqttException {
      mqttClient.publish(topic, new MqttMessage(message.getBytes(StandardCharsets.UTF_8)));
  }

}
