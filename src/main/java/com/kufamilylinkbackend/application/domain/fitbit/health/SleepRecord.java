package com.kufamilylinkbackend.application.domain.fitbit.health;

import com.kufamilylinkbackend.application.domain.fitbit.FitbitUser;
import com.kufamilylinkbackend.data.fitbit.health.SleepResponse;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
public class SleepRecord {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private Long id;

  @ManyToOne
  @JoinColumn(name = "fitbit_user_id")
  private FitbitUser fitbitUser;

  private LocalDate date;

  private long duration;
  private int efficiency;
  private String startTime;
  private String endTime;
  private boolean isMainSleep;

  private int totalMinutesAsleep;
  private int totalSleepRecords;
  private int totalTimeInBed;

  public void updateSleepData(SleepResponse.SleepData data, SleepResponse.SleepSummary summary) {
    this.duration = data.getDuration();
    this.efficiency = data.getEfficiency();
    this.startTime = data.getStartTime();
    this.endTime = data.getEndTime();
    this.isMainSleep = data.isMainSleep();

    this.totalMinutesAsleep = summary.getTotalMinutesAsleep();
    this.totalSleepRecords = summary.getTotalSleepRecords();
    this.totalTimeInBed = summary.getTotalTimeInBed();
  }
}
