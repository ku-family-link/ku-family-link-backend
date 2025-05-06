package com.kufamilylinkbackend.application.domain.fitbit.health;

import com.kufamilylinkbackend.application.domain.fitbit.FitbitUser;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import java.time.LocalDate;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ActivitySummaryRecord {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private Long id;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "fitbit_user_id")
  private FitbitUser fitbitUser;
  private LocalDate date;
  private int caloriesOut;
  private int sedentaryMinutes;
  private int activeMinutes;

  public void update(int caloriesOut, int sedentaryMinutes, int activeMinutes) {
    this.caloriesOut = caloriesOut;
    this.sedentaryMinutes = sedentaryMinutes;
    this.activeMinutes = activeMinutes;
  }
}
