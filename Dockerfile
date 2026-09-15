FROM dhi.io/eclipse-temurin:26-alpine3.23@sha256:f86c1ad176fd4150a04e7b6080dbcc4b2a195f24f65611d36842deddfe2c8ed7
EXPOSE 8080

USER nonroot
COPY --chown=nonroot:nonroot build/libs/*.jar /app.jar

ENTRYPOINT ["java", "-XX:MaxRAMPercentage=70.0", "-Duser.timezone=Europe/Oslo", "-jar", "/app.jar"]
