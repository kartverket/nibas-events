FROM gcr.io/distroless/java25-debian13:nonroot@sha256:66a23001a1a4a5d02098c59fa27d265a8bb8e7d77d64464798e200441e53d040

EXPOSE 8080

COPY build/libs/*.jar /app.jar

ENTRYPOINT ["java", "-XX:MaxRAMPercentage=70.0", "-Duser.timezone=Europe/Oslo", "-jar", "/app.jar"]
