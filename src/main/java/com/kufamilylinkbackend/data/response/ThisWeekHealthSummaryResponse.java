package com.kufamilylinkbackend.data.response;

import java.time.LocalDate;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ThisWeekHealthSummaryResponse {
  private final Period period;
  private final double averageCaloriesOut;
  private final double averageSendentaryMinutes;
  private final double averageActiveMinutes;

  private final int averageSteps;
  private final double averageSleepHours;
  private final double averageSleepEfficiency;

  private final int averageRestingHeartRate;

  // 추가 필드: 평균 체중, 평균 수분 섭취량, 평균 체지방률
  private final double averageWeight;        // kg
  private final double averageBmi;
  private final double averageWaterIntake;   // mL
  private final double averageBodyFat;       // %
  private final String comment;

  @Getter
  @Builder
  public static class Period {
    private final LocalDate startDate;
    private final LocalDate endDate;
  }
}
