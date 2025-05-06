package com.kufamilylinkbackend.infrastructure.client;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class PublicDataApiClient {

  private final RestTemplate restTemplate = new RestTemplate();

  @Value("${public-api.data.emergency-location.key}")
  private String emergencyLocationApiKey;

  public String getEmergencyHospitalInfo(double latitude, double longitude) {
    try {
      String urlStr = String.format(
          "https://apis.data.go.kr/B552657/ErmctInfoInqireService/getEgytLcinfoInqire" +
              "?serviceKey=%s&WGS84_LON=%f&WGS84_LAT=%f", emergencyLocationApiKey, longitude,
          latitude
      );

      HttpClient client = HttpClient.newHttpClient();
      HttpRequest request = HttpRequest.newBuilder()
          .uri(URI.create(urlStr))
          .header("Accept", "application/xml")
          .header("User-Agent", "Mozilla/5.0")
          .GET()
          .build();

      HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
      System.out.println(urlStr);
      System.out.println("응답 코드: " + response.statusCode());
      return response.body();

    } catch (Exception e) {
      e.printStackTrace();
      return "요청 실패";
    }
  }

}
