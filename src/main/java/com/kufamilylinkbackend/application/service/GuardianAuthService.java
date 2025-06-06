package com.kufamilylinkbackend.application.service;

import com.kufamilylinkbackend.application.domain.fitbit.FitbitUser;
import com.kufamilylinkbackend.application.domain.fitbit.Guardian;
import com.kufamilylinkbackend.data.request.GuardianLoginRequest;
import com.kufamilylinkbackend.data.request.GuardianSignupRequest;
import com.kufamilylinkbackend.data.response.GuardianLoginResponse;
import com.kufamilylinkbackend.data.response.GuardianSignupResponse;
import com.kufamilylinkbackend.global.exception.ApplicationException;
import com.kufamilylinkbackend.global.exception.ErrorCode;
import com.kufamilylinkbackend.global.util.Argon2PasswordEncoder;
import com.kufamilylinkbackend.infrastructure.repository.FitbitUserRepository;
import com.kufamilylinkbackend.infrastructure.repository.GuardianRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class GuardianAuthService {

  private final GuardianRepository guardianRepository;
  private final FitbitUserRepository fitbitUserRepository;

  @Transactional
  public GuardianSignupResponse signup(GuardianSignupRequest request) {
    // validation: 회원가입된 유저인지 확인
    if (guardianRepository.existsByEmail(request.email())) {
      throw new ApplicationException(ErrorCode.USER_ALREADY_EXIST_EXCEPTION);
    }
    FitbitUser fitbitUser = fitbitUserRepository.findById(request.clientageId())
        .orElseThrow(() -> new ApplicationException(ErrorCode.FITBIT_USER_NOT_FOUND_EXCEPTION));
    // business logic: 비밀번호 인코딩 후 유저 저장
    String encodedPassword = Argon2PasswordEncoder.encode(request.password().toCharArray());

    // businessLogic: Guardian Entity를 만들고 저장함.
    Guardian newGuardian = Guardian.builder()
        .email(request.email())
        .password(encodedPassword)
        .name(request.name())
        .phone(request.phone())
        .clientage(fitbitUser)
        .build();

    Guardian save = guardianRepository.save(newGuardian);

    // return: 유저 id 반환
    return new GuardianSignupResponse(save.getId());
  }

  @Transactional(readOnly = true)
  public GuardianLoginResponse login(GuardianLoginRequest request) {
    // validation: 회원가입된 유저인지 확인
    Guardian findGuardian = guardianRepository.findByEmail(request.email())
        .orElseThrow(() -> new ApplicationException(ErrorCode.GUARDIAN_NOT_FOUND_EXCEPTION));

    // validation: 비밀번호 확인
    if (!Argon2PasswordEncoder.matches(request.password().toCharArray(), findGuardian.getPassword())) {
      throw new ApplicationException(ErrorCode.PASSWORD_MISMATCH_EXCEPTION);
    }

    return new GuardianLoginResponse(findGuardian.getId());
  }

}
