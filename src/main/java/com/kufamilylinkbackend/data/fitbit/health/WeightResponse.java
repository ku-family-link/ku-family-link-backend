package com.kufamilylinkbackend.data.fitbit.health;

import java.util.List;

public record WeightResponse(
    List<WeightLog> weight
) {
  public record WeightLog(
      String date,
      double weight,
      double bmi
  ) {}
}
