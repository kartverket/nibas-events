FROM gcr.io/distroless/java25-debian13:nonroot@sha256:b1eb8a18891104b7405f29edbb2eaca9b34179707957a0e5a41b54d4a45cbdfd

EXPOSE 8080

COPY build/libs/*.jar /app.jar

ENTRYPOINT ["java", "-XX:MaxRAMPercentage=70.0", "-Duser.timezone=Europe/Oslo", "-jar", "/app.jar"]
