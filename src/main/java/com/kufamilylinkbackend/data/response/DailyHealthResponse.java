package com.kufamilylinkbackend.data.response;

import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DailyHealthResponse {
  private LocalDate date;
  private int caloriesOut;
  private int activeMinutes;
  private int sendentaryMinutes;
  private int sleepEfficiency;
  private double bmi;
  private int heartRate;
  private int steps;
  private double sleepHours;
  private double weight;
  private double waterIntake;
  private double bodyFat;
}
