package com.kufamilylinkbackend.application.scheduler;

import com.kufamilylinkbackend.application.domain.HealthAnomalyDetector;
import com.kufamilylinkbackend.application.domain.HealthAnomalyDetector.AnomalyResult;
import com.kufamilylinkbackend.application.service.FitbitDataFetchService;
import com.kufamilylinkbackend.infrastructure.repository.FitbitUserRepository;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import com.kufamilylinkbackend.notification.AlertLog;
import com.kufamilylinkbackend.notification.AlertType;
import com.kufamilylinkbackend.notification.model.AlertMessage;
import com.kufamilylinkbackend.notification.repository.AlertLogRepository;
import com.kufamilylinkbackend.notification.service.AlertService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class HealthMonitorScheduler {

  private final FitbitDataFetchService fetchService;
  private final FitbitUserRepository userRepository;
  private final AlertService alertService;
  private final AlertLogRepository alertLogRepository;

  //TODO: 추후 DB에서 감지중인 userId 리스트를 가져오도록 개선 가능
  private final List<String> testUsers = List.of("CLC3TK");

  @Scheduled(fixedDelay = 300_000) // 5분 간격
  public void checkHealth() {
    String accessToken = userRepository.findById(testUsers.get(0))
        .orElseThrow().getAccessToken();
    String today = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));

    for (String userId : testUsers) {
      try {
        var heart = fetchService.fetchHeartRate(accessToken, today, "1d");
        var sleep = fetchService.fetchSleepData(accessToken, today);
        var step = fetchService.fetchStepData(accessToken, today, "1d");

        AnomalyResult result = HealthAnomalyDetector.detect(heart, sleep, step);

        if (result.hasAnyAnomaly()) {
          log.warn("이상 징후 발생: userId={} => {}", userId, result);
          //TODO: GPT 메시지 생성 + 저장 또는 알림 전송 후처리로 이어짐
          // 1. AI 기반 메시지 생성 (임시 mock)
          String title = "이상 건강 징후 감지!";
          String content = generateMockMessage(result); // 아래 메서드 참고

          // 2. DB 저장 (AlertLog)
          AlertLog logEntry = AlertLog.builder()
                  .fitbitUserId(userId)
                  .type(AlertType.valueOf("HEALTH_ANOMALY"))
                  .title(title)
                  .content(content)
                  .createdAt(LocalDateTime.now())
                  .build();
          alertLogRepository.save(logEntry);

          // 3. SSE 알림 전송
          AlertMessage alertMessage = AlertMessage.builder()
                  .fitbitUserId(userId)
                  .title(title)
                  .content(content)
                  .type("HEALTH_ANOMALY")  // 또는 "WARNING", "CRITICAL" 등으로 관리
                  .createdAt(LocalDateTime.now())
                  .build();

          alertService.sendAlert(userId, alertMessage);
        } else {
          log.info("정상 상태: userId={}", userId);
        }

      } catch (Exception e) {
        log.error("건강 상태 확인 실패: userId=" + userId, e);
      }
    }
  }

  private String generateMockMessage(AnomalyResult result) {
    List<String> messages = new ArrayList<>();

    if (result.isHighRestingHeartRate()) {
      messages.add("심박수가 평소보다 높습니다. 과로 또는 스트레스 상태일 수 있어요.");
    }
    if (result.isLowSleep()) {
      messages.add("수면 시간이 부족해요. 충분한 휴식을 취해주세요.");
    }
    if (result.isLowSteps()) {
      messages.add("오늘 활동량이 매우 적어요. 가벼운 산책을 권장합니다.");
    }

    // 메시지가 여러 개일 경우 줄 바꿈으로 구분
    return String.join("\n", messages);
  }
}
