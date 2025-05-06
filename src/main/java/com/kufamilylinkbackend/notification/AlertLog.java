package com.kufamilylinkbackend.notification;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AlertLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String fitbitUserId;   // 알림 대상자

    private String type;           // 예: HEART_RATE, INACTIVITY
    private String title;          // 예: 이상 징후 감지
    private String content;        // 예: 심박수가 130을 초과했습니다.

    private LocalDateTime createdAt;

    @PrePersist
    public void prePersist() {
        this.createdAt = LocalDateTime.now();
    }
}
