package com.kufamilylinkbackend.infrastructure.mqtt;

import org.eclipse.paho.client.mqttv3.MqttAsyncClient;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
public class MqttConfiguration {

  @Value("${mqtt.broker-url}")
  private String BROKER_URL;
  private static final String CLIENT_ID = MqttAsyncClient.generateClientId();

  @Value("${mqtt.topic}")
  private String TOPIC;

  @Bean
  public MqttConnectOptions mqttConnectOptions() {
    MqttConnectOptions options = new MqttConnectOptions();
    options.setServerURIs(new String[]{BROKER_URL});
    options.setCleanSession(true);
    return options;
  }

  @Bean
  public MqttClient mqttClient() {
    try {
      MqttClient mqttClient = new MqttClient(BROKER_URL, CLIENT_ID, new MemoryPersistence());

      mqttClient.connect(mqttConnectOptions());
      mqttClient.subscribe(TOPIC);
      return mqttClient;
    } catch (MqttException e) {
      throw new RuntimeException(e);
    }
  }


}
