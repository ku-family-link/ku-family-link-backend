package com.kufamilylinkbackend.presentation.controller;

import com.kufamilylinkbackend.application.scheduler.FitbitDataS3BackupScheduler;
import com.kufamilylinkbackend.application.service.FitbitHealthStatusService;
import com.kufamilylinkbackend.application.service.FitbitSaveDataService;
import com.kufamilylinkbackend.data.response.DailyHealthResponse;
import com.kufamilylinkbackend.data.response.HealthSummaryResponse;
import com.kufamilylinkbackend.data.response.ThisWeekHealthSummaryResponse;
import com.kufamilylinkbackend.data.response.WeeklyHealthSummaryResponse;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class HealthStatusController {

  private final FitbitHealthStatusService fitbitHealthStatusService;
  private final FitbitSaveDataService fitbitSaveDataService;
  private final FitbitDataS3BackupScheduler backupScheduler;

  @GetMapping("/{userId}/health/summary/today")
  public ResponseEntity<HealthSummaryResponse> getHealthSummary(
      @PathVariable("userId") String userId) {
    saveTodayHealthStatus(userId);
    return ResponseEntity.ok(fitbitHealthStatusService.getTodayHealthSummary(userId));
  }

  @GetMapping("/{userId}/health/summary/last-week")
  public ResponseEntity<WeeklyHealthSummaryResponse> getLastWeekHealthSummary(
      @PathVariable("userId") String userId) {
    return ResponseEntity.ok(fitbitHealthStatusService.getLastWeekHealthSummary(userId));
  }

  @GetMapping("/{userId}/health/summary/this-week")
  public ResponseEntity<ThisWeekHealthSummaryResponse> getThisWeekHealthSummary(
      @PathVariable("userId") String userId) {
    saveTodayHealthStatus(userId);
    return ResponseEntity.ok(fitbitHealthStatusService.getThisWeekHealthSummary(userId));
  }

  @GetMapping("/{userId}/health/all/last-2week")
  public ResponseEntity<List<DailyHealthResponse>> getLast2WeekHealthData(
      @PathVariable("userId") String userId) {
    return ResponseEntity.ok(fitbitHealthStatusService.getLast2WeekHealthData(userId));
  }

  @PostMapping("/{userId}/health/today")
  public ResponseEntity<Map<String, String>> saveTodayHealthStatus(
      @PathVariable("userId") String userId) {
    fitbitSaveDataService.saveActivitySummary(userId, LocalDate.now());
    fitbitSaveDataService.saveSleep(userId, LocalDate.now());
    fitbitSaveDataService.saveSteps(userId, LocalDate.now());
    fitbitSaveDataService.saveHeartRate(userId, LocalDate.now());
    fitbitSaveDataService.saveWater(userId, LocalDate.now());
    fitbitSaveDataService.saveWeight(userId, LocalDate.now());
    fitbitSaveDataService.saveBodyFat(userId, LocalDate.now());
    return ResponseEntity.ok(Map.of("status", "success"));
  }

  @PostMapping("/all/fitbit/records/backup/yesterday")
  public ResponseEntity<Map<String, String>> backupTest() {
    backupScheduler.backupYesterdayDataToS3();
    return ResponseEntity.ok(Map.of("status", "success"));
  }
}
