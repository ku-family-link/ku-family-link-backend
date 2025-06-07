package com.kufamilylinkbackend.data.response;

import com.kufamilylinkbackend.application.domain.fitbit.Guardian;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@AllArgsConstructor
@Builder
@Getter
public class GuardianUserMyPageResponse {
  private String fitbitUserId;
  private String fitbitUserName;
  private String fitbitUserGender;
  private int fitbitUserAge;
  private String name;
  private String phone;
  private String relationship;

  public static GuardianUserMyPageResponse of(Guardian guardian) {
    return GuardianUserMyPageResponse.builder()
        .name(guardian.getName())
        .phone(guardian.getPhone())
        .relationship(guardian.getRelationship())
        .fitbitUserName(guardian.getClientage().getName())
        .fitbitUserAge(guardian.getClientage().getAge())
        .fitbitUserGender(guardian.getClientage().getGender())
        .fitbitUserId(guardian.getClientage().getFitbitUserId())
        .build();
  }
}
