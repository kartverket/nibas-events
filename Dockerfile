FROM gcr.io/distroless/java25-debian13:nonroot@sha256:29a8dfd3f2357a0b32839c2728893f5bcdacdde00eafa45c5c7b95e6f264b2b1

EXPOSE 8080

COPY build/libs/*.jar /app.jar

ENTRYPOINT ["java", "-XX:MaxRAMPercentage=70.0", "-Duser.timezone=Europe/Oslo", "-jar", "/app.jar"]
