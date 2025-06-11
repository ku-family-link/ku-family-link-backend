package com.kufamilylinkbackend.data.response;

import com.kufamilylinkbackend.notification.AlertLog;
import java.time.LocalDateTime;

public record AlertLogResponse(
    Long id,
    String fitbitUserId,
    String type,
    String title,
    String content,
    LocalDateTime createdAt
) {
    public static AlertLogResponse from(AlertLog log) {
        return new AlertLogResponse(
            log.getId(),
            log.getFitbitUserId(),
            log.getType().name(),
            log.getTitle(),
            log.getContent(),
            log.getCreatedAt()
        );
    }
}
