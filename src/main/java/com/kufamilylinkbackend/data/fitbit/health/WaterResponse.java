package com.kufamilylinkbackend.data.fitbit.health;

public record WaterResponse(
    WaterSummary summary
) {
  public record WaterSummary(
      double water
  ) {}
}