package com.kufamilylinkbackend.application.domain;

import com.kufamilylinkbackend.data.fitbit.health.HeartRateResponse;
import com.kufamilylinkbackend.data.fitbit.health.SleepResponse;
import com.kufamilylinkbackend.data.fitbit.health.StepResponse;
import java.util.Optional;
import lombok.Builder;
import lombok.Data;

public class HealthAnomalyDetector {
  public static AnomalyResult detect(HeartRateResponse heartRate, SleepResponse sleep, StepResponse steps) {
    boolean highRestingHR = Optional.ofNullable(heartRate.getActivitiesHeart())
        .flatMap(list -> list.stream().findFirst())
        .map(h -> h.getValue().getRestingHeartRate())
        .map(hr -> hr >= 90)
        .orElse(false);

    boolean lowSleep = Optional.ofNullable(sleep.getSummary())
        .map(s -> s.getTotalMinutesAsleep() / 60.0)
        .map(hours -> hours < 4)
        .orElse(false);

    boolean lowSteps = Optional.ofNullable(steps.getActivitiesSteps())
        .flatMap(list -> list.stream().findFirst())
        .map(s -> Integer.parseInt(s.getValue()) < 1000)
        .orElse(false);

    return AnomalyResult.builder()
        .highRestingHeartRate(highRestingHR)
        .lowSleep(lowSleep)
        .lowSteps(lowSteps)
        .build();
  }

  @Data
  @Builder
  public static class AnomalyResult {
    private boolean highRestingHeartRate;
    private boolean lowSleep;
    private boolean lowSteps;

    public boolean hasAnyAnomaly() {
      return highRestingHeartRate || lowSleep || lowSteps;
    }
  }
}
