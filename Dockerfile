FROM dhi.io/eclipse-temurin:26-alpine3.23@sha256:eae0cfd72bae1bd05399e30675312f056570fa181121b354ddbd02032514031a
EXPOSE 8080

USER nonroot
COPY --chown=nonroot:nonroot build/libs/*.jar /app.jar

ENTRYPOINT ["java", "-XX:MaxRAMPercentage=70.0", "-Duser.timezone=Europe/Oslo", "-jar", "/app.jar"]
