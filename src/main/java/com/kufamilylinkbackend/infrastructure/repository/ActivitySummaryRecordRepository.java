package com.kufamilylinkbackend.infrastructure.repository;

import com.kufamilylinkbackend.application.domain.fitbit.FitbitUser;
import com.kufamilylinkbackend.application.domain.fitbit.health.ActivitySummaryRecord;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ActivitySummaryRecordRepository extends
    JpaRepository<ActivitySummaryRecord, Long> {
  List<ActivitySummaryRecord> findByFitbitUserAndDate(FitbitUser fitbitUser, LocalDate date);

  List<ActivitySummaryRecord> findByFitbitUserAndDateBetween(FitbitUser user, LocalDate startDate, LocalDate endDate);
}
