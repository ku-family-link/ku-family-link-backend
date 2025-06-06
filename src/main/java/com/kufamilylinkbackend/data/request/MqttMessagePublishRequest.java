package com.kufamilylinkbackend.data.request;

public record MqttMessagePublishRequest(
    String topic,
    String message
) {

}
