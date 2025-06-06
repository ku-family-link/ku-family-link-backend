package com.kufamilylinkbackend.data.fitbit.health;

import java.util.List;

public record BodyFatResponse(
    List<BodyFatLog> fat
) {
  public record BodyFatLog(
      String date,
      double fat
  ) {}
}

