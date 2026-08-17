# syntax=docker/dockerfile:1.4
# Stage 1: Build & Package
FROM eclipse-temurin:25-jdk-noble AS builder
WORKDIR /workspace

# Cache de dependencias de Maven
COPY pom.xml ./
COPY src ./src

RUN mvn clean package -DskipTests -B

# Stage 2: Distroless Runtime
FROM gcr.io/distroless/java25-debian12:nonroot
WORKDIR /app

COPY --from=builder /workspace/target/ProyectoPlayasInteligentesCostas-*.jar app.jar

USER nonroot:nonroot
ENV JAVA_TOOL_OPTIONS="-XX:+UseVirtualThreads -XX:MaxRAMPercentage=75.0 -Djava.security.egd=file:/dev/./urandom"

EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
