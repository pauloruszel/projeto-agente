FROM eclipse-temurin:21-jdk AS builder
WORKDIR /workspace

# Preload Maven wrapper and dependencies
COPY .mvn .mvn
COPY mvnw pom.xml ./
RUN chmod +x mvnw \
    && ./mvnw -ntp dependency:go-offline

# Build the application
COPY src ./src
RUN ./mvnw -ntp clean package -DskipTests

FROM eclipse-temurin:21-jre
WORKDIR /app
ENV SPRING_PROFILES_ACTIVE=default \
    JAVA_OPTS=""

COPY --from=builder /workspace/target/projeto-agente-0.0.1-SNAPSHOT.jar app.jar

EXPOSE 8080
ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar app.jar"]
