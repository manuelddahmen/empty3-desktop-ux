# Stage 1: Build
FROM gradle:9.6-jdk25 AS build
WORKDIR /home/gradle/src
COPY --chown=gradle:gradle . .
# ADDED chmod +x here to fix the Permission Denied error
RUN mkdir -p /home/gradle/src/.jreleaser && echo "" > /home/gradle/src/.jreleaser/config.properties && chmod +x gradlew && ./gradlew wrapper && chmod +rx /home/gradle/src/gradle/wrapper/gradle-wrapper.jar && ./gradlew jar extractDeps prepareJPackageFiles --no-daemon -x test -PskipTestFromBuild -Dorg.gradle.jvmargs=-Xmx2g
# Stage 2: Run
FROM eclipse-temurin:25-jdk
WORKDIR /app
RUN pwd
# Explicitly copy the correct fat JAR
COPY --from=build /home/gradle/src/build/libs/empty3-desktop-ux.jar game-server.jar
COPY --from=build /home/gradle/src/build/app-image/*.jar ./

# Expose default port
EXPOSE 4712

# Run the application
ENTRYPOINT ["java", "-cp", "game-server.jar", "one.empty3.apps.opad.server.GameServerMain"]
