package com.kufamilylinkbackend.data.fitbit.health;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import java.util.List;
import lombok.Data;

@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class SleepResponse {
  private List<SleepData> sleep;
  private SleepSummary summary;

  @Data
  public static class SleepData {
    private String dateOfSleep;
    private long duration;
    private int efficiency;
    private String startTime;
    private String endTime;
  }

  @Data
  public static class SleepSummary {
    private int totalMinutesAsleep;
    private int totalSleepRecords;
  }
}
