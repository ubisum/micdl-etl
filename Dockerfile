# ===== Build base: Eclipse Temurin 21 (runtime) =====
FROM docker.io/eclipse-temurin:21-jre AS runtime

WORKDIR /app

# Copia il jar dal target (usato dallo stage Maven)
COPY target/*.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "/app/app.jar"]
