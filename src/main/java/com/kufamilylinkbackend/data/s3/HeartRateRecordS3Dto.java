package com.kufamilylinkbackend.data.s3;

import com.kufamilylinkbackend.application.domain.fitbit.health.HeartRateRecord;
import java.util.List;
import java.util.stream.Collectors;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class HeartRateRecordS3Dto {
  private String userId;
  private String date;
  private Integer restingHeartRate;
  private List<HeartRateZoneRecordS3Dto> zones;

  public static HeartRateRecordS3Dto from(HeartRateRecord r) {
    return HeartRateRecordS3Dto.builder()
        .userId(r.getFitbitUser().getFitbitUserId())
        .date(r.getDate().toString())
        .restingHeartRate(r.getRestingHeartRate())
        .zones(r.getZones().stream()
            .map(HeartRateZoneRecordS3Dto::from)
            .collect(Collectors.toList()))
        .build();
  }
}
