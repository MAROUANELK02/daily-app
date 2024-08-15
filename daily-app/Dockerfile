# Stage 1: Build the Java app
FROM maven:3.8.5-openjdk-17-slim as build

WORKDIR /app

COPY pom.xml ./
COPY src ./src

RUN mvn clean package -DskipTests

# Stage 2: Run the Java app
FROM openjdk:17-jdk-slim

COPY --from=build /app/target/daily-app-0.0.1-SNAPSHOT.jar /app/daily-app-0.0.1-SNAPSHOT.jar

EXPOSE 5000

CMD ["java", "-jar", "/app/daily-app-0.0.1-SNAPSHOT.jar"]