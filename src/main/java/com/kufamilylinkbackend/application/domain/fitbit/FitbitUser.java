package com.kufamilylinkbackend.application.domain.fitbit;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;


@Entity
@AllArgsConstructor
@Builder
@Getter
@NoArgsConstructor
public class FitbitUser {

  @Id
  private String fitbitUserId;
  private String name;
  private String gender;
  private int age;
  private String accessToken;

  public void updateAccessToken(String accessToken) {
    this.accessToken = accessToken;
  }
}
