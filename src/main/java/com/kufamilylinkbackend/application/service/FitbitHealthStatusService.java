package com.kufamilylinkbackend.application.service;

import com.kufamilylinkbackend.application.domain.fitbit.FitbitUser;
import com.kufamilylinkbackend.data.fitbit.health.ActivitySummaryResponse;
import com.kufamilylinkbackend.data.fitbit.health.HeartRateResponse;
import com.kufamilylinkbackend.data.fitbit.health.SleepResponse;
import com.kufamilylinkbackend.data.fitbit.health.StepResponse;
import com.kufamilylinkbackend.data.response.HealthSummaryResponse;
import com.kufamilylinkbackend.infrastructure.repository.FitbitUserRepository;
import java.time.LocalDate;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class FitbitHealthStatusService {

  private final FitbitDataFetchService fetchService;
  private final FitbitUserRepository userRepository;

  @Transactional(readOnly = true)
  public HealthSummaryResponse getTodayHealthSummary(String userId, String date) {
    FitbitUser fitbitUser = userRepository.findById(userId)
        .orElseThrow();

    String accessToken = fitbitUser.getAccessToken(); //TODO: access token 유효성 검사

    String today = LocalDate.now().toString();
    String period = "1d";

    // Fitbit에서 오늘의 정보를 가져오기
    ActivitySummaryResponse activitySummaryResponse = fetchService.fetchActivitySummary(accessToken,
        today);
    System.out.println(activitySummaryResponse);
    HeartRateResponse heartRate = fetchService.fetchHeartRate(accessToken, date, period);
    System.out.println(heartRate);
    StepResponse step = fetchService.fetchStepData(accessToken, date, period);
    System.out.println(step);
    SleepResponse sleep = fetchService.fetchSleepData(accessToken, today);
    System.out.println(sleep);

    //수면시간
    double sleepHours = Optional.ofNullable(sleep)
        .map(s -> s.getSummary().getTotalMinutesAsleep() / 60.0)
        .orElse(0.0);
    //아무 활동도 하지 않은 휴식 시간
    int restingHr = Optional.ofNullable(heartRate.getActivitiesHeart())
        .flatMap(list -> list.stream().findFirst())
        .map(h -> h.getValue().getRestingHeartRate())
        .orElse(0);
    //걸음 수
    int steps = Optional.ofNullable(step.getActivitiesSteps())
        .flatMap(list -> list.stream().findFirst())
        .map(s -> Integer.parseInt(s.getValue()))
        .orElse(0);
    
    //정보 분석
    String analysis = generateAnalysis(restingHr, steps, sleepHours);

    return new HealthSummaryResponse(today, restingHr, steps, sleepHours, analysis);
  }

  //TODO: AI를 통한 분석 혹은 기법 적용
  private String generateAnalysis(int restingHr, int steps, double sleepHours) {
    if (sleepHours < 5 && steps < 3000) {
      return "수면 부족과 활동량 부족이 감지돼요. 건강 관리가 필요해요.";
    }
    if (steps < 3000) {
      return "오늘은 거의 움직이지 않으셨어요. 산책이라도 해보시는 건 어때요?";
    }
    if (sleepHours < 5) {
      return "수면이 부족해요. 조금 더 휴식을 취해보세요.";
    }
    return "건강 상태는 전반적으로 양호해요!";
  }
}
