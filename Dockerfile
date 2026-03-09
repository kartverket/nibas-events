FROM gcr.io/distroless/java25-debian13:nonroot@sha256:ace83a068839dbfb151b0d80693df23120f6d13f963427fde7e43d9a175fd54a

EXPOSE 8080

COPY build/libs/*.jar /app.jar

ENTRYPOINT ["java", "-XX:MaxRAMPercentage=70.0", "-Duser.timezone=Europe/Oslo", "-jar", "/app.jar"]
