package com.kufamilylinkbackend.infrastructure.repository;

import com.kufamilylinkbackend.application.domain.fitbit.FitbitUser;
import com.kufamilylinkbackend.application.domain.fitbit.health.BodyFatRecord;
import java.time.LocalDate;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BodyFatRecordRepository extends JpaRepository<BodyFatRecord, Long> {

  Optional<BodyFatRecord> findByFitbitUserAndDate(FitbitUser user, LocalDate date);
}
