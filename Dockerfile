FROM gcr.io/distroless/java25-debian13:nonroot@sha256:efb9a5000ce8ff56745d56c88c8e61017e674ed053b67e0c44af25ddabf1faa8

EXPOSE 8080

COPY build/libs/*.jar /app.jar

ENTRYPOINT ["java", "-XX:MaxRAMPercentage=70.0", "-Duser.timezone=Europe/Oslo", "-jar", "/app.jar"]
