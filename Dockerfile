FROM gcr.io/distroless/java25-debian13:nonroot@sha256:2e3d8fc12381605a522cb015f551cb340fd857afb51c71d7decf4fea0d8802d5

EXPOSE 8080

COPY build/libs/*.jar /app.jar

ENTRYPOINT ["java", "-XX:MaxRAMPercentage=70.0", "-Duser.timezone=Europe/Oslo", "-jar", "/app.jar"]
