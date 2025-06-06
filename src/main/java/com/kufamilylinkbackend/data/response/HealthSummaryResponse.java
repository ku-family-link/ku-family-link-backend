package com.kufamilylinkbackend.data.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class HealthSummaryResponse {
  private String date;
  private int heartRate;
  private int totalSteps;
  private double sleepHours;
  private double weight;
  private double waterIntake;
  private double bodyFat;
  private String analysis;
}
