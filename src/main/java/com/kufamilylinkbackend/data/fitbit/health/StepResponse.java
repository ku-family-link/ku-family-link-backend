package com.kufamilylinkbackend.data.fitbit.health;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import java.util.List;
import lombok.Data;

@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class StepResponse {

  @JsonProperty("activities-steps")
  private List<StepData> activitiesSteps;

  @Data
  public static class StepData {

    private String dateTime;
    private String value; // 걸음 수가 문자열로 옴
  }
}
