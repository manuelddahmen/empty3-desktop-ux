FROM eclipse-temurin:25-jdk

WORKDIR /app
COPY . /app/

RUN ./gradlew classes testClasses --no-daemon

CMD ["./gradlew", "runClass", "-PclassName=one.empty3.testagentcode_jvm.MoonRotation4K"]
