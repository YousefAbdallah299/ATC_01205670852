FROM maven:3.8.5-openjdk-17 AS build
COPY . .
RUN mvn clean package -DskipTests

FROM openjdk:17-jdk-slim
COPY --from=build /target/event-booking-service-0.0.1-SNAPSHOT.jar /event-booking-service.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "event-booking-service.jar"]
