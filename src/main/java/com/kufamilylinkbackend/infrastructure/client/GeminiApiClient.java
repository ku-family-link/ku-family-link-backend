package com.kufamilylinkbackend.infrastructure.client;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

@Component
public class GeminiApiClient {


  @Value("${gemini.key}")
  private String GEMINI_KEY;

  public String requestPrompt(String prompt) {
    try {
      URI uri = UriComponentsBuilder
          .fromUriString("https://generativelanguage.googleapis.com")
          .path("/v1beta/models/gemini-1.5-flash-latest:generateContent")
          .queryParam("key", GEMINI_KEY)
          .encode()
          .build()
          .toUri();

      // 요청 본문 JSON 데이터 설정
      String jsonBody = String.format(
          "{ \"contents\": [ { \"parts\": [ { \"text\": \"%s\" } ] } ] }",
          prompt
      );

      HttpClient client = HttpClient.newHttpClient();
      HttpRequest request = HttpRequest.newBuilder()
          .uri(uri)
          .header("Content-Type", "application/json")
          .POST(HttpRequest.BodyPublishers.ofString(jsonBody))
          .build();

      HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

      return getTextFromResponse(convertJsonToMap(response.body()));
    }
    catch (Exception e) {
      e.printStackTrace();
      return "요청 실패";
    }
  }

  private Map<String, Object> convertJsonToMap(String json) {
    ObjectMapper objectMapper = new ObjectMapper();
    Map<String, Object> map;

    try {
      map = objectMapper.readValue(json, Map.class);
    } catch (Exception e) {
      throw new RuntimeException("convert json to Map fail");
    }

    return map;
  }

  private String getTextFromResponse(Map<String, Object> map) {
    List<Map<String, Object>> candidates = (List<Map<String, Object>>) map.get("candidates");
    Map<String, Object> content = (Map<String, Object>) candidates.get(0).get("content");
    List<Map<String, String>> parts = (List<Map<String, String>>) content.get("parts");
    String responseText = parts.get(0).get("text");
    return responseText;
  }

}
