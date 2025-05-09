package com.kufamilylinkbackend.application.domain.fitbit.health;

import com.kufamilylinkbackend.application.domain.fitbit.FitbitUser;
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
public class StepRecord {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private Long id;

  @ManyToOne
  @JoinColumn(name = "fitbit_user_id")
  private FitbitUser fitbitUser;

  private LocalDate date;
  private int stepCount;

  public void updateStepCount(int stepCount) {
    this.stepCount = stepCount;
  }
}
