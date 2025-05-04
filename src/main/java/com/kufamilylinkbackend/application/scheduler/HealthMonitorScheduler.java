package com.kufamilylinkbackend.application.scheduler;

import com.kufamilylinkbackend.application.domain.HealthAnomalyDetector;
import com.kufamilylinkbackend.application.domain.HealthAnomalyDetector.AnomalyResult;
import com.kufamilylinkbackend.application.service.FitbitDataFetchService;
import com.kufamilylinkbackend.infrastructure.repository.FitbitUserRepository;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
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

  //TODO: 추후 DB에서 감지중인 userId 리스트를 가져오도록 개선 가능
  private final List<String> testUsers = List.of("CLC3TK");

  @Scheduled(fixedDelay = 60_000) // 1분 간격
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
        } else {
          log.info("정상 상태: userId={}", userId);
        }

      } catch (Exception e) {
        log.error("건강 상태 확인 실패: userId=" + userId, e);
      }
    }
  }
}
