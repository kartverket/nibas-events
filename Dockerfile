FROM dhi.io/eclipse-temurin:26-alpine3.23@sha256:fe31cc53e56ee9dd3ca1268fca813e4bdcad663f0b87a7ab31280f5b12b08e1e
EXPOSE 8080

USER nonroot
COPY --chown=nonroot:nonroot build/libs/*.jar /app.jar

ENTRYPOINT ["java", "-XX:MaxRAMPercentage=70.0", "-Duser.timezone=Europe/Oslo", "-jar", "/app.jar"]
