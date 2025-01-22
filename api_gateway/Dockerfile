# Use an OpenJDK 17 Alpine image as the base image
FROM openjdk:17-jdk-alpine
# Copy the JAR file from the host to the Docker image
COPY target/api_gateway-1.0-SNAPSHOT.jar api_gateway.jar
# Specify the command to run the JAR file
ENTRYPOINT ["java", "-jar", "/api_gateway.jar"]
EXPOSE 8090

