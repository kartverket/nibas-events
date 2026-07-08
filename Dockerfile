FROM dhi.io/eclipse-temurin:25-alpine3.23@sha256:6e8460b64ed88f558f1d59ffb2378716d7795e3b08ac1af98e0343b5b0e5382b
EXPOSE 8080

USER nonroot
COPY --chown=nonroot:nonroot build/libs/*.jar /app.jar

ENTRYPOINT ["java", "-XX:MaxRAMPercentage=70.0", "-Duser.timezone=Europe/Oslo", "-jar", "/app.jar"]
