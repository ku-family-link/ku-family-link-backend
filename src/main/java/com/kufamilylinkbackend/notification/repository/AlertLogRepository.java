package com.kufamilylinkbackend.notification.repository;

import com.kufamilylinkbackend.notification.AlertLog;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AlertLogRepository extends JpaRepository<AlertLog, Long> {
    List<AlertLog> findAllByFitbitUserIdOrderByCreatedAtDesc(String fitbitUserId);
}