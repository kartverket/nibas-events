FROM dhi.io/eclipse-temurin:25-alpine3.23@sha256:593a10f87c86086e7156d362fc3720f1a9b38668ba30c75111f51c49e784b8ff
EXPOSE 8080

USER nonroot
COPY --chown=nonroot:nonroot build/libs/*.jar /app.jar

ENTRYPOINT ["java", "-XX:MaxRAMPercentage=70.0", "-Duser.timezone=Europe/Oslo", "-jar", "/app.jar"]
