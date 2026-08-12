FROM eclipse-temurin:25-jdk-alpine as build
WORKDIR /workspace/app
COPY pom.xml .
COPY src src
RUN ./mvnw install -DskipTests
FROM eclipse-temurin:25-jre-alpine
VOLUME /tmp
COPY --from=build /workspace/app/target/*.jar app.jar
ENTRYPOINT ["java","-jar","/app.jar"]
