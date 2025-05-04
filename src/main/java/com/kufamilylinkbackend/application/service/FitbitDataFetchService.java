package com.kufamilylinkbackend.application.service;

import com.kufamilylinkbackend.data.fitbit.health.ActivitySummaryResponse;
import com.kufamilylinkbackend.data.fitbit.FitbitUserProfile;
import com.kufamilylinkbackend.data.fitbit.health.HeartRateResponse;
import com.kufamilylinkbackend.data.fitbit.health.SleepResponse;
import com.kufamilylinkbackend.data.fitbit.health.StepResponse;
import com.kufamilylinkbackend.infrastructure.client.FitbitApiClient;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class FitbitDataFetchService {

  private final FitbitApiClient fitbitApiClient;

  public FitbitUserProfile fetchUserProfile(String accessToken) {
    return fitbitApiClient.fetch("https://api.fitbit.com/1/user/-/profile.json", accessToken,
        FitbitUserProfile.class);
  }

  public HeartRateResponse fetchHeartRate(String accessToken,
      String date, String period) {
    String url = String.format("https://api.fitbit.com/1/user/-/activities/heart/date/%s/%s.json",
        date, period);

    return fitbitApiClient.fetch(url, accessToken, HeartRateResponse.class);
  }

  public StepResponse fetchStepData(String accessToken, String date, String period) {
    String url = String.format("https://api.fitbit.com/1/user/-/activities/steps/date/%s/%s.json",
        date, period);
    return fitbitApiClient.fetch(url, accessToken, StepResponse.class);
  }

  public SleepResponse fetchSleepData(String accessToken, String date) {
    String url = String.format("https://api.fitbit.com/1.2/user/-/sleep/date/%s.json", date);
    return fitbitApiClient.fetch(url, accessToken, SleepResponse.class);
  }

  public ActivitySummaryResponse fetchActivitySummary(String accessToken, String date) {
    String url = String.format("https://api.fitbit.com/1/user/-/activities/date/%s.json", date);
    return fitbitApiClient.fetch(url, accessToken, ActivitySummaryResponse.class);
  }

}
