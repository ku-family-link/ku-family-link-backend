package com.kufamilylinkbackend.data.fitbit;

import lombok.Data;

@Data
public class FitbitUserProfile {
  private User user;

  @Data
  public static class User {
    private String encodedId;
    private String displayName;
    private String age;
    private String gender;
    private String email;
    private String country;
    private String memberSince;
  }
}
