# 1. Java 17 이상 사용하는 경우
FROM openjdk:17-jdk-slim

# 2. 작업 디렉토리 생성
WORKDIR /app

# 3. jar 파일 복사 (build/libs 경로에 있는 jar)
COPY build/libs/*.jar app.jar

# 4. 포트 설정 (예: 8080)
EXPOSE 8080

# 5. 실행 명령어
ENTRYPOINT ["java", "-jar", "app.jar"]
