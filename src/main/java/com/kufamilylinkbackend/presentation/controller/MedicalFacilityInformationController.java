package com.kufamilylinkbackend.presentation.controller;

import com.kufamilylinkbackend.infrastructure.client.PublicDataApiClient;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/medical/facility")
@RequiredArgsConstructor
public class MedicalFacilityInformationController {

  private final PublicDataApiClient publicDataApiClient;

  @GetMapping("/emergency/nearby")
  public ResponseEntity<String> getNearbyHospitals(
      @RequestParam("lat") double latitude,
      @RequestParam("lon") double longitude  ) {
    return ResponseEntity.ok(publicDataApiClient.getEmergencyHospitalInfo(latitude, longitude));
  }
}
