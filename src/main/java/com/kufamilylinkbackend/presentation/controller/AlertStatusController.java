package com.kufamilylinkbackend.presentation.controller;

import com.kufamilylinkbackend.data.response.AlertLogResponse;
import com.kufamilylinkbackend.notification.service.AlertQueryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/notifications")
public class AlertStatusController {

    private final AlertQueryService alertQueryService;

    // 알림 목록 조회
    @GetMapping("/{fitbitUserId}")
    public ResponseEntity<java.util.List<AlertLogResponse>> getAlerts(@PathVariable String fitbitUserId) {
        return ResponseEntity.ok(alertQueryService.getAlerts(fitbitUserId));
    }
}
