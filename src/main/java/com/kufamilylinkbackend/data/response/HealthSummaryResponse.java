package com.kufamilylinkbackend.data.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class HealthSummaryResponse {
  private String date;
  private int restingHeartRate;
  private int totalSteps;
  private double sleepHours;
  private String analysis;
}
