FROM openjdk:23-jdk-slim

WORKDIR /app

COPY build.gradle settings.gradle ./
COPY gradlew ./
COPY gradle ./gradle
COPY src ./src
COPY document ./document

RUN ./gradlew build --no-daemon

CMD ["./gradlew", "bootRun"]