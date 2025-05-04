package com.kufamilylinkbackend.data.fitbit.health;

import java.util.List;
import lombok.Data;

@Data
public class HeartRateResponse {
  private List<HeartRateDataset> activitiesHeart;

  @Data
  public static class HeartRateDataset {
    private String dateTime;
    private HeartRateValue value;
  }

  @Data
  public static class HeartRateValue {
    private int restingHeartRate;
    private List<Zone> heartRateZones;

    @Data
    public static class Zone {
      private String name;
      private int min;
      private int max;
      private int minutes;
      private double caloriesOut;
    }
  }
}
