package com.kufamilylinkbackend.infrastructure.repository;

import com.kufamilylinkbackend.application.domain.fitbit.FitbitUser;
import com.kufamilylinkbackend.application.domain.fitbit.health.SleepRecord;
import com.kufamilylinkbackend.application.domain.fitbit.health.StepRecord;
import java.time.LocalDate;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StepRecordRepository extends JpaRepository<StepRecord, Long> {

  Optional<StepRecord> findByFitbitUserAndDate(FitbitUser fitbitUser, LocalDate date);
  List<StepRecord> findAllByDate(LocalDate date);

  List<StepRecord> findByFitbitUserAndDateBetween(FitbitUser user, LocalDate startDate, LocalDate endDate);

}
