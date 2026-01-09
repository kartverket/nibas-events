FROM gcr.io/distroless/java25-debian13:nonroot@sha256:fa9bfc14924fa3b43d43944d93887155d19843b3aa45610b659496f928fe2a9c

EXPOSE 8080

COPY build/libs/*.jar /app.jar

ENTRYPOINT ["java", "-XX:MaxRAMPercentage=70.0", "-Duser.timezone=Europe/Oslo", "-jar", "/app.jar"]
