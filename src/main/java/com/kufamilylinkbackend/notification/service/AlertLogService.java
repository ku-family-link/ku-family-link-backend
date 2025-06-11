package com.kufamilylinkbackend.notification.service;

import com.kufamilylinkbackend.notification.AlertLog;
import com.kufamilylinkbackend.notification.AlertType;
import com.kufamilylinkbackend.notification.repository.AlertLogRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AlertLogService {

    private final AlertLogRepository alertLogRepository;

    @Transactional
    public void saveMissionCompletedAlert(String fitbitUserId) {
        AlertLog log = AlertLog.builder()
                .fitbitUserId(fitbitUserId)
                .type(AlertType.COMPLETED)
                .title("미션 완료 확인")
                .content("사용자가 오늘의 미션을 완료했습니다.")
                .createdAt(LocalDateTime.now())
                .build();

        alertLogRepository.save(log);
    }
}
