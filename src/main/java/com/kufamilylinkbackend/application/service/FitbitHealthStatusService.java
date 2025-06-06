package com.kufamilylinkbackend.application.service;

import com.kufamilylinkbackend.application.domain.fitbit.FitbitUser;
import com.kufamilylinkbackend.data.fitbit.health.ActivitySummaryResponse;
import com.kufamilylinkbackend.data.fitbit.health.BodyFatResponse;
import com.kufamilylinkbackend.global.util.FitbitHealthDataExtractionUtil;
import com.kufamilylinkbackend.data.fitbit.health.HeartRateResponse;
import com.kufamilylinkbackend.data.fitbit.health.SleepResponse;
import com.kufamilylinkbackend.data.fitbit.health.StepResponse;
import com.kufamilylinkbackend.data.fitbit.health.WaterResponse;
import com.kufamilylinkbackend.data.fitbit.health.WeightResponse;
import com.kufamilylinkbackend.data.response.HealthSummaryResponse;
import com.kufamilylinkbackend.infrastructure.client.GeminiApiClient;
import com.kufamilylinkbackend.infrastructure.repository.FitbitUserRepository;
import java.time.LocalDate;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class FitbitHealthStatusService {

  private final FitbitDataFetchService fetchService;
  private final FitbitUserRepository userRepository;
  private final GeminiApiClient geminiApiClient;

  @Transactional(readOnly = true)
  public void getWeeklyHealthSummary() {

  }
  @Transactional(readOnly = true)
  public HealthSummaryResponse getTodayHealthSummary(String userId, String date) {

    // 유저 찾기
    FitbitUser fitbitUser = userRepository.findById(userId)
        .orElseThrow();

    String accessToken = fitbitUser.getAccessToken(); //TODO: access token 유효성 검사
    String today = LocalDate.now().toString();
    String period = "1d";

    // Fitbit에서 오늘의 정보를 가져오기
    ActivitySummaryResponse activitySummaryResponse = fetchService.fetchActivitySummary(accessToken,
        today);
    HeartRateResponse heartRateResponse = fetchService.fetchHeartRate(accessToken, date, period);
    StepResponse stepResponse = fetchService.fetchStepData(accessToken, date, period);
    SleepResponse sleepResponse = fetchService.fetchSleepData(accessToken, today);
    WeightResponse weightResponse = fetchService.fetchWeight(accessToken, today);
    WaterResponse waterResponse = fetchService.fetchWaterLogs(accessToken, today);
    BodyFatResponse bodyFatResponse = fetchService.fetchBodyFat(accessToken, today);

    {
      log.info("Activity Summary: {}\nHeart Rate: {}\nStep: {}\nSleep: {}"
          , activitySummaryResponse
          , heartRateResponse
          , stepResponse
          , sleepResponse);

      log.info("Weight: {}\nWater: {}\n Body Fat: {}"
          , weightResponse
          , waterResponse
          , bodyFatResponse);
    }

    int heartRate = FitbitHealthDataExtractionUtil.extractRestingHeartRate(heartRateResponse);
    double sleep = FitbitHealthDataExtractionUtil.extractSleepHours(sleepResponse);
    int step = FitbitHealthDataExtractionUtil.extractSteps(stepResponse);
    double weight = FitbitHealthDataExtractionUtil.extractWeight(weightResponse);
    double waterIntake = FitbitHealthDataExtractionUtil.extractWaterIntake(waterResponse);
    double bodyFat = FitbitHealthDataExtractionUtil.extractBodyFat(bodyFatResponse);

    //정보 분석
    String analysis = generateDayAnalysis(heartRate, step, sleep, weight, waterIntake, bodyFat);

    return new HealthSummaryResponse(today, heartRate, step, sleep, weight, waterIntake, bodyFat, analysis);
  }
  private String generateDayAnalysis(int heartRate, int steps, double sleepHours, double weight,
      double waterIntake, double bodyFat) {

    String prompt = String.format("""
          다음은 사용자의 오늘의 건강 데이터입니다.(아직 하루가 지나지 않아 모든 데이터가 없을 수 있습니다.):

          - 심박수: %d bpm
          - 수면 시간: %.1f시간
          - 걸음 수: %d보
          - 체중 : %f kg
          - 물 섭취량: %f ml
          - 체지방률: %f %%
          
          이 데이터를 바탕으로 오늘의 건강 상태를 한 줄로 요약해줘.
          - 문장은 정중하고 부드러운 조언 형태로 해줘.
          - 긍정적이면 응원, 위험 신호가 있으면 조심하라는 말을 해줘.
          - 결과는 100자 이내의 한국어 문장 하나로 반환해줘.
        """, heartRate, sleepHours, steps, weight, waterIntake, bodyFat);

    return geminiApiClient.requestPrompt(prompt);
  }

  private String generateWeekAnalysis(List<Integer> restingHrs, List<Integer> steps, List<Double> sleepHours) {
    String prompt = String.format("""
        다음은 사용자의 최근 7일간 건강 데이터입니다:
        
        - 심박수 (bpm): %s
        - 걸음 수 (보): %s
        - 수면 시간 (시간): %s

        이 데이터를 종합 분석하여 사용자에게 전반적인 건강 상태 요약을 제공해줘.
        - 피드백은 자연스럽고 정중한 어투로 작성해줘.
        - 좋은 점과 개선할 점이 함께 포함되면 좋아.
        - 단, 결과 문장은 100자 이내로 간결하게 만들어줘.
    """,
        restingHrs.toString(),
        steps.toString(),
        sleepHours.toString()
    );

    return prompt;
  }


}
