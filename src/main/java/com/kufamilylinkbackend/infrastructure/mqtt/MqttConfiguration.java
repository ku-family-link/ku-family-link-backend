package com.kufamilylinkbackend.infrastructure.mqtt;

import javax.net.ssl.SSLSocketFactory;
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
  @Value("${mqtt.ca-path}")
  private String caCrtFilePath;
  @Value("${mqtt.cert-path}")
  private String crtFilePath;
  @Value("${mqtt.key-path}")
  private String keyFilePath;

  @Bean
  public MqttConnectOptions mqttConnectOptions() throws Exception {
    MqttConnectOptions options = new MqttConnectOptions();

    // TLS 인증 경로 설정
    SSLSocketFactory socketFactory = getSocketFactory(
        caCrtFilePath,
        crtFilePath,
        keyFilePath
    );

    options.setSocketFactory(socketFactory);
    options.setServerURIs(new String[]{"ssl://" + BROKER_URL + ":8883"});
    options.setCleanSession(true);
    return options;
  }

  @Bean
  public MqttClient mqttClient(MqttConnectOptions options) {
    try {
      MqttClient client = new MqttClient("ssl://" + BROKER_URL + ":8883", CLIENT_ID, new MemoryPersistence());
      client.connect(options);
      client.subscribe(TOPIC);
      return client;
    } catch (MqttException e) {
      throw new RuntimeException("MQTT 연결 실패", e);
    }
  }

  // PEM 인증서 → Java SSL SocketFactory 변환
  private SSLSocketFactory getSocketFactory(String caCrtFile, String crtFile, String keyFile) throws Exception {
    return AwsIotSslUtil.getSocketFactory(caCrtFile, crtFile, keyFile); // 아래에 구현 제공
  }

}
