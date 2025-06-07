package com.kufamilylinkbackend.application.service;

import com.kufamilylinkbackend.application.domain.fitbit.FitbitUser;
import com.kufamilylinkbackend.application.domain.fitbit.health.ActivitySummaryRecord;
import com.kufamilylinkbackend.application.domain.fitbit.health.BodyFatRecord;
import com.kufamilylinkbackend.application.domain.fitbit.health.HeartRateRecord;
import com.kufamilylinkbackend.application.domain.fitbit.health.SleepRecord;
import com.kufamilylinkbackend.application.domain.fitbit.health.StepRecord;
import com.kufamilylinkbackend.application.domain.fitbit.health.WaterRecord;
import com.kufamilylinkbackend.application.domain.fitbit.health.WeightRecord;
import com.kufamilylinkbackend.data.fitbit.health.ActivitySummaryResponse;
import com.kufamilylinkbackend.data.fitbit.health.BodyFatResponse;
import com.kufamilylinkbackend.data.response.DailyHealthResponse;
import com.kufamilylinkbackend.data.response.ThisWeekHealthSummaryResponse;
import com.kufamilylinkbackend.data.response.WeeklyHealthSummaryResponse;
import com.kufamilylinkbackend.global.util.FitbitHealthDataExtractionUtil;
import com.kufamilylinkbackend.data.fitbit.health.HeartRateResponse;
import com.kufamilylinkbackend.data.fitbit.health.SleepResponse;
import com.kufamilylinkbackend.data.fitbit.health.StepResponse;
import com.kufamilylinkbackend.data.fitbit.health.WaterResponse;
import com.kufamilylinkbackend.data.fitbit.health.WeightResponse;
import com.kufamilylinkbackend.data.response.HealthSummaryResponse;
import com.kufamilylinkbackend.global.util.WeekCalculator;
import com.kufamilylinkbackend.infrastructure.client.GeminiApiClient;
import com.kufamilylinkbackend.infrastructure.repository.ActivitySummaryRecordRepository;
import com.kufamilylinkbackend.infrastructure.repository.BodyFatRecordRepository;
import com.kufamilylinkbackend.infrastructure.repository.FitbitUserRepository;
import com.kufamilylinkbackend.infrastructure.repository.HeartRateRecordRepository;
import com.kufamilylinkbackend.infrastructure.repository.SleepRecordRepository;
import com.kufamilylinkbackend.infrastructure.repository.StepRecordRepository;
import com.kufamilylinkbackend.infrastructure.repository.WaterRecordRepository;
import com.kufamilylinkbackend.infrastructure.repository.WeightRecordRepository;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
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
  private final ActivitySummaryRecordRepository activitySummaryRepository;
  private final HeartRateRecordRepository heartRateRepository;
  private final StepRecordRepository stepRepository;
  private final SleepRecordRepository sleepRepository;
  private final WeightRecordRepository weightRepository;
  private final WaterRecordRepository waterRepository;
  private final BodyFatRecordRepository bodyFatRepository;
  private final GeminiApiClient geminiApiClient;

  @Transactional(readOnly = true)
  public List<DailyHealthResponse> getLast2WeekHealthData(String userId) {
    // 1) 유저 조회
    FitbitUser user = userRepository.findById(userId)
        .orElseThrow();

    // 2) 날짜 범위 계산 (오늘(포함)부터 2주 전까지)
    LocalDate endDate = LocalDate.now();           // 오늘
    LocalDate startDate = endDate.minusWeeks(2);   // 2주 전

    // 3) 각 레코드리스트를 한 번씩만 범위 조회
    List<ActivitySummaryRecord> activityList = activitySummaryRepository.findByFitbitUserAndDateBetween(
        user, startDate, endDate);
    List<StepRecord> stepList = stepRepository.findByFitbitUserAndDateBetween(user, startDate,
        endDate);
    List<SleepRecord> sleepList = sleepRepository.findByFitbitUserAndDateBetween(user, startDate,
        endDate);
    List<HeartRateRecord> hrList = heartRateRepository.findByFitbitUserAndDateBetween(user,
        startDate, endDate);
    List<WeightRecord> weightList = weightRepository.findByFitbitUserAndDateBetween(user, startDate,
        endDate);
    List<WaterRecord> waterList = waterRepository.findByFitbitUserAndDateBetween(user, startDate,
        endDate);
    List<BodyFatRecord> bodyFatList = bodyFatRepository.findByFitbitUserAndDateBetween(user,
        startDate, endDate);

    // 4) 날짜별 매핑: LocalDate -> Entity
    Map<LocalDate, ActivitySummaryRecord> activityMap = activityList.stream()
        .collect(Collectors.toMap(ActivitySummaryRecord::getDate, sr -> sr));
    Map<LocalDate, StepRecord> stepMap = stepList.stream()
        .collect(Collectors.toMap(StepRecord::getDate, sr -> sr));
    Map<LocalDate, SleepRecord> sleepMap = sleepList.stream()
        .collect(Collectors.toMap(SleepRecord::getDate, sr -> sr));
    Map<LocalDate, HeartRateRecord> hrMap = hrList.stream()
        .collect(Collectors.toMap(HeartRateRecord::getDate, hr -> hr));
    Map<LocalDate, WeightRecord> weightMap = weightList.stream()
        .collect(Collectors.toMap(WeightRecord::getDate, wr -> wr));
    Map<LocalDate, WaterRecord> waterMap = waterList.stream()
        .collect(Collectors.toMap(WaterRecord::getDate, wr -> wr));
    Map<LocalDate, BodyFatRecord> bodyFatMap = bodyFatList.stream()
        .collect(Collectors.toMap(BodyFatRecord::getDate, bf -> bf));

    // 5) 날짜 순회하면서 DTO 생성
    List<DailyHealthResponse> result = new ArrayList<>();
    LocalDate cursor = startDate;
    while (!cursor.isAfter(endDate)) {
      int caloriesOut = Optional.ofNullable(activityMap.get(cursor))
          .map(ActivitySummaryRecord::getCaloriesOut)
          .orElse(0);
      int sedentaryMinutes = Optional.ofNullable(activityMap.get(cursor))
          .map(ActivitySummaryRecord::getSedentaryMinutes)
          .orElse(0);
      int activeMinutes = Optional.ofNullable(activityMap.get(cursor))
          .map(ActivitySummaryRecord::getActiveMinutes)
          .orElse(0);
      // (1) steps
      int steps = Optional.ofNullable(stepMap.get(cursor))
          .map(StepRecord::getStepCount)
          .orElse(0);

      // (2) sleepHours (시간 단위)
      double sleepHours = Optional.ofNullable(sleepMap.get(cursor))
          .map(r -> r.getTotalMinutesAsleep() / 60.0)
          .orElse(0.0);
      int sleepEfficiency = Optional.ofNullable(sleepMap.get(cursor))
          .map(SleepRecord::getEfficiency)
          .orElse(0);

      // (3) restingHeartRate
      int restingHr = Optional.ofNullable(hrMap.get(cursor))
          .map(HeartRateRecord::getRestingHeartRate)
          .orElse(0);

      // (4) weight (kg)
      double weight = Optional.ofNullable(weightMap.get(cursor))
          .map(WeightRecord::getWeight)
          .orElse(0.0);
      double bmi = Optional.ofNullable(weightMap.get(cursor))
          .map(WeightRecord::getBmi)
          .orElse(0.0);

      // (5) water intake (mL)
      double water = Optional.ofNullable(waterMap.get(cursor))
          .map(WaterRecord::getWater)
          .orElse(0.0);

      // (6) body fat (%)
      double bodyFat = Optional.ofNullable(bodyFatMap.get(cursor))
          .map(BodyFatRecord::getFat)
          .orElse(0.0);

      // DTO 생성
      result.add(DailyHealthResponse.builder()
          .date(cursor)
          .sendentaryMinutes(sedentaryMinutes)
          .caloriesOut(caloriesOut)
          .activeMinutes(activeMinutes)
          .sleepEfficiency(sleepEfficiency)
          .bmi(bmi)
          .steps(steps)
          .sleepHours(Math.round(sleepHours * 100.0) / 100.0)  // 소수점 둘째 자리 반올림
          .heartRate(restingHr)
          .weight(Math.round(weight * 100.0) / 100.0)          // 소수점 둘째 자리 반올림
          .waterIntake(Math.round(water * 100.0) / 100.0)      // 소수점 둘째 자리 반올림
          .bodyFat(Math.round(bodyFat * 100.0) / 100.0)        // 소수점 둘째 자리 반올림
          .build());

      cursor = cursor.plusDays(1);
    }

    return result;
  }

  @Transactional(readOnly = true)
  public ThisWeekHealthSummaryResponse getThisWeekHealthSummary(String userId) {
    // 유저 찾기
    FitbitUser user = userRepository.findById(userId)
        .orElseThrow();
    WeeklyHealthSummaryResponse summary = getWeeklyHealthSummary(user,
        WeekCalculator.getThisWeekMonday(), LocalDate.now());

    String comment = generateWeekAnalysis(
        summary.getAverageRestingHeartRate(),
        summary.getAverageSteps(),
        summary.getAverageSleepHours(),
        summary.getAverageWeight(),
        summary.getAverageWaterIntake(),
        summary.getAverageBodyFat(),
        summary.getAverageCaloriesOut(),
        summary.getAverageSendentaryMinutes(),
        summary.getAverageActiveMinutes(),
        summary.getAverageSleepEfficiency(),
        summary.getAverageBmi()
    );

    return ThisWeekHealthSummaryResponse.builder()
        .period(ThisWeekHealthSummaryResponse.Period.builder()
            .startDate(WeekCalculator.getThisWeekMonday())
            .endDate(LocalDate.now())
            .build())
        .averageCaloriesOut(summary.getAverageCaloriesOut())
        .averageSendentaryMinutes(summary.getAverageSendentaryMinutes())
        .averageActiveMinutes(summary.getAverageActiveMinutes())
        .averageSleepEfficiency(summary.getAverageSleepEfficiency())
        .averageBmi(summary.getAverageBmi())
        .averageSteps(summary.getAverageSteps())
        .averageSleepHours(summary.getAverageSleepHours())
        .averageRestingHeartRate(summary.getAverageRestingHeartRate())
        .averageWeight(summary.getAverageWeight())
        .averageWaterIntake(summary.getAverageWaterIntake())
        .averageBodyFat(summary.getAverageBodyFat())
        .comment(comment)
        .build();
  }

  @Transactional(readOnly = true)
  public WeeklyHealthSummaryResponse getLastWeekHealthSummary(String userId) {
    // 유저 찾기
    FitbitUser user = userRepository.findById(userId)
        .orElseThrow();
    return getWeeklyHealthSummary(user, WeekCalculator.getLastWeekMonday(),
        WeekCalculator.getLastWeekSunday());
  }

  private WeeklyHealthSummaryResponse getWeeklyHealthSummary(FitbitUser user, LocalDate startDate,
      LocalDate endDate) {
    //0. ActivitySummary 조회 및 평균 구하기
    List<ActivitySummaryRecord> activities = activitySummaryRepository.findByFitbitUserAndDateBetween(
        user, startDate, endDate);
    double avgCaloriesOut = 0.0;
    double avgSedentaryMinutes = 0.0;
    double avgActiveMinutes = 0.0;

    if (!activities.isEmpty()) {
      avgCaloriesOut = Math.round(
          activities.stream().mapToInt(ActivitySummaryRecord::getCaloriesOut).average().orElse(0.0)
              * 100.0
      ) / 100.0;

      avgSedentaryMinutes = Math.round(
          activities.stream().mapToInt(ActivitySummaryRecord::getSedentaryMinutes).average()
              .orElse(0.0) * 100.0
      ) / 100.0;

      avgActiveMinutes = Math.round(
          activities.stream().mapToInt(ActivitySummaryRecord::getActiveMinutes).average()
              .orElse(0.0) * 100.0
      ) / 100.0;
    }

    // 1. StepRecord 조회 및 평균 걸음 수 계산
    List<Integer> stepsList = stepRepository.findByFitbitUserAndDateBetween(user, startDate,
            endDate)
        .stream()
        .map(StepRecord::getStepCount)
        .toList();
    int avgSteps = 0;
    if (!stepsList.isEmpty()) {
      avgSteps = (int) Math.round(
          stepsList.stream().mapToInt(Integer::intValue).average().orElse(0));
    }

    // 2. SleepRecord 조회 및 평균 수면 시간(시간 단위)과 평균 수면 효율 계산
    List<SleepRecord> sleepRecords = sleepRepository.findByFitbitUserAndDateBetween(user, startDate,
        endDate);

    List<Double> sleepHoursList = sleepRecords
        .stream()
        .map(record -> record.getTotalMinutesAsleep() / 60.0)
        .toList();
    double avgSleep = 0.0;
    if (!sleepHoursList.isEmpty()) {
      avgSleep = Math.round(
          sleepHoursList.stream().mapToDouble(Double::doubleValue).average().orElse(0.0) * 100.0
      ) / 100.0; // 소수점 둘째 자리까지 반올림
    }

    // 평균 수면 효율 (소수점 둘째 자리 반올림)
    double avgEfficiency = 0.0;
    if (!sleepRecords.isEmpty()) {
      avgEfficiency = Math.round(
          sleepRecords.stream().mapToInt(SleepRecord::getEfficiency).average().orElse(0.0) * 100.0
      ) / 100.0;
    }

    // 3. HeartRateRecord 조회 및 평균 휴식 심박수 계산
    List<Integer> hrList = heartRateRepository.findByFitbitUserAndDateBetween(user, startDate,
            endDate)
        .stream()
        .map(HeartRateRecord::getRestingHeartRate)
        .toList();
    int avgHr = 0;
    if (!hrList.isEmpty()) {
      avgHr = (int) Math.round(hrList.stream().mapToInt(Integer::intValue).average().orElse(0));
    }

    // 4. 평균 체중 (kg)
    //    WeightRecord 엔티티의 findByFitbitUserAndDateBetween 메서드를 사용
    List<WeightRecord> weightRecords = weightRepository.findByFitbitUserAndDateBetween(user,
        startDate, endDate);

    List<Double> weightList = weightRecords
        .stream()
        .map(WeightRecord::getWeight)
        .toList();
    double avgWeight = 0.0;
    if (!weightList.isEmpty()) {
      avgWeight = Math.round(
          weightList.stream().mapToDouble(Double::doubleValue).average().orElse(0.0) * 100.0
      ) / 100.0; // 소수점 둘째 자리까지 반올림
    }

    // 평균 BMI 계산
    double avgBmi = 0.0;
    if (!weightRecords.isEmpty()) {
      avgBmi = Math.round(
          weightRecords.stream().mapToDouble(WeightRecord::getBmi).average().orElse(0.0) * 100.0
      ) / 100.0;
    }

    // 5. 평균 수분 섭취량 (mL)
    //    WaterRecordRepository에 findByFitbitUserAndDateBetween 메서드가 필요합니다.
    List<Double> waterList = waterRepository.findByFitbitUserAndDateBetween(user, startDate,
            endDate)
        .stream()
        .map(WaterRecord::getWater)
        .toList();
    double avgWater = 0.0;
    if (!waterList.isEmpty()) {
      avgWater = Math.round(
          waterList.stream().mapToDouble(Double::doubleValue).average().orElse(0.0) * 100.0
      ) / 100.0;
    }

    // 6. 평균 체지방률 (%)
    List<Double> bodyFatList = bodyFatRepository.findByFitbitUserAndDateBetween(user, startDate,
            endDate)
        .stream()
        .map(BodyFatRecord::getFat)
        .toList();
    double avgBodyFat = 0.0;
    if (!bodyFatList.isEmpty()) {
      avgBodyFat = Math.round(
          bodyFatList.stream().mapToDouble(Double::doubleValue).average().orElse(0.0) * 100.0
      ) / 100.0;
    }

    // 6. 결과 빌드
    return WeeklyHealthSummaryResponse.builder()
        .period(WeeklyHealthSummaryResponse.Period.builder()
            .startDate(startDate)
            .endDate(endDate)
            .build())
        .averageCaloriesOut(avgCaloriesOut)
        .averageSendentaryMinutes(avgSedentaryMinutes)
        .averageActiveMinutes(avgActiveMinutes)
        .averageSleepEfficiency(avgEfficiency)
        .averageBmi(avgBmi)
        .averageSteps(avgSteps)
        .averageSleepHours(avgSleep)
        .averageRestingHeartRate(avgHr)
        .averageWeight(avgWeight)
        .averageWaterIntake(avgWater)
        .averageBodyFat(avgBodyFat)
        .build();
  }

  @Transactional(readOnly = true)
  public HealthSummaryResponse getTodayHealthSummary(String userId) {

    // 유저 찾기
    FitbitUser fitbitUser = userRepository.findById(userId)
        .orElseThrow();

    String accessToken = fitbitUser.getAccessToken(); //TODO: access token 유효성 검사
    String date = "today";
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

    double caloriesOut = FitbitHealthDataExtractionUtil.extractCaloriesOut(activitySummaryResponse);
    double sendentaryMinutes = FitbitHealthDataExtractionUtil.extractSedentaryMinutes(
        activitySummaryResponse);
    double activeMinutes = FitbitHealthDataExtractionUtil.extractActiveMinutes(
        activitySummaryResponse);
    double sleepEfficiency = FitbitHealthDataExtractionUtil.extractEfficiency(sleepResponse);
    double bmi = FitbitHealthDataExtractionUtil.extractBmi(weightResponse);
    int heartRate = FitbitHealthDataExtractionUtil.extractRestingHeartRate(heartRateResponse);
    double sleep = FitbitHealthDataExtractionUtil.extractSleepHours(sleepResponse);
    int step = FitbitHealthDataExtractionUtil.extractSteps(stepResponse);
    double weight = FitbitHealthDataExtractionUtil.extractWeight(weightResponse);
    double waterIntake = FitbitHealthDataExtractionUtil.extractWaterIntake(waterResponse);
    double bodyFat = FitbitHealthDataExtractionUtil.extractBodyFat(bodyFatResponse);

    //정보 분석
    String analysis = generateDayAnalysis(heartRate, step, sleep, weight, waterIntake, bodyFat,
        caloriesOut, sendentaryMinutes, activeMinutes, sleepEfficiency, bmi);

    return new HealthSummaryResponse(today, caloriesOut, sendentaryMinutes, activeMinutes,
        sleepEfficiency, bmi, heartRate, step, sleep, weight, waterIntake, bodyFat,
        analysis);
  }

  private String generateDayAnalysis(int heartRate, int steps, double sleepHours, double weight,
      double waterIntake, double bodyFat, double caloriesOut, double sedentaryMinutes,
      double activeMinutes, double sleepEfficiency, double bmi) {

    String prompt = String.format("""
              다음은 사용자의 오늘의 건강 데이터입니다.(아직 하루가 지나지 않아 모든 데이터가 없을 수 있습니다.):

              - 심박수: %d bpm
              - 수면 시간: %.1f시간
              - 걸음 수: %d보
              - 체중: %.2f kg
              - 물 섭취량: %.2f ml
              - 체지방률: %.2f %%
              - 소모 칼로리: %.2f kcal
              - 앉아있는 시간: %.2f 분
              - 활동 시간: %.2f 분
              - 수면 효율: %.2f %%
              - BMI: %.2f
              
              이 데이터를 바탕으로 오늘의 건강 상태를 한 줄로 요약해줘.
              - 문장은 정중하고 부드러운 조언 형태로 해줘.
              - 긍정적이면 응원, 위험 신호가 있으면 조심하라는 말을 해줘.
              - 결과는 100자 이내의 한국어 문장 하나로 반환해줘.
            """, heartRate, sleepHours, steps, weight, waterIntake, bodyFat,
        caloriesOut, sedentaryMinutes, activeMinutes, sleepEfficiency, bmi);

    return geminiApiClient.requestPrompt(prompt);
  }

  private String generateWeekAnalysis(int heartRate, int steps, double sleepHours, double weight,
      double waterIntake, double bodyFat, double caloriesOut, double sedentaryMinutes,
      double activeMinutes, double sleepEfficiency, double bmi) {
    String prompt = String.format("""
                다음은 사용자의 최근 7일간 건강 데이터입니다.:
                  
                  - 평균 심박수: %d bpm
                  - 평균 수면 시간: %.1f시간
                  - 평균 걸음 수: %d보
                  - 평균 체중: %.2f kg
                  - 평균 물 섭취량: %.2f ml
                  - 평균 체지방률: %.2f %%
                  - 평균 소모 칼로리: %.2f kcal
                  - 평균 앉아있는 시간: %.2f 분
                  - 평균 활동 시간: %.2f 분
                  - 평균 수면 효율: %.2f %%
                  - 평균 BMI: %.2f

                이 데이터를 종합 분석하여 사용자에게 전반적인 건강 상태 요약을 제공해줘.
                - 피드백은 자연스럽고 정중한 어투로 작성해줘.
                - 좋은 점과 개선할 점이 함께 포함되면 좋아.
                - 단, 결과 문장은 100자 이내로 간결하게 만들어줘.
            """, heartRate, sleepHours, steps, weight, waterIntake, bodyFat,
        caloriesOut, sedentaryMinutes, activeMinutes, sleepEfficiency, bmi);

    return geminiApiClient.requestPrompt(prompt);
  }


}
