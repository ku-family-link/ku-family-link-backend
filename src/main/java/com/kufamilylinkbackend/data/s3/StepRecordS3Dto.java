package com.kufamilylinkbackend.data.s3;

import com.kufamilylinkbackend.application.domain.fitbit.health.StepRecord;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class StepRecordS3Dto {
  private String userId;
  private String date;
  private int stepCount;

  public static StepRecordS3Dto from(StepRecord r) {
    return StepRecordS3Dto.builder()
        .userId(r.getFitbitUser().getFitbitUserId())
        .date(r.getDate().toString())
        .stepCount(r.getStepCount())
        .build();
  }
}
