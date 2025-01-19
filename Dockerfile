FROM openjdk:17-jdk

# 빌드된 JAR 파일 복사
ARG JAR_FILE=build/libs/*.jar
COPY ${JAR_FILE} app.jar

# application.yml 복사 (src/main/resources에서 올바른 경로로 복사)
COPY src/main/resources/application.yml /app/config/application.yml

# Spring Boot 애플리케이션 실행 시 application.yml 경로 명시
ENTRYPOINT ["java", "-jar", "/app.jar", "--spring.config.location=file:/app/config/application.yml"]