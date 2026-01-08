FROM eclipse-temurin:25-jdk@sha256:572fe7b5b3ca8beb3b3aca96a7a88f1f7bc98a3bdffd03784a4568962c1a963a
ARG project_version_arg

ENV PROJECT_VERSION=$project_version_arg
ENV GROUP_NAME=nibas
ENV GROUP_ID=199
ENV USER_NAME=nibas-events
ENV USER_ID=199

RUN groupadd -g ${GROUP_ID} ${GROUP_NAME} && useradd --uid ${USER_ID}  -G ${GROUP_NAME} ${USER_NAME}

# Set timezone to Oslo
RUN apk add --no-cache tzdata
ENV TZ=Europe/Oslo

EXPOSE 8080
RUN mkdir /nibas-events
COPY build/libs/*.jar /nibas-events/app.jar
VOLUME /tmp
USER ${USER_ID}
ENTRYPOINT ["java", "-XX:MaxRAMPercentage=70.0", "-Djava.security.egd=file:/dev/./urandom","-jar", "/nibas-events/app.jar"]
