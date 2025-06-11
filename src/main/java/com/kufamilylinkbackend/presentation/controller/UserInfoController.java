package com.kufamilylinkbackend.presentation.controller;

import com.kufamilylinkbackend.application.domain.fitbit.FitbitUser;
import com.kufamilylinkbackend.application.domain.fitbit.Guardian;
import com.kufamilylinkbackend.data.response.FitbitUserMyPageResponse;
import com.kufamilylinkbackend.data.response.GuardianUserMyPageResponse;
import com.kufamilylinkbackend.infrastructure.repository.FitbitUserRepository;
import com.kufamilylinkbackend.infrastructure.repository.GuardianRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserInfoController {

  private final FitbitUserRepository fitbitUserRepository;
  private final GuardianRepository guardianRepository;

  @GetMapping("/{userId}/fitbit")
  public ResponseEntity<FitbitUserMyPageResponse> getFitbitUserInfo(@PathVariable String userId) {
    FitbitUser fitbitUser = fitbitUserRepository.findById(userId)
        .orElseThrow();
    List<Guardian> guardian = guardianRepository.findByClientage(fitbitUser);
    Guardian responseGuardian;
    if(guardian.isEmpty()) {
      responseGuardian = Guardian.builder()
          .phone("-")
          .email("-")
          .name("-")
          .relationship("-")
          .build();
    }
    else {
      responseGuardian = guardian.get(0);
    }
    return ResponseEntity.ok(FitbitUserMyPageResponse.of(fitbitUser, responseGuardian));
  }

  @GetMapping("/{userId}/guardian")
  public ResponseEntity<GuardianUserMyPageResponse> getGuardianInfo(@PathVariable Long userId) {
    Guardian guardian = guardianRepository.findById(userId)
        .orElseThrow();
    return ResponseEntity.ok(GuardianUserMyPageResponse.of(guardian));
  }

}
