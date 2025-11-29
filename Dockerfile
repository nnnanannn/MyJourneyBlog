# --- STAGE 1: Build the Application ---
# JDK to compile the code
FROM maven:3.9-eclipse-temurin-17-alpine AS builder
WORKDIR /app

# Copy target JUST for local computer
# COPY target/*.jar app.jar
# 1. Copy just the Maven wrapper and pom.xml first (for caching)
COPY .mvn/ .mvn
COPY mvnw pom.xml ./

# 2. Download dependencies (this step is cached if pom.xml doesn't change)
RUN mvn dependency:go-offline

# 3. Copy source code and build
COPY src ./src
RUN mvn clean package -DskipTests

# --- STAGE 2: Run the Application ---
# JRE (Runtime Environment) which is smaller and safer
FROM eclipse-temurin:17-jre-alpine
WORKDIR /app

# Copy the JAR file from the "builder" stage above
COPY --from=builder /app/target/*.jar app.jar

EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]