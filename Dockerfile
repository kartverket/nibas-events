FROM gcr.io/distroless/java25-debian13:nonroot@sha256:7c2ef3e630046d73ae8372add70a23cbf6f0c334c76c6a9196a21b01abb45559

EXPOSE 8080

COPY build/libs/*.jar /app.jar

ENTRYPOINT ["java", "-XX:MaxRAMPercentage=70.0", "-Duser.timezone=Europe/Oslo", "-jar", "/app.jar"]
