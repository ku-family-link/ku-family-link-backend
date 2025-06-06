package com.kufamilylinkbackend.application.service;

import com.kufamilylinkbackend.data.fitbit.health.ActivitySummaryResponse;
import com.kufamilylinkbackend.data.fitbit.FitbitUserProfile;
import com.kufamilylinkbackend.data.fitbit.health.BodyFatResponse;
import com.kufamilylinkbackend.data.fitbit.health.HeartRateResponse;
import com.kufamilylinkbackend.data.fitbit.health.SleepResponse;
import com.kufamilylinkbackend.data.fitbit.health.StepResponse;
import com.kufamilylinkbackend.data.fitbit.health.WaterResponse;
import com.kufamilylinkbackend.data.fitbit.health.WeightResponse;
import com.kufamilylinkbackend.infrastructure.client.FitbitApiClient;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class FitbitDataFetchService {

  private final FitbitApiClient fitbitApiClient;

  /**
   * 프로필 가져오기
   */
  public FitbitUserProfile fetchUserProfile(String accessToken) {
    return fitbitApiClient.fetch("https://api.fitbit.com/1/user/-/profile.json", accessToken,
        FitbitUserProfile.class);
  }

  /**
   * 심박수 가져오기
   */
  public HeartRateResponse fetchHeartRate(String accessToken,
      String date, String period) {
    String url = String.format("https://api.fitbit.com/1/user/-/activities/heart/date/%s/%s.json",
        date, period);

    return fitbitApiClient.fetch(url, accessToken, HeartRateResponse.class);
  }

  /**
   * 걸음 수 가져오기
   */
  public StepResponse fetchStepData(String accessToken, String date, String period) {
    String url = String.format("https://api.fitbit.com/1/user/-/activities/steps/date/%s/%s.json",
        date, period);
    return fitbitApiClient.fetch(url, accessToken, StepResponse.class);
  }

  /**
   * 수면시간 가져오기
   */
  public SleepResponse fetchSleepData(String accessToken, String date) {
    String url = String.format("https://api.fitbit.com/1.2/user/-/sleep/date/%s.json", date);
    return fitbitApiClient.fetch(url, accessToken, SleepResponse.class);
  }

  /**
   * 활동 요약 가져오기
   */
  public ActivitySummaryResponse fetchActivitySummary(String accessToken, String date) {
    String url = String.format("https://api.fitbit.com/1/user/-/activities/date/%s.json", date);
    return fitbitApiClient.fetch(url, accessToken, ActivitySummaryResponse.class);
  }


  /**
   * 수분 섭취량 가져오기
   */
  public WaterResponse fetchWaterLogs(String accessToken, String date) {
    String url = String.format("https://api.fitbit.com/1/user/-/foods/log/water/date/%s.json", date);
    return fitbitApiClient.fetch(url, accessToken, WaterResponse.class);
  }

  /**
   * 체중 정보 가져오기
   */
  public WeightResponse fetchWeight(String accessToken, String date) {
    String url = String.format("https://api.fitbit.com/1/user/-/body/log/weight/date/%s.json", date);
    return fitbitApiClient.fetch(url, accessToken, WeightResponse.class);
  }

  /**
   * 체지방률 가져오기
   */
  public BodyFatResponse fetchBodyFat(String accessToken, String date) {
    String url = String.format("https://api.fitbit.com/1/user/-/body/log/fat/date/%s.json", date);
    return fitbitApiClient.fetch(url, accessToken, BodyFatResponse.class);
  }

}
