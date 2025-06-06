package com.kufamilylinkbackend.global.util;

import com.kufamilylinkbackend.data.fitbit.health.BodyFatResponse;
import com.kufamilylinkbackend.data.fitbit.health.BodyFatResponse.BodyFatLog;
import com.kufamilylinkbackend.data.fitbit.health.HeartRateResponse;
import com.kufamilylinkbackend.data.fitbit.health.SleepResponse;
import com.kufamilylinkbackend.data.fitbit.health.StepResponse;
import com.kufamilylinkbackend.data.fitbit.health.WaterResponse;
import com.kufamilylinkbackend.data.fitbit.health.WaterResponse.WaterSummary;
import com.kufamilylinkbackend.data.fitbit.health.WeightResponse;
import com.kufamilylinkbackend.data.fitbit.health.WeightResponse.WeightLog;
import java.util.Optional;

public class FitbitHealthDataExtractionUtil {
  public static double extractSleepHours(SleepResponse sleep) {
    return Optional.ofNullable(sleep)
        .map(s -> s.getSummary().getTotalMinutesAsleep() / 60.0)
        .orElse(0.0);
  }

  public static int extractRestingHeartRate(HeartRateResponse heartRate) {
    return Optional.ofNullable(heartRate.getActivitiesHeart())
        .flatMap(list -> list.stream().findFirst())
        .map(h -> h.getValue().getRestingHeartRate())
        .orElse(0);
  }

  public static int extractSteps(StepResponse step) {
    return Optional.ofNullable(step.getActivitiesSteps())
        .flatMap(list -> list.stream().findFirst())
        .map(s -> Integer.parseInt(s.getValue()))
        .orElse(0);
  }

  // 체중 (kg) 가져오기
  public static double extractWeight(WeightResponse weightResponse) {
    return Optional.ofNullable(weightResponse.weight())
        .flatMap(list -> list.stream().findFirst())
        .map(WeightLog::weight)
        .orElse(0.0);
  }

  // 물 섭취량 (ml) 가져오기
  public static double extractWaterIntake(WaterResponse waterResponse) {
    return Optional.ofNullable(waterResponse.summary())
        .map(WaterSummary::water)
        .orElse(0.0);
  }

  // 체지방률 (%) 가져오기
  public static double extractBodyFat(BodyFatResponse bodyFatResponse) {
    return Optional.ofNullable(bodyFatResponse.fat())
        .flatMap(list -> list.stream().findFirst())
        .map(BodyFatLog::fat)
        .orElse(0.0);
  }
}
