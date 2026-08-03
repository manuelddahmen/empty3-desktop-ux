# Stage 1: Build
FROM gradle:9.6-jdk25 AS build
WORKDIR /home/gradle/src
COPY --chown=gradle:gradle . .
# ADDED chmod +x here to fix the Permission Denied error
RUN gradle --stop
RUN set -e mkdir -p /home/gradle/src/.jreleaser && echo "" > /home/gradle/src/.jreleaser/config.properties && chmod +x gradlew && ./gradlew wrapper && chmod +rx /home/gradle/src/gradle/wrapper/gradle-wrapper.jar && ./gradlew jar extractDeps -x test -PskipTestFromBuild #--no-daemon
# Stage 2: Run
FROM eclipse-temurin:25-jdk
WORKDIR /app
RUN pwd
# Explicitly copy the correct fat JAR
COPY --from=build /home/gradle/src/build/libs/empty3-library-mp-with-dependencies.jar game-server.jar

# Expose default port
EXPOSE 4712

# Run the application
ENTRYPOINT ["java", "-jar", "game-server.jar", "one.empty3.apps.opad.server.GameServerMain"]
