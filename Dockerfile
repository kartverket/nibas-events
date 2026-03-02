FROM gcr.io/distroless/java25-debian13:nonroot@sha256:4eadd00d3bff73e6a7491dd36653c1d318ac93fb1fb2cd5eef768fd2b4238408

EXPOSE 8080

COPY build/libs/*.jar /app.jar

ENTRYPOINT ["java", "-XX:MaxRAMPercentage=70.0", "-Duser.timezone=Europe/Oslo", "-jar", "/app.jar"]
