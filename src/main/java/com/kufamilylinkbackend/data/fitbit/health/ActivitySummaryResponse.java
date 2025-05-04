package com.kufamilylinkbackend.data.fitbit.health;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Data;

@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class ActivitySummaryResponse {
  private ActivitySummary summary;

  @Data
  public static class ActivitySummary {
    private int steps;
    private int caloriesOut;
    private int sedentaryMinutes;
    private int fairlyActiveMinutes;
    private int veryActiveMinutes;
  }
}
