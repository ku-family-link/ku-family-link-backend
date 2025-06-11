package com.kufamilylinkbackend.notification.service;

import com.kufamilylinkbackend.data.response.AlertLogResponse;
import com.kufamilylinkbackend.notification.repository.AlertLogRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AlertQueryService {
    private final AlertLogRepository alertLogRepository;

    @Transactional(readOnly = true)
    public List<AlertLogResponse> getAlerts(String fitbitUserId) {
        return alertLogRepository
            .findAllByFitbitUserIdOrderByCreatedAtDesc(fitbitUserId)
            .stream()
            .map(AlertLogResponse::from)
            .toList();
    }
}
