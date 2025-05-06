package com.kufamilylinkbackend.data.s3;

import com.kufamilylinkbackend.application.domain.fitbit.health.HeartRateZoneRecord;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class HeartRateZoneRecordS3Dto {

  private String name;
  private int min;
  private int max;
  private int minutes;
  private double caloriesOut;

  public static HeartRateZoneRecordS3Dto from(HeartRateZoneRecord z) {
    return HeartRateZoneRecordS3Dto.builder()
        .name(z.getName())
        .min(z.getMin())
        .max(z.getMax())
        .minutes(z.getMinutes())
        .caloriesOut(z.getCaloriesOut())
        .build();
  }
}
