package com.kufamilylinkbackend.infrastructure.repository;

import com.kufamilylinkbackend.application.domain.fitbit.FitbitUser;
import com.kufamilylinkbackend.application.domain.fitbit.health.HeartRateRecord;
import com.kufamilylinkbackend.application.domain.fitbit.health.SleepRecord;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface HeartRateRecordRepository extends JpaRepository<HeartRateRecord, Long> {

  Optional<HeartRateRecord> findByDate(LocalDate date);

  void deleteByFitbitUserAndDate(FitbitUser fitbitUser, LocalDate date);

  List<HeartRateRecord> findAllByDate(LocalDate date);
}
