# JDK 17을 기반으로 하는 이미지 사용
FROM openjdk:17-jdk-slim

# 작업 디렉토리 설정
WORKDIR /app

# Gradle로 빌드된 JAR 파일을 컨테이너로 복사
COPY build/libs/*.jar app.jar

# 애플리케이션 포트 노출 (기본값 8080, 필요시 변경)
EXPOSE 8080

# 애플리케이션 실행
ENTRYPOINT ["java", "-jar", "/app/app.jar"]
