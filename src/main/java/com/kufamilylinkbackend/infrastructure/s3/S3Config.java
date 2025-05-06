package com.kufamilylinkbackend.infrastructure.s3;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.auth.credentials.EnvironmentVariableCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;

@Configuration
public class S3Config {

  @Value("${spring.cloud.aws.region.static}")
  private String region;

  @Bean
  public S3Client s3Client() {
    return S3Client.builder()
        .region(Region.of(region))
        .credentialsProvider(EnvironmentVariableCredentialsProvider.create()) // 또는 Profile/Static 방식
        .build();
  }

}
