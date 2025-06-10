package com.kufamilylinkbackend.notification.repository;

import com.kufamilylinkbackend.notification.AlertLog;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface AlertLogRepository extends JpaRepository<AlertLog, Long> {
    List<AlertLog> findAllByFitbitUserIdAndCreatedAtBetweenOrderByCreatedAtDesc(
            String fitbitUserId,
            LocalDateTime startOfDay,
            LocalDateTime endOfDay
    );
}