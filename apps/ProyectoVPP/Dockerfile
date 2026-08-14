# Multi-stage Dockerfile for ProyectoVPP (Java 25 LTS & Spring Boot 4.0)
FROM maven:3.9.9-eclipse-temurin-25 AS builder
WORKDIR /app

COPY pom.xml .
COPY src ./src

RUN mvn clean package -DskipTests

# Generar archivo de clases compartidas Leyden CDS
RUN java -Dspring.context.exit=on-refresh -Dspring.aot.enabled=true -XX:ArchiveClassesAtExit=application.jsa -jar target/*.jar || true

# Etapa 2: Runtime JRE 25 Seguro (Non-Root)
FROM eclipse-temurin:25-jre-jammy AS runtime
WORKDIR /app

RUN addgroup --system spring && adduser --system spring --ingroup spring

COPY --from=builder /app/target/*.jar app.jar
COPY --from=builder /app/application.jsa ./application.jsa

RUN chown -R spring:spring /app
USER spring:spring

ENV JAVA_OPTS="-XX:+UnlockExperimentalVMOptions -XX:+UseCompactObjectHeaders -XX:+UseZGC -XX:+ZGenerational -Djava.security.egd=file:/dev/./urandom"
EXPOSE 8080 9090

ENTRYPOINT ["sh", "-c", "if [ -f application.jsa ]; then exec java --enable-preview $JAVA_OPTS -XX:SharedArchiveFile=application.jsa -jar app.jar; else exec java --enable-preview $JAVA_OPTS -jar app.jar; fi"]