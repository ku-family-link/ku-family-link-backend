package com.kufamilylinkbackend.notification.controller;

import com.kufamilylinkbackend.notification.service.AlertService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
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
}
