package com.kufamilylinkbackend.notification.service;

import com.kufamilylinkbackend.data.response.AlertLogResponse;
import com.kufamilylinkbackend.notification.repository.AlertLogRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AlertQueryService {
    private final AlertLogRepository alertLogRepository;

    @Transactional(readOnly = true)
    public List<AlertLogResponse> getAlerts(String fitbitUserId) {

        LocalDate today = LocalDate.now();  // 현재 날짜
        LocalDateTime startOfDay = today.atStartOfDay();
        LocalDateTime endOfDay = today.atTime(LocalTime.MAX);

        return alertLogRepository
                .findAllByFitbitUserIdAndCreatedAtBetweenOrderByCreatedAtDesc(
                        fitbitUserId, startOfDay, endOfDay)
                .stream()
                .map(AlertLogResponse::from)
                .toList();
    }
}
