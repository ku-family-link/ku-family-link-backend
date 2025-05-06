package com.kufamilylinkbackend.notification.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AlertMessage {
    private String fitbitUserId;   // 어떤 사용자에게 발생한 알림인지
    private String title;          // 예: "이상징후 감지"
    private String content;        // 예: "심박수가 비정상적으로 높습니다.\n수면 시간이 부족합니다."
    private String type;           // 예: "WARNING", "INFO", "CRITICAL" 등
    private LocalDateTime createdAt; // 알림 발생 시각
}
