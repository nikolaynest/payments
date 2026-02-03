# -----------------------
# Stage 1: Build JAR
# -----------------------
FROM maven:3.9.12-eclipse-temurin-21-alpine AS build
WORKDIR /app

COPY pom.xml .
RUN mvn dependency:go-offline

COPY src ./src

RUN mvn clean package -DskipTests

# -----------------------
# Stage 2: Run JAR
# -----------------------
FROM eclipse-temurin:21-jdk-alpine
WORKDIR /app

COPY --from=build /app/target/payments-1.0-SNAPSHOT.jar payments.jar

ENTRYPOINT ["java", "-jar", "/app/payments.jar"]
