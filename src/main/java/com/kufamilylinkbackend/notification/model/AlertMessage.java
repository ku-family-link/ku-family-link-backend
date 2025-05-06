package com.kufamilylinkbackend.notification.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AlertMessage {
    private String title;    // 예: "이상징후 감지"
    private String content;  // 예: "심박수가 비정상적으로 높습니다."
}
