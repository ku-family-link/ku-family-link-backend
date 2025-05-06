package com.kufamilylinkbackend.data.s3;

import com.kufamilylinkbackend.application.domain.fitbit.health.SleepRecord;
import java.time.LocalDate;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class SleepRecordS3Dto {
  private String userId;
  private LocalDate date;

  private long duration;
  private int efficiency;
  private String startTime;
  private String endTime;
  private boolean isMainSleep;

  private int totalMinutesAsleep;
  private int totalSleepRecords;
  private int totalTimeInBed;

  public static SleepRecordS3Dto from(SleepRecord r) {
    return SleepRecordS3Dto.builder()
        .userId(r.getFitbitUser().getFitbitUserId())
        .date(r.getDate())
        .duration(r.getDuration())
        .efficiency(r.getEfficiency())
        .isMainSleep(r.isMainSleep())
        .startTime(r.getStartTime())
        .endTime(r.getEndTime())
        .totalMinutesAsleep(r.getTotalMinutesAsleep())
        .totalSleepRecords(r.getTotalSleepRecords())
        .totalTimeInBed(r.getTotalTimeInBed())
        .build();
  }
}
