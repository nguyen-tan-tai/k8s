# Build Stage
FROM maven:3.9.6-eclipse-temurin-21 AS builder
WORKDIR /build
# Copy the pom.xml and source code
COPY pom.xml .
COPY src ./src
# Build the application
RUN mvn clean package -DskipTests

# Runtime Stage
FROM eclipse-temurin:21-jre-alpine

# Set the working directory inside the container
WORKDIR /app

# Copy the OpenTelemetry Java Agent from local jars folder
COPY jars/opentelemetry-javaagent.jar opentelemetry-javaagent.jar

# Copy the built jar from the builder stage
COPY --from=builder /build/target/*.jar app.jar

# Expose the standard Spring Boot port
EXPOSE 8080

# Configure the environment to point to observability endpoints if needed
# We must use http://tempo:4318 for OTLP HTTP because 4317 is for gRPC
ENV OTEL_EXPORTER_OTLP_ENDPOINT=http://tempo:4318
# Tell the agent to ONLY export traces to Tempo, because Tempo DOES NOT accept metrics or logs!
ENV OTEL_METRICS_EXPORTER=none
ENV OTEL_LOGS_EXPORTER=none
ENV OTEL_SERVICE_NAME=demo-app

# Execute the application
ENTRYPOINT ["java", "-javaagent:opentelemetry-javaagent.jar", "-jar", "app.jar"]
