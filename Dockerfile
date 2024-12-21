FROM maven:3.9-eclipse-temurin-21-alpine AS build
COPY . /app
WORKDIR /app
RUN mvn -e clean package -DskipTests

FROM openjdk:21-slim

WORKDIR /app

COPY --from=build /app/target/*.jar ./app.jar
ENTRYPOINT ["java", "-jar", "app.jar"]