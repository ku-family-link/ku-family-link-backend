package com.kufamilylinkbackend.application.domain.fitbit.health;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import java.time.LocalDate;
import java.util.UUID;

@Entity
public class ActivitySummaryRecord {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private Long id;

  private UUID fitbitUserId;
  private LocalDate date;
  private int caloriesOut;
  private int sedentaryMinutes;
  private int activeMinutes;
}
