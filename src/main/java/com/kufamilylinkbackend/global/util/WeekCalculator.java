package com.kufamilylinkbackend.global.util;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.temporal.TemporalAdjuster;
import java.time.temporal.TemporalAdjusters;

public class WeekCalculator {

  public static LocalDate getLastWeekMonday() {
    return getThisWeekMonday().minusWeeks(1);
  }

  public static LocalDate getLastWeekSunday() {
    return getThisWeekMonday().minusDays(1);
  }

  public static LocalDate getThisWeekMonday() {
    LocalDate today = LocalDate.now();
    return today.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY));
  }

}
