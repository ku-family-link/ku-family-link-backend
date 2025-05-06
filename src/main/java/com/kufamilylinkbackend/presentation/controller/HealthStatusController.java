package com.kufamilylinkbackend.presentation.controller;

import com.kufamilylinkbackend.application.scheduler.FitbitDataS3BackupScheduler;
import com.kufamilylinkbackend.application.service.FitbitHealthStatusService;
import com.kufamilylinkbackend.application.service.FitbitSaveDataService;
import com.kufamilylinkbackend.data.response.HealthSummaryResponse;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class HealthStatusController {

  private final FitbitHealthStatusService fitbitHealthStatusService;
  private final FitbitSaveDataService fitbitSaveDataService;
  private final FitbitDataS3BackupScheduler backupScheduler;

  @GetMapping("/{userId}/health/summary")
  public ResponseEntity<HealthSummaryResponse> getHealthSummary(
      @PathVariable("userId") String userId
      , @RequestParam(value = "date", required = false, defaultValue = "today") String date) {
    if (!date.equals("today") && !isValidDate(date)) {
      return ResponseEntity.badRequest()
          .body(null);
    }
    return ResponseEntity.ok(fitbitHealthStatusService.getTodayHealthSummary(userId, date));
  }

  private boolean isValidDate(String dateStr) {
    try {
      LocalDate.parse(dateStr, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
      return true;
    } catch (DateTimeParseException e) {
      return false;
    }
  }

  @PostMapping("/{userId}/health/today")
  public ResponseEntity<Map<String, String>> saveTodayHealthStatus(
      @PathVariable("userId") String userId) {
    fitbitSaveDataService.saveActivitySummary(userId, LocalDate.now());
    fitbitSaveDataService.saveSleep(userId, LocalDate.now());
    fitbitSaveDataService.saveSteps(userId, LocalDate.now());
    fitbitSaveDataService.saveHeartRate(userId, LocalDate.now());
    return ResponseEntity.ok(Map.of("status", "success"));
  }

  @PostMapping("/all/fitbit/records/backup/yesterday")
  public ResponseEntity<Map<String, String>> backupTest() {
    backupScheduler.backupYesterdayDataToS3();
    return ResponseEntity.ok(Map.of("status", "success"));
  }
}
