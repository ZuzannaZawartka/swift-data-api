FROM openjdk:17-jdk-alpine
ARG JAR_FILE=target/swift-data-api-0.0.1-SNAPSHOT.jar

# Skopiowanie aplikacji
COPY ${JAR_FILE} app.jar

# Skopiowanie pliku Excel do odpowiedniego katalogu w obrazie
COPY src/main/resources/Interns_2025_SWIFT_CODES.xlsx /src/main/resources/

ENTRYPOINT ["java","-jar","/app.jar"]
