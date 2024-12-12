FROM openjdk:17-jdk-slim

WORKDIR /app

COPY build/libs/onedayowner-0.0.1-SNAPSHOT.jar app.jar
COPY src/main/resources/application.yaml src/main/resources/
COPY src/main/resources/private_key.pem src/main/resources/
COPY src/main/resources/public_key.pem src/main/resources/

ENV SPRING_PROFILES_ACTIVE=prod

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]