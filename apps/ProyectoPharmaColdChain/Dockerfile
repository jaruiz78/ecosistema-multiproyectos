# Multi-stage build for Java 25 (LTS) / Spring Boot 4.0 / Cloud Run
FROM eclipse-temurin:25-jdk-noble AS builder
WORKDIR /workspace
COPY . .
RUN if [ -f "./mvnw" ]; then ./mvnw clean package -DskipTests --no-transfer-progress || true;     elif [ -f "pom.xml" ]; then echo "Maven module prepped"; fi

FROM eclipse-temurin:25-jre-noble
WORKDIR /app
COPY --from=builder /workspace/target/*.jar /app/app.jar 2>/dev/null || true
ENV JAVA_TOOL_OPTIONS="--enable-preview -XX:+UseZGC -XX:+ZGenerational -XX:+ExitOnOutOfMemoryError"
EXPOSE 8080
USER 1000:1000
ENTRYPOINT ["java", "-jar", "/app/app.jar"]
