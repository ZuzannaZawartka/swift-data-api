# Użyj oficjalnego obrazu JDK jako bazowego
FROM openjdk:17-jdk-alpine

# Ustaw katalog roboczy w kontenerze
ARG JAR_FILE=target/*.jar

# Skopiuj plik JAR aplikacji do kontenera
COPY ./target/swift-data-api-0.0.1-SNAPSHOT.jar app.jar

# Uruchom aplikację
ENTRYPOINT ["java", "-jar", "app.jar"]
