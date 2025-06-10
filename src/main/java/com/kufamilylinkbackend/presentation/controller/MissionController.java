package com.kufamilylinkbackend.presentation.controller;

import com.kufamilylinkbackend.application.service.MissionService;
import com.kufamilylinkbackend.data.response.MissionResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class MissionController {

    private final MissionService missionService;

    @GetMapping("/{userId}/mission/today")
    public ResponseEntity<MissionResponse> getTodayMission(@PathVariable String userId) {
        return ResponseEntity.ok(missionService.getTodayMission(userId));
    }
}
