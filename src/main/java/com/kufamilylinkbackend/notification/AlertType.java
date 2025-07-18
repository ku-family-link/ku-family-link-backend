package com.kufamilylinkbackend.notification;

public enum AlertType {
    HEALTH_ANOMALY, // 이상 징후
    INFO,           // 일반 정보
    CRITICAL,       // 심각 경고
    WARNING,         // 경고 수준
    INACTIVITY_EMERGENCY,
    HEART_RATE_WARNING,
    COMPLETED,  // 미션 완료
    NORMAL      // 이상 징후 없음
}