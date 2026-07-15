FROM dhi.io/eclipse-temurin:26-alpine3.23@sha256:e22aa723b38700b7f5924490a3f741190e39fa11817608c4604548856139fb18
EXPOSE 8080

USER nonroot
COPY --chown=nonroot:nonroot build/libs/*.jar /app.jar

ENTRYPOINT ["java", "-XX:MaxRAMPercentage=70.0", "-Duser.timezone=Europe/Oslo", "-jar", "/app.jar"]
