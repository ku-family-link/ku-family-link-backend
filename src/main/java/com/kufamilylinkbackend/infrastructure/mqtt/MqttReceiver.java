package com.kufamilylinkbackend.infrastructure.mqtt;

import com.kufamilylinkbackend.notification.AlertLog;
import com.kufamilylinkbackend.notification.AlertType;
import com.kufamilylinkbackend.notification.repository.AlertLogRepository;
import jakarta.annotation.PostConstruct;
import java.time.LocalDateTime;
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
  private final AlertLogRepository alertLogRepository;

  @PostConstruct
  public void init() {
    mqttClient.setCallback(this);
  }

  @Override
  public void connectionLost(Throwable throwable) {
    System.out.println("연결이 끊어짐");

  }

  @Override
  public void messageArrived(String s, MqttMessage mqttMessage) throws Exception {
    log.info("메세지 도착 : topic: {} / message : {}", s, mqttMessage);

    alertLogRepository.save(
        AlertLog.builder()
            .fitbitUserId(null)
            .type(AlertType.INFO)
            .title("MQTT 메시지 수신")
            .content(String.format("비상상황 발생! 피보호자가 위험 신호를 알렸습니다."))
            .fitbitUserId("CLC3TK")
            .createdAt(LocalDateTime.now())
            .build());

  }

  @Override
  public void deliveryComplete(IMqttDeliveryToken iMqttDeliveryToken) {
    System.out.println("메시지 전송이 완료됨");

  }
}
