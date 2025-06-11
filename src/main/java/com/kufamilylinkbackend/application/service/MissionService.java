package com.kufamilylinkbackend.application.service;

import com.kufamilylinkbackend.application.domain.fitbit.FitbitUser;
import com.kufamilylinkbackend.application.domain.fitbit.health.StepRecord;
import com.kufamilylinkbackend.data.response.MissionResponse;
import com.kufamilylinkbackend.infrastructure.repository.FitbitUserRepository;
import com.kufamilylinkbackend.infrastructure.repository.StepRecordRepository;
import java.time.LocalDate;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class MissionService {
    private final StepRecordRepository stepRecordRepository;
    private final FitbitUserRepository fitbitUserRepository;
    private final FitbitSaveDataService fitbitSaveDataService;

    private static final int DAILY_STEP_GOAL = 3000;

    @Transactional(readOnly = true)
    public MissionResponse getTodayMission(String userId) {
        // 최신 스텝 데이터를 저장 (오늘 기준)
        fitbitSaveDataService.saveSteps(userId, LocalDate.now());

        FitbitUser user = fitbitUserRepository.findById(userId).orElseThrow();
        int stepCount = stepRecordRepository.findByFitbitUserAndDate(user, LocalDate.now())
            .map(StepRecord::getStepCount)
            .orElse(0);

        boolean completed = stepCount >= DAILY_STEP_GOAL;
        String description = String.format("%d보 걷기", DAILY_STEP_GOAL);

        return new MissionResponse(description, DAILY_STEP_GOAL, stepCount, completed);
    }
}
