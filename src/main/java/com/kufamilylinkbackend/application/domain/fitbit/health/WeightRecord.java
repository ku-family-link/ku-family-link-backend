package com.kufamilylinkbackend.application.domain.fitbit.health;

import com.kufamilylinkbackend.application.domain.fitbit.FitbitUser;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
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

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class WeightRecord {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "fitbit_user_id")
  private FitbitUser fitbitUser;

  private LocalDate date;

  private double weight;

  private double bmi;

  public void updateWeightAndBmi(double weight, double bmi) {
    this.weight = weight;
    this.bmi = bmi;
  }
}
