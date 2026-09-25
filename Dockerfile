FROM dhi.io/eclipse-temurin:26-alpine3.23@sha256:5d6b41e324f47216ab8fd6074e567ccb6ff7711998a3c99cc0d22bc3b0ff62a2
EXPOSE 8080

USER nonroot
COPY --chown=nonroot:nonroot build/libs/*.jar /app.jar

ENTRYPOINT ["java", "-XX:MaxRAMPercentage=70.0", "-Duser.timezone=Europe/Oslo", "-jar", "/app.jar"]
