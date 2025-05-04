package com.kufamilylinkbackend.data.fitbit;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Data;

@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class FitbitTokenResponse {

  private String accessToken;
  private int expiresIn;
  private String refreshToken;
  private String scope;
  private String tokenType;
  private String userId;
}