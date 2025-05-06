package com.kufamilylinkbackend.notification.service;

import com.kufamilylinkbackend.notification.model.AlertMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@Service
public class AlertService {

    // 사용자별 emitter 저장소 (fitbitUserId 기준)
    private final Map<String, SseEmitter> emitters = new ConcurrentHashMap<>();

    // 구독 연결
    public SseEmitter subscribe(String fitbitUserId) {
        SseEmitter emitter = new SseEmitter(60 * 60 * 1000L); // 1시간 유지
        emitters.put(fitbitUserId, emitter);

        log.info("SSE 연결됨: {}", fitbitUserId);

        emitter.onCompletion(() -> emitters.remove(fitbitUserId));
        emitter.onTimeout(() -> emitters.remove(fitbitUserId));
        emitter.onError((e) -> emitters.remove(fitbitUserId));

        return emitter;
    }

    // 알림 전송
    public void sendAlert(String fitbitUserId, AlertMessage alertMessage) {
        SseEmitter emitter = emitters.get(fitbitUserId);
        if (emitter != null) {
            try {
                emitter.send(SseEmitter.event()
                        .name("health-alert")
                        .data(alertMessage));
                log.info("알림 전송 완료: {}", fitbitUserId);
            } catch (IOException e) {
                emitters.remove(fitbitUserId);
                log.error("알림 전송 실패: {}", fitbitUserId);
            }
        }
    }
}
