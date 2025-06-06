package com.kufamilylinkbackend.presentation.controller;

import com.kufamilylinkbackend.application.service.GuardianAuthService;
import com.kufamilylinkbackend.data.request.GuardianLoginRequest;
import com.kufamilylinkbackend.data.request.GuardianSignupRequest;
import com.kufamilylinkbackend.data.response.GuardianLoginResponse;
import com.kufamilylinkbackend.data.response.GuardianSignupResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/guardian/auth")
@RequiredArgsConstructor
public class GuardianAuthController {

  private final GuardianAuthService guardianAuthService;

  @PostMapping("/signup")
  public ResponseEntity<GuardianSignupResponse> signup(@RequestBody GuardianSignupRequest request) {
    return ResponseEntity.status(HttpStatus.CREATED).body(guardianAuthService.signup(request));

  }

  @PostMapping("/login")
  public ResponseEntity<GuardianLoginResponse> login(@RequestBody GuardianLoginRequest request) {
    return ResponseEntity.ok(guardianAuthService.login(request));
  }
}
