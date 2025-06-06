package com.kufamilylinkbackend.presentation.controller;

import com.kufamilylinkbackend.data.request.MqttMessagePublishRequest;
import com.kufamilylinkbackend.infrastructure.mqtt.MqttPublisher;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/mqtt")
@RequiredArgsConstructor
public class MQTTController {

  private final MqttPublisher mqttPublisher;

  @PostMapping("/message")
  public ResponseEntity<Map<String, Boolean>> mqttMessagePublish(
      @RequestBody MqttMessagePublishRequest request) {
    try {
      mqttPublisher.publish(request.topic(), request.message());
    } catch (MqttException e) {
      e.printStackTrace();
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
          .body(Map.of("is_success", false));
    }
    return ResponseEntity.ok(Map.of("is_success", true));
  }
}
