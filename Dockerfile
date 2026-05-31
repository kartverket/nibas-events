FROM dhi.io/eclipse-temurin:25-alpine3.23@sha256:40de9c71890390b202e59d566f954c8832482c2ee82744f2f73d1e5b48ed3eae
EXPOSE 8080

USER nonroot
COPY --chown=nonroot:nonroot build/libs/*.jar /app.jar

ENTRYPOINT ["java", "-XX:MaxRAMPercentage=70.0", "-Duser.timezone=Europe/Oslo", "-jar", "/app.jar"]
