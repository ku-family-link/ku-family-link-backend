package com.kufamilylinkbackend.infrastructure.repository;

import com.kufamilylinkbackend.application.domain.fitbit.FitbitUser;
import com.kufamilylinkbackend.application.domain.fitbit.health.WeightRecord;
import java.time.LocalDate;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WeightRecordRepository extends JpaRepository<WeightRecord, Long> {

  Optional<WeightRecord> findByFitbitUserAndDate(FitbitUser user, LocalDate date);

  List<WeightRecord> findByFitbitUserAndDateBetween(FitbitUser user, LocalDate startDate, LocalDate endDate);
}
