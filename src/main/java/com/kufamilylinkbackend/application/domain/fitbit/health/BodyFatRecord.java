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
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BodyFatRecord {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "fitbit_user_id")
  private FitbitUser fitbitUser;

  /**
   * 측정 날짜 (yyyy-MM-dd 포맷)
   */
  private LocalDate date;

  /**
   * 체지방률 (%)
   */
  private double fat;

  public void updateFat(double fat) {
    this.fat = fat;
  }
}
