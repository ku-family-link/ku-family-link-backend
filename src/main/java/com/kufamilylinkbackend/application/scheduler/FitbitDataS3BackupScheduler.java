package com.kufamilylinkbackend.application.scheduler;

import com.kufamilylinkbackend.application.domain.fitbit.health.HeartRateRecord;
import com.kufamilylinkbackend.application.domain.fitbit.health.SleepRecord;
import com.kufamilylinkbackend.application.domain.fitbit.health.StepRecord;
import com.kufamilylinkbackend.data.s3.HeartRateRecordS3Dto;
import com.kufamilylinkbackend.data.s3.SleepRecordS3Dto;
import com.kufamilylinkbackend.data.s3.StepRecordS3Dto;
import com.kufamilylinkbackend.infrastructure.repository.HeartRateRecordRepository;
import com.kufamilylinkbackend.infrastructure.repository.SleepRecordRepository;
import com.kufamilylinkbackend.infrastructure.repository.StepRecordRepository;
import com.kufamilylinkbackend.infrastructure.s3.S3Uploader;
import java.io.IOException;
import java.time.LocalDate;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class FitbitDataS3BackupScheduler {

  private final SleepRecordRepository sleepRepo;
  private final StepRecordRepository stepRepo;
  private final HeartRateRecordRepository heartRepo;
  private final S3Uploader uploader;

  @Scheduled(cron = "0 30 0 * * *")
  @Transactional(readOnly = true)
  public void backupYesterdayDataToS3() {
    LocalDate date = LocalDate.now().minusDays(1);

    // 모든 유저에 대해 처리
    List<SleepRecord> sleeps = sleepRepo.findAllByDate(date);
    List<StepRecord> steps = stepRepo.findAllByDate(date);
    List<HeartRateRecord> heartRates = heartRepo.findAllByDate(date);

    sleeps.forEach(record -> {
      try {
        String key = String.format("fitbit-data/%s/%s/sleep.json", record.getFitbitUser().getFitbitUserId(), date);
        uploader.uploadJson(key, SleepRecordS3Dto.from(record));
      } catch (IOException e) {
        e.printStackTrace();
      }
    });

    steps.forEach(record -> {
      try {
        String key = String.format("fitbit-data/%s/%s/steps.json", record.getFitbitUser().getFitbitUserId(), date);
        uploader.uploadJson(key, StepRecordS3Dto.from(record));
      } catch (IOException e) {
        e.printStackTrace();
      }
    });

    heartRates.forEach(record -> {
      try {
        String key = String.format("fitbit-data/%s/%s/heart-rate.json", record.getFitbitUser().getFitbitUserId(), date);
        uploader.uploadJson(key, HeartRateRecordS3Dto.from(record));
      } catch (IOException e) {
        e.printStackTrace();
      }
    });
  }
}
