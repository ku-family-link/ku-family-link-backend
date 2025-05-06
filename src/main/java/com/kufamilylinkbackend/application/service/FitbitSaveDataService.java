package com.kufamilylinkbackend.application.service;

import com.kufamilylinkbackend.application.domain.fitbit.FitbitUser;
import com.kufamilylinkbackend.application.domain.fitbit.health.ActivitySummaryRecord;
import com.kufamilylinkbackend.application.domain.fitbit.health.HeartRateRecord;
import com.kufamilylinkbackend.application.domain.fitbit.health.HeartRateZoneRecord;
import com.kufamilylinkbackend.application.domain.fitbit.health.SleepRecord;
import com.kufamilylinkbackend.application.domain.fitbit.health.StepRecord;
import com.kufamilylinkbackend.data.fitbit.health.ActivitySummaryResponse;
import com.kufamilylinkbackend.data.fitbit.health.HeartRateResponse;
import com.kufamilylinkbackend.data.fitbit.health.SleepResponse;
import com.kufamilylinkbackend.data.fitbit.health.StepResponse;
import com.kufamilylinkbackend.infrastructure.repository.ActivitySummaryRecordRepository;
import com.kufamilylinkbackend.infrastructure.repository.FitbitUserRepository;
import com.kufamilylinkbackend.infrastructure.repository.HeartRateRecordRepository;
import com.kufamilylinkbackend.infrastructure.repository.SleepRecordRepository;
import com.kufamilylinkbackend.infrastructure.repository.StepRecordRepository;
import java.time.LocalDate;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class FitbitSaveDataService {

  private final FitbitDataFetchService fetchService;

  private final FitbitUserRepository fitbitUserRepository;
  private final ActivitySummaryRecordRepository activitySummaryRepository;
  private final HeartRateRecordRepository heartRateRepository;
  private final StepRecordRepository stepRepository;
  private final SleepRecordRepository sleepRepository;

  public void saveActivitySummary(String userId, LocalDate date) {

    // 유저 찾기
    FitbitUser fitbitUser = fitbitUserRepository.findById(userId)
        .orElseThrow();

    // 데이터 fetch
    ActivitySummaryResponse response = fetchService.fetchActivitySummary(
        fitbitUser.getAccessToken(), String.valueOf(date));
    ActivitySummaryResponse.ActivitySummary summary = response.getSummary();

    // Activity summary 정보 가져오기
    // 정보가 있다면 영속성 컨텍스트에 저장
    // 없다면 새로 만들기
    ActivitySummaryRecord record = activitySummaryRepository.findByFitbitUserAndDate(fitbitUser,
            date)
        .orElse(ActivitySummaryRecord.builder()
            .fitbitUser(fitbitUser)
            .date(date)
            .build());

    // Dto -> Entity
    record.update(summary.getCaloriesOut(),
        summary.getSedentaryMinutes(),
        summary.getFairlyActiveMinutes() + summary.getVeryActiveMinutes());

    //save
    activitySummaryRepository.save(record);
  }

  public void saveHeartRate(String userId, LocalDate date) {

    // 유저 찾기
    FitbitUser fitbitUser = fitbitUserRepository.findById(userId)
        .orElseThrow();

    // 데이터 패치
    HeartRateResponse response = fetchService.fetchHeartRate(fitbitUser.getAccessToken(),
        String.valueOf(date), "1d");
    if (response == null) {
      return;
    }
    if (response.getActivitiesHeart() == null) {
      return;
    }

    // 기존 기록 있으면 삭제 (자식 포함), 새로 insert
    heartRateRepository.deleteByFitbitUserAndDate(fitbitUser, date);

    // Dto -> Entity
    HeartRateResponse.HeartRateValue value = response.getActivitiesHeart().get(0).getValue();
    HeartRateRecord record = HeartRateRecord.builder()
        .fitbitUser(fitbitUser)
        .date(date)
        .restingHeartRate(value.getRestingHeartRate())
        .build();

    value.getHeartRateZones().forEach(zone -> {
      record.addZone(HeartRateZoneRecord.builder()
          .name(zone.getName())
          .min(zone.getMin())
          .max(zone.getMax())
          .minutes(zone.getMinutes())
          .caloriesOut(zone.getCaloriesOut())
          .heartRateRecord(record)
          .build());
    });

    heartRateRepository.save(record);
  }

  public void saveSteps(String userId, LocalDate date) {
    // 유저 찾기
    FitbitUser user = fitbitUserRepository.findById(userId).orElseThrow();
    // 데이터 패치
    StepResponse response = fetchService.fetchStepData(user.getAccessToken(), String.valueOf(date),
        "1d");

    // 날짜에 해당하는 데이터 추출
    int stepCount = response.getActivitiesSteps().stream()
        .filter(s -> LocalDate.parse(s.getDateTime()).equals(date))
        .mapToInt(s -> Integer.parseInt(s.getValue()))
        .sum();

    // 기존 데이터 존재 시 수정, 없으면 생성
    StepRecord record = stepRepository.findByFitbitUserAndDate(user, date)
        .orElse(StepRecord.builder()
            .fitbitUser(user)
            .date(date)
            .build());

    record.updateStepCount(stepCount);
    stepRepository.save(record);
  }

  public void saveSleep(String userId, LocalDate date) {
    // 유저 찾기
    FitbitUser user = fitbitUserRepository.findById(userId).orElseThrow();
    // 데이터 패치
    SleepResponse response = fetchService.fetchSleepData(user.getAccessToken(),
        String.valueOf(date));

    if (response.getSleep().isEmpty()) {
      return;
    }

    // 가장 큰 duration (주 수면 데이터) 기준으로 추출
    SleepResponse.SleepData mainSleep = response.getSleep().stream()
        .filter(SleepResponse.SleepData::isMainSleep)
        .findFirst()
        .orElse(response.getSleep().get(0)); // 없으면 첫 번째

    // 기존 데이터 갱신
    SleepRecord record = sleepRepository.findByFitbitUserAndDate(user, date)
        .orElse(SleepRecord.builder()
            .fitbitUser(user)
            .date(date)
            .build());

    record.updateSleepData(mainSleep, response.getSummary());
    sleepRepository.save(record);
  }
}
