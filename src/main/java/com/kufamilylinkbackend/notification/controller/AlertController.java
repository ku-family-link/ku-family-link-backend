package com.kufamilylinkbackend.notification.controller;

import com.kufamilylinkbackend.notification.model.AlertMessage;
import com.kufamilylinkbackend.notification.service.AlertService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/notifications")
public class AlertController {

    private final AlertService alertService;

    // 사용자가 구독하기
    @GetMapping("/subscribe/{fitbitUserId}")
    public SseEmitter subscribe(@PathVariable String fitbitUserId) {

        return alertService.subscribe(fitbitUserId);
    }

    @PostMapping("/send-test")
    public ResponseEntity<Void> sendTestAlert(@RequestBody AlertMessage message) {
        alertService.sendAlert(message.getFitbitUserId(), message);
        return ResponseEntity.ok().build();
    }
}
