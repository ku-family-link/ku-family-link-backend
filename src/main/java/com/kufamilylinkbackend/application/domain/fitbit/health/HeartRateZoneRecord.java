package com.kufamilylinkbackend.application.domain.fitbit.health;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class HeartRateZoneRecord {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  private String name;
  private int min;
  private int max;
  private int minutes;
  private double caloriesOut;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "heart_rate_record_id")
  private HeartRateRecord heartRateRecord;

}
