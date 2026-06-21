FROM dhi.io/eclipse-temurin:25-alpine3.23@sha256:b854ef99193c77ca1c6655c813ca57ab55c0fb0f0f4307511c8889045d0d5a19
EXPOSE 8080

USER nonroot
COPY --chown=nonroot:nonroot build/libs/*.jar /app.jar

ENTRYPOINT ["java", "-XX:MaxRAMPercentage=70.0", "-Duser.timezone=Europe/Oslo", "-jar", "/app.jar"]
