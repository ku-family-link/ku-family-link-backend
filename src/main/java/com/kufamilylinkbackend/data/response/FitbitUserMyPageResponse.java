package com.kufamilylinkbackend.data.response;

import com.kufamilylinkbackend.application.domain.fitbit.FitbitUser;
import com.kufamilylinkbackend.application.domain.fitbit.Guardian;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@AllArgsConstructor
@Builder
@Getter
public class FitbitUserMyPageResponse {
  private String fitbitUserId;
  private String name;
  private String gender;
  private int age;
  private String guardianName;
  private String guardianPhone;
  private String guardianRelationship;

  public static FitbitUserMyPageResponse of(FitbitUser fitbitUser, Guardian guardian) {
    return FitbitUserMyPageResponse.builder()
        .age(fitbitUser.getAge())
        .fitbitUserId(fitbitUser.getFitbitUserId())
        .name(fitbitUser.getName())
        .gender(fitbitUser.getGender())
        .guardianPhone(guardian.getPhone())
        .guardianRelationship(guardian.getRelationship())
        .guardianName(guardian.getName())
        .build();
  }
}
