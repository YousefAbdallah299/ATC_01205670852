FROM maven:3.8.5-openjdk-17 AS build
WORKDIR /app
COPY . .
RUN mvn clean package -DskipTests
RUN ls -lah /app/target

FROM openjdk:17-jdk-slim
WORKDIR /app
COPY --from=build /app/target/*.jar event-booking-service.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "event-booking-service.jar"]
