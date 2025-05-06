package com.kufamilylinkbackend.infrastructure.repository;

import com.kufamilylinkbackend.application.domain.fitbit.FitbitUser;
import com.kufamilylinkbackend.application.domain.fitbit.health.SleepRecord;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SleepRecordRepository extends JpaRepository<SleepRecord, Long> {

  Optional<SleepRecord> findByFitbitUserAndDate(FitbitUser user, LocalDate date);
  List<SleepRecord> findAllByDate(LocalDate date);
}
