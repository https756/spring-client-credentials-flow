# STAGE 1: dependencies
FROM eclipse-temurin:25-jdk-alpine AS depenencies
WORKDIR /build

RUN apk add --no-cache bash

COPY gradlew .
COPY gradle gradle
COPY build.gradle settings.gradle ./

RUN ./gradlew dependencies --no-daemon


# STAGE 2: build
FROM depenencies AS build
COPY src ./src

RUN ./gradlew build -x test --no-daemon


# STAGE 3: runtime
FROM eclipse-temurin:25-jre-alpine AS runtime
WORKDIR /build

RUN apk add --no-cache curl

COPY --from=build /build/build/libs/*.jar app.jar

ENTRYPOINT ["java", "-jar", "app.jar"]