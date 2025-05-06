package com.kufamilylinkbackend.notification.controller;

import com.kufamilylinkbackend.notification.AlertLog;
import com.kufamilylinkbackend.notification.AlertType;
import com.kufamilylinkbackend.notification.model.AlertMessage;
import com.kufamilylinkbackend.notification.repository.AlertLogRepository;
import com.kufamilylinkbackend.notification.service.AlertService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/notifications/test")
public class AlertTestController {

    private final AlertService alertService;
    private final AlertLogRepository alertLogRepository;

    @PostMapping("/{fitbitUserId}")
    public ResponseEntity<Void> testInactivity(@PathVariable String fitbitUserId) {
        String title = "무응답 또는 무활동 감지";
        String content = "테스트용 비상상황 알림입니다.";

        alertService.sendAlert(
                fitbitUserId,
                AlertMessage.builder()
                        .fitbitUserId(fitbitUserId)
                        .title(title)
                        .content(content)
                        .type("INACTIVITY_EMERGENCY") // 혹은 테스트니까 "TEST"도 가능
                        .createdAt(LocalDateTime.now())
                        .build()
        );

        alertLogRepository.save(AlertLog.builder()
                .fitbitUserId(fitbitUserId)
                .title(title)
                .content(content)
                .type(AlertType.valueOf("INACTIVITY_EMERGENCY"))
                .createdAt(LocalDateTime.now())
                .build()
        );

        return ResponseEntity.ok().build();
    }
}
