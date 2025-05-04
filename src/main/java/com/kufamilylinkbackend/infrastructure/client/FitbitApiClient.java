package com.kufamilylinkbackend.infrastructure.client;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class FitbitApiClient {

  private final RestTemplate restTemplate = new RestTemplate();

  public <T> T fetch(String url, String accessToken, Class<T> responseType) {
    // accessToken 을 헤더에 셋팅
    HttpHeaders headers = new HttpHeaders();
    headers.setBearerAuth(accessToken);
    HttpEntity<Void> entity = new HttpEntity<>(headers);

    //url 에 대한 Get 요청
    ResponseEntity<T> response = restTemplate.exchange(
        url,
        HttpMethod.GET,
        entity,
        responseType
    );

    return response.getBody();
  }
}
