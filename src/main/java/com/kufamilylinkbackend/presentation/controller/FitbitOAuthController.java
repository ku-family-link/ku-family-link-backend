package com.kufamilylinkbackend.presentation.controller;

import com.kufamilylinkbackend.application.service.FitbitDataFetchService;
import com.kufamilylinkbackend.application.service.FitbitOAuthService;
import com.kufamilylinkbackend.data.fitbit.FitbitTokenResponse;
import com.kufamilylinkbackend.data.fitbit.FitbitUserProfile;
import java.net.URI;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/oauth2/fitbit")
@RequiredArgsConstructor
public class FitbitOAuthController {

  private final FitbitOAuthService fitbitOAuthService;
  private final FitbitDataFetchService fitbitDataFetchService;

  @GetMapping("/login")
  public ResponseEntity<String> getFitbitLoginForm() {
    return ResponseEntity.status(HttpStatus.FOUND)  // 302 redirect
        .location(URI.create(fitbitOAuthService.getFitbitLoginFormURI()))
        .build();
  }

  @GetMapping("/callback")
  public ResponseEntity<Map<String, String>> callback(@RequestParam("code") String code) {
    FitbitTokenResponse tokenResponse = fitbitOAuthService.getAccessToken(code);
    FitbitUserProfile fitbitUserProfile = fitbitDataFetchService.fetchUserProfile(
        tokenResponse.getAccessToken());
    return ResponseEntity.ok().body(Map.of("userId",
        fitbitOAuthService.signup(fitbitUserProfile, tokenResponse.getAccessToken()))
    );
  }
}
