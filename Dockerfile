FROM dhi.io/eclipse-temurin:25-alpine3.23@sha256:28763c61fcfa4bd165b1b2f0a4829e7b33cee655b8d66c6d8e66b52edf34b794
EXPOSE 8080

USER nonroot
COPY --chown=nonroot:nonroot build/libs/*.jar /app.jar

ENTRYPOINT ["java", "-XX:MaxRAMPercentage=70.0", "-Duser.timezone=Europe/Oslo", "-jar", "/app.jar"]
