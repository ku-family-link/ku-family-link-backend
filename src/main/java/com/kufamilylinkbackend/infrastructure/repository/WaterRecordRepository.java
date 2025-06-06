package com.kufamilylinkbackend.infrastructure.repository;

import com.kufamilylinkbackend.application.domain.fitbit.FitbitUser;
import com.kufamilylinkbackend.application.domain.fitbit.health.WaterRecord;
import java.time.LocalDate;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WaterRecordRepository extends JpaRepository<WaterRecord, Long> {

  Optional<WaterRecord> findByFitbitUserAndDate(FitbitUser user, LocalDate date);
}
