package com.kufamilylinkbackend.application.service;

import com.kufamilylinkbackend.application.domain.fitbit.FitbitUser;
import com.kufamilylinkbackend.data.fitbit.FitbitTokenResponse;
import com.kufamilylinkbackend.data.fitbit.FitbitUserProfile;
import com.kufamilylinkbackend.infrastructure.repository.FitbitUserRepository;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

@Service
@RequiredArgsConstructor
public class FitbitOAuthService {

  private final RestTemplate restTemplate = new RestTemplate();
  private final FitbitUserRepository fitbitUserRepository;

  @Value("${fitbit.client-id}")
  private String clientId;

  @Value("${fitbit.redirect-uri}")
  private String redirectUri;

  @Value("${fitbit.client-secret}")
  private String clientSecret;

  @Value("${fitbit.scope}")
  private String scope;

  public String getFitbitLoginFormURI() {
    String baseUrl = "https://www.fitbit.com/oauth2/authorize";

    String url = UriComponentsBuilder.fromHttpUrl(baseUrl)
        .queryParam("response_type", "code")
        .queryParam("client_id", URLEncoder.encode(clientId, StandardCharsets.UTF_8))
        .queryParam("redirect_uri", URLEncoder.encode(redirectUri, StandardCharsets.UTF_8))
        .queryParam("scope", URLEncoder.encode(scope, StandardCharsets.UTF_8))
        .queryParam("expires_in", 604800)
        .build()
        .toUriString();
    System.out.println(url);
    return url;
  }

  public FitbitTokenResponse getAccessToken(String code) {
    HttpHeaders headers = new HttpHeaders();
    headers.setBasicAuth(clientId, clientSecret);
    headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

    MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
    params.add("grant_type", "authorization_code");
    params.add("code", code);
    params.add("redirect_uri", redirectUri);

    HttpEntity<MultiValueMap<String, String>> entity = new HttpEntity<>(params, headers);

    ResponseEntity<FitbitTokenResponse> response = restTemplate.exchange(
        "https://api.fitbit.com/oauth2/token",
        HttpMethod.POST,
        entity,
        FitbitTokenResponse.class
    );

    return response.getBody(); // JSON 응답 (access_token, user_id 등 포함)
  }

  @Transactional
  public String signup(FitbitUserProfile fitbitUserProfile, String accessToken) {
    FitbitUserProfile.User userInfo = fitbitUserProfile.getUser();
    Optional<FitbitUser> byId = fitbitUserRepository.findById(userInfo.getEncodedId());
    if (byId.isPresent()) {
      byId.get().updateAccessToken(accessToken);
      return userInfo.getEncodedId();
    }

    FitbitUser buildUser = FitbitUser.builder()
        .age(Integer.parseInt(userInfo.getAge()))
        .name(userInfo.getDisplayName())
        .gender(userInfo.getGender())
        .fitbitUserId(userInfo.getEncodedId())
        .accessToken(accessToken)
        .build();

    fitbitUserRepository.save(buildUser);
    return userInfo.getEncodedId();
  }
}
