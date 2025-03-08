# Use a lightweight OpenJDK 17 base
FROM eclipse-temurin:17-jdk-alpine

# Install Syft (requires curl + bash)
RUN apk add --no-cache curl bash
RUN curl -sSfL https://raw.githubusercontent.com/anchore/syft/main/install.sh | bash -s -- -b /usr/local/bin

WORKDIR /app

# Copy the fat JAR built by `./gradlew clean shadowJar`
COPY build/libs/mytool-1.0.0-all.jar /app/MyTool.jar

ENTRYPOINT ["java", "-jar", "/app/MyTool.jar"]
