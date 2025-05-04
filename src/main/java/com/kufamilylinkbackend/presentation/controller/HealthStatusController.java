package com.kufamilylinkbackend.presentation.controller;

import com.kufamilylinkbackend.application.service.FitbitHealthStatusService;
import com.kufamilylinkbackend.data.response.HealthSummaryResponse;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class HealthStatusController {

  private final FitbitHealthStatusService fitbitHealthStatusService;

  @GetMapping("/{userId}/health/summary")
  public ResponseEntity<HealthSummaryResponse> getHealthSummary(@PathVariable("userId") String userId
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
}
